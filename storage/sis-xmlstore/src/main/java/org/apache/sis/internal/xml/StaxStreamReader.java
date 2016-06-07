/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.internal.xml;

import java.io.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Node;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import static javax.xml.stream.XMLStreamReader.*;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * An abstract class for all stax parser.<br>
 * Readers for a given specification should extend this class and
 * provide appropriate read methods.<br>
 * <br>
 * Example : <br>
 * <pre>
 * {@code
 * public class UserReader extends StaxStreamReader{
 *
 *   public User read() throws XMLStreamException{
 *      //casual stax reading operations
 *      return user;
 *   }
 * }
 * }
 * </pre>
 * And should be used like :<br>
 * <pre>
 * {@code
 * final UserReader instance = new UserReader();
 * try{
 *     instance.setInput(stream);
 *     user = instance.read();
 * }finally{
 *     instance.dispose();
 * }
 * }
 * </pre>
 *
 * @author Johann Sorel (Geomatys)
 * @since   0.7
 * @version 0.7
 * @module
 */
public abstract class StaxStreamReader extends AbstractConfigurable implements AutoCloseable {

    /**
     * Wrapped xml stream reader
     */
    protected XMLStreamReader reader;
    
    /**
     * Store the input stream if it was generated by the parser itself.
     * It will closed on the dispose method or when a new input is set.
     */
    private InputStream sourceStream;

    /**
     * close potentiel previous stream and cache if there are some.
     * This way the reader can be reused for a different input later.
     * The underlying stax reader will be closed.
     * 
     * @throws java.io.IOException if previous source stream caused an exception on close
     * @throws javax.xml.stream.XMLStreamException if previous stax reader caused an exception on close
     */
    public void reset() throws IOException, XMLStreamException {
        if(sourceStream != null){
            sourceStream.close();
            sourceStream = null;
        }
        if(reader != null){
            reader.close();
            reader = null;
        }
    }

    /**
     * Release potentiel locks or opened stream.
     * Must be called when the reader is not needed anymore.
     * It should not be used after this method has been called.
     */
    @Override
    public void close() throws Exception {
        reset();
    }
    
    /**
     * Set the input for this reader.<br>
     * Handle types are :<br>
     * - java.io.File<br>
     * - java.io.Reader<br>
     * - java.io.InputStream<br>
     * - java.net.URL<br>
     * - java.net.URI<br>
     * - javax.xml.stream.XMLStreamReader<br>
     * - javax.xml.transform.Source<br>
     * 
     * @param input input object
     * @throws IOException if input failed to be opened for any IO reason
     * @throws XMLStreamException if input is not a valid XML stream
     */
    public void setInput(Object input) throws IOException, XMLStreamException {
        reset();

        if(input instanceof XMLStreamReader){
            reader = (XMLStreamReader) input;
            return;
        }

        if(input instanceof File){
            sourceStream = new FileInputStream((File)input);
            input = sourceStream;
        }else if(input instanceof Path){
            sourceStream = Files.newInputStream((Path)input, StandardOpenOption.READ);
            input = sourceStream;
        }else if(input instanceof URL){
            sourceStream = ((URL)input).openStream();
            input = sourceStream;
        }else if(input instanceof URI){
            sourceStream = ((URI)input).toURL().openStream();
            input = sourceStream;
        }else if(input instanceof String){
            input = new StringReader((String) input);
        }

        reader = toReader(input);
    }

    /**
     * Iterator on the reader until it reachs the end of the given tag name.
     * @param tagName tag name to search
     * @throws XMLStreamException if end tag could not be found
     *                      or if there is error processing the xml stream
     */
    protected void toTagEnd(final String tagName) throws XMLStreamException {
        while (reader.hasNext()) {
            if(END_ELEMENT == reader.next() &&
               tagName.equalsIgnoreCase(reader.getLocalName()))
               return;
        }
        throw new XMLStreamException("Error in xml file, Could not find end of tag "+tagName+" .");
    }

    /**
     * Creates a new XMLStreamReader.
     * @param input input to convert to stax stream
     * @return XMLStreamReader
     * @throws XMLStreamException if the input is not handled
     */
    private static final XMLStreamReader toReader(final Object input)
            throws XMLStreamException {
        final XMLInputFactory XMLfactory = XMLInputFactory.newInstance();
        XMLfactory.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", Boolean.TRUE);
        XMLfactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        if (input instanceof InputStream) {
            return XMLfactory.createXMLStreamReader((InputStream) input);
        } else if (input instanceof Source) {
            return XMLfactory.createXMLStreamReader((Source) input);
        } else if (input instanceof Node) {
            
            /* here we can think that we can use a DOMSource and pass it directly to the
             * method XMLfactory.createXMLStreamReader(Source) but it lead to a NPE
             * during the geometry unmarshall.
             */
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Result outputTarget = new StreamResult(outputStream);
                Transformer t = TransformerFactory.newInstance().newTransformer();
                t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                t.transform(new DOMSource((Node) input), outputTarget);
                return XMLfactory.createXMLStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));
            } catch (TransformerException ex) {
                throw new XMLStreamException(ex);
            }
        } else if (input instanceof Reader) {
            return XMLfactory.createXMLStreamReader((Reader) input);
        } else {
            throw new XMLStreamException("Input type is not supported : " + input);
        }
    }

    /**
     * <p>XML language provides two notations for boolean type :
     * "true" can be written "1" and "0" significates "false".
     * This method considers all this values as CharSequences and return its boolean value.</p>
     *
     * @param candidate The String to parse
     * @return true if bool is equal to "true" or "1".
     */
    protected static boolean parseBoolean(final String candidate) {
        if (candidate.length() == 1) {
            return !candidate.equals("0");
        }
        return Boolean.parseBoolean(candidate);
    }

    /**
     * <p>This method reads doubles with coma separated.</p>
     * 
     * @param candidate Can not be null.
     * @return parsed double value
     */
    protected static double parseDouble(final String candidate) {
        return Double.parseDouble(candidate.replace(',', '.'));
    }

    /**
     * Iterator on the reader until it reach the end of the given tag name.
     * Return the read elements as dom.
     *
     * @param tagName tag
     * @return Document read elements as a dom document
     * @throws javax.xml.stream.XMLStreamException if conversion to dom failed
     */
    protected Document readAsDom(final String tagName) throws XMLStreamException {
        
        final XMLStreamReader limitedReader = new StreamReaderDelegate(reader){
            boolean finished = false;

            @Override
            public boolean hasNext() throws XMLStreamException {
                if(finished) return false;
                return super.hasNext();
            }

            @Override
            public int next() throws XMLStreamException {
                int t = super.next();
                finished = END_ELEMENT == t && tagName.equalsIgnoreCase(reader.getLocalName());
                return t;
            }

        };

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);

        final TransformerFactory trsFactory = TransformerFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Transformer idTransform = trsFactory.newTransformer();
            final Source input = new StAXSource(limitedReader);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final Result output = new StreamResult(out);
            idTransform.transform(input, output);
            final Document doc = builder.parse(new ByteArrayInputStream(out.toByteArray()));
            return doc;
        } catch (TransformerConfigurationException e) {
            throw new XMLStreamException(e.getMessage());
        } catch (TransformerFactoryConfigurationError e) {
            throw new XMLStreamException(e.getMessage());
        } catch (IOException e) {
            throw new XMLStreamException(e.getMessage());
        } catch (TransformerException e) {
            throw new XMLStreamException(e.getMessage());
        } catch (SAXException e) {
            throw new XMLStreamException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new XMLStreamException(e.getMessage());
        }

    }

}
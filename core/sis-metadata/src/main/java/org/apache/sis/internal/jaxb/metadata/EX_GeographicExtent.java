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
package org.apache.sis.internal.jaxb.metadata;

import javax.xml.bind.annotation.XmlElementRef;
import org.opengis.metadata.extent.GeographicExtent;
import org.apache.sis.metadata.iso.extent.AbstractGeographicExtent;
import org.apache.sis.internal.jaxb.gco.PropertyType;


/**
 * JAXB adapter mapping implementing class to the GeoAPI interface. See
 * package documentation for more information about JAXB and interface.
 *
 * @author  Cédric Briançon (Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @version 0.3
 * @since   0.3
 * @module
 */
public final class EX_GeographicExtent extends PropertyType<EX_GeographicExtent, GeographicExtent> {
    /**
     * Empty constructor for JAXB only.
     */
    public EX_GeographicExtent() {
    }

    /**
     * Returns the GeoAPI interface which is bound by this adapter.
     * This method is indirectly invoked by the private constructor
     * below, so it shall not depend on the state of this object.
     *
     * @return {@code GeographicExtent.class}
     */
    @Override
    protected Class<GeographicExtent> getBoundType() {
        return GeographicExtent.class;
    }

    /**
     * Constructor for the {@link #wrap} method only.
     */
    private EX_GeographicExtent(final GeographicExtent metadata) {
        super(metadata);
    }

    /**
     * Invoked by {@link PropertyType} at marshalling time for wrapping the given metadata value
     * in a {@code <gmd:EX_GeographicExtent>} XML element.
     *
     * @param  metadata  the metadata element to marshall.
     * @return a {@code PropertyType} wrapping the given the metadata element.
     */
    @Override
    protected EX_GeographicExtent wrap(final GeographicExtent metadata) {
        return new EX_GeographicExtent(metadata);
    }

    /**
     * Invoked by JAXB at marshalling time for getting the actual metadata to write
     * inside the {@code <gmd:EX_GeographicExtent>} XML element.
     * This is the value or a copy of the value given in argument to the {@code wrap} method.
     *
     * @return the metadata to be marshalled.
     */
    @XmlElementRef
    public AbstractGeographicExtent getElement() {
        return AbstractGeographicExtent.castOrCopy(metadata);
    }

    /**
     * Invoked by JAXB at unmarshalling time for storing the result temporarily.
     *
     * @param  metadata  the unmarshalled metadata.
     */
    public void setElement(final AbstractGeographicExtent metadata) {
        this.metadata = metadata;
    }
}

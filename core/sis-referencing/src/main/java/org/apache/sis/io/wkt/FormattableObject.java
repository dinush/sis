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
package org.apache.sis.io.wkt;

import java.io.Console;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicReference;
import javax.xml.bind.annotation.XmlTransient;
import org.opengis.parameter.GeneralParameterValue;
import org.apache.sis.util.Debug;
import org.apache.sis.util.ArgumentChecks;
import org.apache.sis.internal.util.X364;


/**
 * Base class for objects that can be formatted as <cite>Well Known Text</cite> (WKT).
 * {@link WKTFormat} checks for this interface at formatting time for each element to format.
 * When a {@code FormattableObject} element is found, its {@link #formatTo(Formatter)} method
 * is invoked for allowing the element to control its formatting.
 *
 * <p>This class provides two methods for getting a default <cite>Well Known Text</cite>
 * representation of this object:</p>
 *
 * <ul>
 *   <li>{@link #toWKT()} returns a strictly compliant WKT or throws {@link UnformattableObjectException}
 *       if this object contains elements not defined by the ISO 19162 standard.</li>
 *   <li>{@link #toString()} returns a WKT with some rules relaxed in order to never throw exception,
 *       using non-standard representation if necessary. In some cases {@code toString()} may also use
 *       an alternative text representation for better readability, for example a matrix instead of
 *       a list of {@code PARAMETER["elt_…", …]} elements for linear transforms.</li>
 * </ul>
 *
 * {@section Syntax coloring}
 * A convenience {@link #print()} method is provided, which is roughly equivalent to
 * {@code System.out.println(this)} except that syntax coloring is automatically applied
 * if the terminal seems to support the ANSI escape codes.
 *
 * @author  Martin Desruisseaux (IRD, Geomatys)
 * @since   0.4 (derived from geotk-2.0)
 * @version 0.4
 * @module
 */
@XmlTransient
public class FormattableObject {
    /**
     * The formatter for the {@link #toWKT()} and {@link #toString()} methods. Formatters are not
     * thread-safe, consequently we must make sure that only one thread uses a given instance.
     *
     * {@note We do not use synchronization because the formatter will call back user's code, which
     *        introduce a risk of thread lock if the user performs his own synchronization.}
     *
     * {@note We do not use <code>ThreadLocal</code> because <code>Formatter</code> is not reentrant
     *        neither, so it may produce very confusing behavior when debugging a code that perform
     *        WKT formatting (some debuggers seem to invoke <code>toString()</code> for their own
     *        purpose in the same thread). Since <code>toString()</code> is typically invoked for
     *        debugging purpose, a single formatter for any thread is presumed sufficient.}
     *
     */
    private static final AtomicReference<Formatter> FORMATTER = new AtomicReference<>();

    /**
     * Default constructor.
     */
    protected FormattableObject() {
    }

    /**
     * Returns a strictly compliant <cite>Well Known Text</cite> (WKT) using the default convention,
     * symbols and indentation. If this object can not be represented in a standard way, then this
     * method throws an {@link UnformattableObjectException}.
     *
     * <p>By default this method formats this object according the {@link Convention#WKT2} rules.</p>
     *
     * @return The default Well Know Text representation of this object.
     * @throws UnformattableObjectException If this object can not be formatted as a standard WKT.
     *
     * @see org.opengis.referencing.IdentifiedObject#toWKT()
     */
    public String toWKT() throws UnformattableObjectException {
        return formatWKT(Convention.DEFAULT, WKTFormat.DEFAULT_INDENTATION, false, true);
    }

    /**
     * Returns a <cite>Well Known Text</cite> (WKT) or an alternative text representation for this object.
     * If this object can not be represented in a standard way, then this method fallbacks on a non-standard
     * representation.
     *
     * <p>By default this method formats this object according the {@link Convention#WKT2_SIMPLIFIED} rules.</p>
     *
     * @return The Well Known Text (WKT) or an alternative representation of this object.
     */
    @Override
    public String toString() {
        return formatWKT(Convention.DEFAULT_SIMPLIFIED, WKTFormat.DEFAULT_INDENTATION, false, false);
    }

    /**
     * Returns a <cite>Well Known Text</cite> (WKT) for this object using the specified convention.
     * The {@link Convention#INTERNAL INTERNAL} convention is a special value for debugging map projections.
     *
     * @param  convention The WKT convention to use.
     * @return The Well Known Text (WKT) or a pseudo-WKT representation of this object.
     */
    public String toString(final Convention convention) {
        ArgumentChecks.ensureNonNull("convention", convention);
        return formatWKT(convention, WKTFormat.DEFAULT_INDENTATION, false, false);
    }

    /**
     * Prints a WKT representation of this object to the {@linkplain System#out standard output stream}.
     * If a {@linkplain Console console} is attached to the running JVM (i.e. if the application is run
     * from the command-line and the output is not redirected to a file) and if Apache SIS thinks that
     * the console supports the ANSI escape codes (a.k.a. X3.64), then a syntax coloring will be applied.
     *
     * <p>This is a convenience method for debugging purpose and for console applications.</p>
     *
     * @see Colors#CONSOLE
     */
    @Debug
    public void print() {
        final Console console = System.console();
        final PrintWriter out = (console != null) ? console.writer() : null;
        final String wkt = formatWKT(Convention.DEFAULT_SIMPLIFIED, WKTFormat.DEFAULT_INDENTATION,
                (out != null) && X364.isAnsiSupported(), false);
        if (out != null) {
            out.println(wkt);
        } else {
            System.out.println(wkt);
        }
    }

    /**
     * Returns a WKT for this object using the specified convention.
     * If {@code strict} is true, then an exception is thrown if the WKT is not standard-compliant.
     *
     * @param  convention  The convention for choosing WKT element names.
     * @param  indentation The indentation to apply, or {@link WKTFormat#SINGLE_LINE}.
     * @param  colorize    {@code true} for applying syntax coloring, or {@code false} otherwise.
     * @param  strict      {@code true} if an exception shall be thrown for unformattable objects,
     *                     or {@code false} for providing a non-standard formatting instead.
     * @return The Well Known Text (WKT) or a pseudo-WKT representation of this object.
     * @throws UnformattableObjectException If {@code strict} is {@code true} and this object can not be formatted.
     */
    final String formatWKT(final Convention convention, final byte indentation, final boolean colorize, final boolean strict)
             throws UnformattableObjectException
    {
        Formatter formatter = FORMATTER.getAndSet(null);
        if (formatter == null) {
            formatter = new Formatter();
        }
        formatter.configure(convention, null, colorize ? Colors.CONSOLE : null, indentation);
        final String wkt;
        try {
            /*
             * Special processing for parameter values, which is formatted
             * directly in 'Formatter'. Note that in GeoAPI, that interface
             * doesn't share the same parent interface than other interfaces.
             */
            if (this instanceof GeneralParameterValue) {
                formatter.append((GeneralParameterValue) this);
            } else {
                formatter.append(this);
            }
            if (strict) {
                final String message = formatter.getErrorMessage();
                if (message != null) {
                    throw new UnformattableObjectException(message, formatter.errorCause);
                }
            }
            wkt = formatter.toString();
        } finally {
            formatter.clear();
        }
        FORMATTER.set(formatter);
        return wkt;
    }

    /**
     * Formats the inner part of this <cite>Well Known Text</cite> (WKT) element into the given formatter.
     * This method is automatically invoked by {@link WKTFormat} when a formattable element is found.
     *
     * <p>Keywords and authority codes shall not be formatted here.
     * For example if this formattable element is for a {@code GEOGCS} element,
     * then this method shall write the content starting at the insertion point shows below:</p>
     *
     * {@preformat text
     *     GEOGCS["WGS 84", ID["EPSG", 4326]]
     *                    ↑
     *            (insertion point)
     * }
     *
     * The default implementation declares that this object produces an invalid WKT.
     * Subclasses shall override this method for proper WKT formatting and shall <strong>not</strong>
     * invoke {@code super.formatTo(formatter)} if they can use a standard WKT syntax.
     *
     * @param  formatter The formatter where to format the inner content of this WKT element.
     * @return The WKT element keyword (e.g. {@code "GEOGCS"}).
     *
     * @see #toWKT()
     * @see #toString()
     */
    protected String formatTo(final Formatter formatter) {
        formatter.setInvalidWKT(getClass());
        String name = formatter.invalidElement;
        if (name == null) { // May happen if the user override Formatter.setInvalidWKT(Class).
            name = "UNKNOWN";
        }
        return name;
    }
}

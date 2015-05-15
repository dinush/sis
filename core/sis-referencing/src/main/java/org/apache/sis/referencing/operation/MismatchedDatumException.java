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
package org.apache.sis.referencing.operation;


/**
 * Thrown when the source and target CRS of a {@linkplain DefaultConversion conversion} use different datum.
 * By definition, conversions do not perform any change of datum
 * while {@linkplain DefaultTransformation transformations} can do.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @since   0.6
 * @version 0.6
 * @module
 */
public class MismatchedDatumException extends IllegalArgumentException {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 9209713725368948171L;

    /**
     * Constructs a new exception with no message.
     */
    public MismatchedDatumException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message, or {@code null} if none.
     */
    public MismatchedDatumException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message, or {@code null} if none.
     * @param cause The cause, or {@code null} if none.
     */
    public MismatchedDatumException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
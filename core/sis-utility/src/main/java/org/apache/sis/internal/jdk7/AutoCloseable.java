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
package org.apache.sis.internal.jdk7;


/**
 * Placeholder for the {@link java.lang.AutoCloseable} interface.
 * {@code instanceof} checks and calls to {@code ((AutoCloseable) object).close()} need to be replaced
 * by calls to {@link JDK7#isAutoCloseable(Object)} and {@link JDK7#close(Object)} respectively.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @since   0.3
 * @version 0.3
 * @module
 */
public interface AutoCloseable {
    /**
     * See {@link java.lang.AutoCloseable#close()}.
     *
     * @throws Exception If an error occurred while closing the resource.
     */
    void close() throws Exception;
}
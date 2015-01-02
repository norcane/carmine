/*
 * Carmine :: Lightweight Java remoting library using HTTP(S)
 * Copyright (c) 2013-2015 norcane
 * http://norcane.com
 * All Rights Reserved
 *
 * This software and the accompanying materials are made available under the
 * terms of the GNU Lesser General Public License (LGPL) version 3 which
 * accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 */
package com.norcane.carmine;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Represents the response sent from the remote side, encapsulating the method
 * invocation result (if successful) or exception object (if failed) and
 * properties map.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public class CarmineResponse implements Serializable {

    private final Object result;
    private final Throwable exception;
    private final Map<String, Object> properties;

    /**
     * Constructs new immutable instance representing successful invocation,
     * with given invocation result and properties map.
     *
     * @param result     result of remote method invocation
     * @param properties properties map shared within the connection
     */
    public CarmineResponse(Object result, Map<String, Object> properties) {
        this.result = result;
        this.exception = null;
        this.properties = properties;
    }

    /**
     * Constructs new immutable instance representing failed invocation, with
     * given exception thrown and properties map.
     *
     * @param exception  exception thrown during invocation (failure cause)
     * @param properties properties map shared within the connection
     */
    public CarmineResponse(Throwable exception, Map<String, Object> properties) {
        this.result = null;
        this.exception = exception;
        this.properties = properties;
    }

    /**
     * Returns the result object if invocation was successful, otherwise
     * {@code null}.
     *
     * @return result object or {@code null}
     */
    public Object getResult() {
        return result;
    }

    /**
     * Returns the exception thrown if invocation was unsuccessful, otherwise
     * {@code null}.
     *
     * @return exception thrown or {@code null}
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * Returns unmodifiable map of properties shared within the connection.
     *
     * @return map of properties
     */
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Returns {@code true} if method invocation was successful and result
     * object is available.
     *
     * @return {@code true} if method invocation was successful
     */
    public boolean isSuccessful() {
        return (result != null);
    }
}

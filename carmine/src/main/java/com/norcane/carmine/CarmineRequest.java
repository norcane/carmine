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
 * Represents the request to be sent to the remote side, encapsulating the
 * caller interface and method name, arguments and properties map.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public class CarmineRequest implements Serializable {

    private final String interfaceName;
    private final String methodName;
    private final Object[] methodArguments;
    private final Map<String, Object> properties;

    /**
     * Constructs new immutable instance with given interface name, method name,
     * method arguments and properties map.
     *
     * @param interfaceName   fully qualified name of the interface where the
     *                        method was invoked
     * @param methodName      name of the invoked method
     * @param methodArguments arguments passed to the invoked method
     * @param properties      properties map shared within the connection
     */
    public CarmineRequest(String interfaceName, String methodName,
                          Object[] methodArguments,
                          Map<String, Object> properties) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.methodArguments = methodArguments;
        this.properties = properties;
    }

    /**
     * Returns the fully qualified name of the interface where the method was
     * invoked.
     *
     * @return fully qualified name of the interface
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * Returns the name of the invoked method.
     *
     * @return name of the invoked method
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Returns the arguments passed to the invoked method.
     *
     * @return method arguments
     */
    public Object[] getMethodArguments() {
        return methodArguments;
    }

    /**
     * Returns unmodifiable map of properties shared within the connection.
     *
     * @return unmodifiable map of properties
     */
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }
}

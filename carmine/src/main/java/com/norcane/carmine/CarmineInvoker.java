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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class representing the remote side logic of the <i>Carmine</i>
 * connection, responsible for receiving remote method call requests,
 * executing the real remote method implementations and returning the result
 * (either return value or exception thrown) back to the local side. The only
 * method which must be implemented is the one whose input parameter is the
 * interface name, on which the method was executed on the client side, and
 * output is the instance of the actual implementation of this interface, on
 * which the method will be executed.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public abstract class CarmineInvoker {

    /**
     * Returns the instance of the actual implementation of the interface, on
     * which the method was invoked on the local side. On this implementation
     * instance the requested method will be executed, and the result (either
     * return value or exception thrown) will be sent back to the client side.
     *
     * @param interfaceName fully qualified name of the interface, for which the
     *                      real implementation instance must be returned
     * @param properties    properties map shared within the communication
     * @return instance of the actual implementation of the given interface
     * @throws Exception thrown when some problem occurs during the
     *                   instantiation of the interface implementation
     */
    public abstract Object getImplementation(
            String interfaceName, Map<String, Object> properties)
            throws Exception;

    /**
     * Takes input and output stream used for communication with the local
     * side, receiving the method invocation requests and sending back the
     * response with invocation result (either successful with return value or
     * unsuccessful with exception object)
     *
     * @param in  input stream used to read incoming requests
     * @param out output stream used to send responses to local side
     */
    public void processRequest(InputStream in, OutputStream out) {
        try {
            ObjectInputStream objIn = new ObjectInputStream(in);
            CarmineRequest request = (CarmineRequest) objIn.readObject();

            String interfaceName = request.getInterfaceName();
            Object[] methodArguments = request.getMethodArguments();
            Map<String, Object> properties = new HashMap<String, Object>(
                    request.getProperties());
            Object instance = null;
            CarmineResponse response = null;

            try {
                instance = getImplementation(interfaceName, properties);
            } catch (Throwable t) {
                response = new CarmineResponse(t, properties);
            }

            if (instance != null) {
                Class[] parameterTypes = null;
                if (request.getMethodArguments() != null) {
                    parameterTypes = new Class[methodArguments.length];
                    for (int i = 0; i < methodArguments.length; i++) {
                        parameterTypes[i] = methodArguments[i].getClass();
                    }
                }
                Method method = instance.getClass().getMethod(
                        request.getMethodName(), parameterTypes);

                try {
                    Object result = method.invoke(instance, methodArguments);
                    response = new CarmineResponse(result, properties);
                } catch (InvocationTargetException ex) {
                    response = new CarmineResponse(ex, properties);
                }
            }

            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(response);
            objOut.flush();
        } catch (IOException ex) {
            handleException(ex);
        } catch (ClassNotFoundException ex) {
            handleException(ex);
        } catch (NoSuchMethodException ex) {
            handleException(ex);
        } catch (IllegalAccessException ex) {
            handleException(ex);
        }
    }

    private void handleException(Exception ex) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                "An exception thrown during Carmine remote invocation", ex);
    }
}

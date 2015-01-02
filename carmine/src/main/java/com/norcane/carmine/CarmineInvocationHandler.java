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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Implementation of the {@link java.lang.reflect.InvocationHandler},
 * transparently sending all the method invocations on the local proxy class
 * to the remote side, and returning the result of remote method execution.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public class CarmineInvocationHandler implements InvocationHandler {

    private final CarmineManager cm;

    /**
     * Constructs new instance with given
     * {@link com.norcane.carmine.CarmineManager}.
     *
     * @param cm Carmine manager instance
     */
    public CarmineInvocationHandler(CarmineManager cm) {
        this.cm = cm;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // create the request object
        CarmineRequest request = new CarmineRequest(
                method.getDeclaringClass().getName(), method.getName(), args,
                new HashMap<String, Object>(cm.getProperties()));

        // setup connection to remote side
        URLConnection connection = cm.getRemoteUrl().openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // send the request object to the remote side
        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
        out.writeObject(request);
        out.close();

        // read the response object from the remote side
        ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        CarmineResponse response = (CarmineResponse) in.readObject();
        cm.setProperties(new HashMap<String, Object>(response.getProperties()));
        in.close();

        // if an exception occurred on the remote side, unwrap and rethrow
        if (!response.isSuccessful()) throw response.getException();

        return response.getResult();
    }
}

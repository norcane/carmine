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
package com.norcane.carmine.demo.server;

import com.norcane.carmine.CarmineInvoker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Represents example servlet, serving as <i>Carmine</i> remote service
 * endpoint, processing incoming method execution requests and passing back
 * responses with result object.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
@WebServlet(name = "CarmineServlet", urlPatterns = {"/CarmineServlet"})
public class CarmineServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // creates the instance of the remote invoker, processing requests
        CarmineInvoker invoker = new CarmineInvoker() {
            @Override
            public Object getImplementation(
                    String interfaceName, Map<String, Object> properties)
                    throws Exception {

                /*
                 * Represents very simple implementation of this method, trying
                 * to instantiate an implementation class for the passed
                 * interface name using the interface name + "Impl" postfix.
                 *
                 * Example:
                 *      com.norcane.carmine.demo.RemoteTest
                 *   -> com.norcane.carmine.demo.RemoteTestImpl
                 *
                 * In real-world scenario, some security checks should be
                 * performed before returning the instance back (e.g. whether
                 * the connected local side is allowed to access this class, or
                 * implement some kind of authorization using properties map).
                 */
                String className = interfaceName + "Impl";
                Class theClass = Class.forName(className);
                return theClass.newInstance();
            }
        };

        // connect input and output stream to the Carmine invoker
        invoker.processRequest(
                request.getInputStream(), response.getOutputStream());
    }
}

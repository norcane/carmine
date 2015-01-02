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
package com.norcane.carmine.demo.client;

import com.norcane.carmine.CarmineManager;
import com.norcane.carmine.demo.RemoteTest;

import java.net.URL;
import java.util.Scanner;

/**
 * A local side application example demonstrating usage of <i>Carmine</i>
 * library to create proxy instance of remote class and invoke method, whose
 * real implementation is on the remote side.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public class Main {

    private static final String REMOTE_URL
            = "http://localhost:8080/carmine-demo-server/CarmineServlet";

    public static void main(String[] args) throws Exception {
        // creates connection to remote side and obtains the proxy instance
        CarmineManager cm = new CarmineManager(new URL(REMOTE_URL));
        RemoteTest remoteTest = cm.getRemote(RemoteTest.class);

        System.out.print("Type your name: ");
        String name = new Scanner(System.in).nextLine();

        // invokes the method 'getGreetings()' which is actually implemented on
        // the server side, and returns its return value
        String remoteResult = remoteTest.getGreetings(name);
        System.out.println(remoteResult);
    }
}

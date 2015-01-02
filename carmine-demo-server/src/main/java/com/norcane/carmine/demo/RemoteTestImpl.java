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
package com.norcane.carmine.demo;

/**
 * An example implementation of the {@link com.norcane.carmine.demo.RemoteTest}
 * interface. This implementation is placed only on the remote side, whereas the
 * interface is used on the local side to create instance of the proxy class.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public class RemoteTestImpl implements RemoteTest {
    @Override
    public String getGreetings(String forName) {
        return "Greetings from the remote server, "
                + (forName.isEmpty() ? "unknown" : forName);
    }
}

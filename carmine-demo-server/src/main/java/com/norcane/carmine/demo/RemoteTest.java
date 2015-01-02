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
 * An example interface of the remote class, whose implementation is actually
 * on the remote side and all method invocations are performed there, using
 * <i>Carmine</i> library to handle the connection.
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public interface RemoteTest {

    /**
     * Returns the short greeting message for the user name specified by the
     * given argument. The real implementation of this method logic is on the
     * remote side.
     *
     * @param forName name for which to create the greeting message
     * @return greeting message
     */
    public String getGreetings(String forName);
}

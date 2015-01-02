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

import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates the connection to the remote <i>Carmine</i> service running on the
 * server specified by the given URL and provides method to obtain the instance
 * of the remote class.
 * <p/>
 * <dl>
 * <dt><b>Example of usage:</b></dt>
 * <dd><pre><code>
 * CarmineManager cm = new CarmineManager(REMOTE_SERVICE_URL);
 * RemoteClass remoteClass = cm.getRemote(RemoteClass.class);
 * remote.remoteMethod();
 * </code></pre></dd>
 * </dl>
 *
 * @author Vaclav Svejcar (v.svejcar@norcane.cz)
 */
public class CarmineManager {

    private final URL remoteUrl;
    private Map<String, Object> properties;

    /**
     * Constructs new instance with the given remote service URL.
     *
     * @param remoteUrl remote service URL
     */
    public CarmineManager(URL remoteUrl) {
        this.remoteUrl = remoteUrl;
        this.properties = new HashMap<String, Object>();
    }

    /**
     * Returns the proxy instance of the remote service, specified by its
     * interface class. Every method call invoked on this proxy object is
     * transparently transformed into request sent to the remote side, where the
     * real implementation of the method is executed, and the result value
     * (if any) is transferred back and returned by this proxy object method
     * call. For the end user, the only difference between using common local
     * instances and <i>Carmine</i>-managed instances is in the way how the new
     * instance is created (i.e. {@code new} keyword vs
     * {@link #getRemote(Class)}.
     *
     * @param theInterface the interface of the remote service
     * @param <T>          return type of the returned object (not required to
     *                     specify explicitly)
     * @return proxy instance for the remote service, specified by the given
     * service interface
     */
    public <T> T getRemote(Class theInterface) {
        CarmineInvocationHandler invocationHandler
                = new CarmineInvocationHandler(this);

        @SuppressWarnings("unchecked")
        T proxy = (T) Proxy.newProxyInstance(theInterface.getClassLoader(),
                new Class[]{theInterface}, invocationHandler);
        return proxy;
    }

    /**
     * Returns the URL of the remote service.
     *
     * @return remote service URL
     */
    public URL getRemoteUrl() {
        return remoteUrl;
    }

    /**
     * Returns the unmodifiable map of properties shared within the connection
     * (for example to pass additional authentication key to the remote server side).
     *
     * @return unmodifiable map of properties
     */
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Set the map of properties shared within the connection.
     *
     * @param properties map of properties
     */
    protected void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Returns the property specified by its key.
     *
     * @param key property key
     * @return property value
     */
    public Object getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Adds new property, identified by its key, with the given value.
     *
     * @param key   property key
     * @param value property value
     */
    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    /**
     * Removes the property, identified by its key.
     *
     * @param key property key
     */
    public void removeProperty(String key) {
        properties.remove(key);
    }
}

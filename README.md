# carmine
This library provides a lightweight alternative to
[RMI](http://en.wikipedia.org/wiki/Java_remote_method_invocation) _(Remote
Method Invocation)_ or [Spring HTTP Invoker]
(http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/remoting/httpinvoker/package-summary.html),
using _HTTP(S)_ protocol for communication between the client and server side.

## Key features
* _Java-to-Java_ remote method invocation is done using _HTTP(S)_ protocol
* working with remote objects on the client side differs only in instantiation
  process (`new` keyword VS `CarmineManager#getRemote()`)
* very simple implementation of the server side (e.g. _Java EE Servlet_)
* exceptions thrown by the remote method implementation are transparently
  rethrown on the client side
* no dependencies on external libraries - just one small _JAR_ file
* _Java SE 6+_ compatible

## Overview
_Carmine_ has been designed for as much simple _Java-to-Java_ remote method
invocation as possible, not only for the ease of usage (invocation of remote
method is exactly the same as local one, except the object instantiation), but
also for the ease of implementing the server side, responsible for method
implementation invocation.

The only two things the developer has to do to make things work are:

1. Implement the `CarmineInvoker#getImplementation()` method, which takes
   interface name and returns the actual implementation for it.
2. Connect the `CarmineInvoker` instance to the input/output stream to handle
   client requests and responses (e.g. `HttpServletRequest#getInputStream()`
   and `HttpServletResponse#getOutputStream()` when using _Java EE Servlet_).

## Basic example - client side
Note that similar code can be found in the _Main_ class, placed in the
_carmine-demo-client_ demo project.

```java
URL remoteServerURL = null;

try {
  remoteServerURL = new URL("http://localhost:8080/carmine-demo-server/CarmineServlet");
} catch (MalformedURLException ex) {
   // handle the exception
}

CarmineManager cm = new CarmineManager(remoteServerURL);

/*
 * RemoteTest.class is the interface class. The real implementation
 * of this interface is on the server and all the methods are called
 * remotely.
 */
RemoteTest remoteTest = cm.getRemote(RemoteTest.class);

// this is the remotely called method, taking String as an argument
String greetings = remoteTest.getGreetings("Arthur");

// prints 'Greetings from the remote server, Arthur' to console
System.out.println(greetings);
```

## Basic example - server side
Note that this is the code snippet from the _CarmineServlet_ Java EE Servlet,
which can be found in the _carmine-demo-server_ demo project.

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    // create the instance of the carmine remote invoker
    CarmineInvoker invoker = new CarmineInvoker() {

        @Override
        @SuppressWarnings("unchecked")
        public Object getImplementation(
                 String interfaceName,
                 Map<String, Object> properties) throws Exception {
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

    invoker.processRequest(request.getInputStream(), response.getOutputStream());
}
```

## Using connection parameters
Sometimes some additional data are required to be shared between the client
and server side within the _Carmine_ connection (e.g. credentials, security
token, etc). To allow this, _Carmine_ offers mechanism of
_connection properties_, represented by the `Map<String, Object>`
object. Any property set either on client or server side will be available to
both of them, during every remote method invocation, until the property is
removed from the map or connection is closed.

To access properties from the _client side_, simply use `CarmineManager`
instance:

```java
CarmineManager cm = new CarmineManager(remoteServerUrl);
cm.addProperty("foo", "bar");       // to add property
cm.removeProperty("foo")     // to remove property
Map<String, Object> properties = cm.getProperties();    // readonly map of properties
```

To access the properties on the server side, simply use the parameter passed to
the `CarmineInvoker#getImplementation()` method.

## Requirements
* to avoid possible serialization problems, the same major version of _JVM_
  should be used on both server and client side (Java SE 6 or newer)
* all objects returned as the result of remote method invocation must be
  serializable
* all exception objects thrown during remote method invocation must be
  serializable (allows to transparently rethrow the exception on the client)
* all connection properties values must be serializable
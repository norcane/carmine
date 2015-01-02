<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<!--

Carmine .:. Lightweight Java remoting library using HTTP(S)
Copyright (c) 2013-2015 norcane
All Rights Reserved

This software and the accompanying materials are made available under the
terms of the GNU Lesser General Public License (LGPL) version 3 which
accompanies this distribution, and is available at
http://www.gnu.org/licenses/lgpl-3.0.html

This software is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
License for more details.

-->
<html>
<head>
    <meta charset="utf-8"/>
    <title>Carmine Server demo</title>

    <style>
        html {
            background: #e8e8e8;
            font-family: Verdana, Arial, sans-serif;
            font-size: 10pt;
        }

        footer {
            font-size: 9pt;
            text-align: center;
        }

        #context-container {
            background: #ffffff;
            border-radius: 5px;
            box-shadow: 0 0 5px 5px #b5b5b5;
            width: 800px;
            margin: 100px auto 20px auto;
            padding: 10px;
        }

        .code {
            background: #e8e8e8;
            font-family: monospace;
            padding: 2px 10px 2px 10px;
        }
    </style>
</head>
<body>
<div id="context-container">
    <h1>Carmine Server demo</h1>

    <p>
        Congratulations, your <em>Carmine</em> demo server is now running.
        Now, try to connect with the <em>Carmine</em> Client demo application
        using this server URL:
    </p>

    <div class="code">
        ${pageContext.request.secure == true ? "https://" : "http://"}${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/CarmineServlet
    </div>
    <p>
        (NOTE that the example above expects you haven't changed the default
        path to the <code>CarmineServlet</code> servlet.)
    </p>

    <h2>See also:</h2>
    <ul>
        <li><a href="http://github.com/norcane/carmine">github.com/norcane/carmine</a>
            - carmine <em>Github</em> page
        </li>
    </ul>
</div>
<footer>
    Copyright (c) 2013-2015 norcane
</footer>
</body>
</html>
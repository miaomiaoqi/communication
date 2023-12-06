package com.miaoqi.webserver.processor;

import com.miaoqi.webserver.connector.ConnectorUtils;
import com.miaoqi.webserver.connector.Request;
import com.miaoqi.webserver.connector.RequestFacade;
import com.miaoqi.webserver.connector.Response;
import com.miaoqi.webserver.connector.ResponseFacade;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ServletProcessor {

    public URLClassLoader getServletLoader() throws MalformedURLException {
        File servletRoot = new File(ConnectorUtils.SERVLET_ROOT);
        URL servletRootUrl = servletRoot.toURI().toURL();
        return new URLClassLoader(new URL[]{servletRootUrl});
    }

    public Servlet getServlet(URLClassLoader loader, Request request) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        /* /servlet/TimeServlet */
        String uri = request.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        // 加载 Servlet
        Class<?> servletClass = Class.forName("com.miaoqi.webserver.servlet.TimeServlet");
        Servlet servlet = (Servlet) servletClass.newInstance();
        return servlet;
    }

    public void process(Request request, Response response) throws IOException {
        try {
            URLClassLoader loader = this.getServletLoader();
            Servlet servlet = this.getServlet(loader, request);
            servlet.service(new RequestFacade(request), new ResponseFacade(response));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

}

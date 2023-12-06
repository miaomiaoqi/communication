package com.miaoqi.webserver.connector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class RequestFacade implements ServletRequest {

    private ServletRequest request = null;

    public RequestFacade(Request request) {
        this.request = request;
    }

    @Override
    public Object getAttribute(String attribute) {
        return this.request.getAttribute(attribute);
    }

    @Override
    public Enumeration getAttributeNames() {
        return this.request.getAttributeNames();
    }

    @Override
    public String getRealPath(String path) {
        return this.request.getRealPath(path);
    }

    @Override
    public int getRemotePort() {
        return this.request.getRemotePort();
    }

    @Override
    public String getLocalName() {
        return this.request.getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return this.request.getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return this.request.getLocalPort();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return this.request.getRequestDispatcher(path);
    }

    @Override
    public boolean isSecure() {
        return this.request.isSecure();
    }

    @Override
    public String getCharacterEncoding() {
        return this.request.getCharacterEncoding();
    }

    @Override
    public int getContentLength() {
        return this.request.getContentLength();
    }

    @Override
    public String getContentType() {
        return this.request.getContentType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.request.getInputStream();
    }

    @Override
    public Locale getLocale() {
        return this.request.getLocale();
    }

    @Override
    public Enumeration getLocales() {
        return this.request.getLocales();
    }

    @Override
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public Map getParameterMap() {
        return this.request.getParameterMap();
    }

    @Override
    public Enumeration getParameterNames() {
        return this.request.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String parameter) {
        return this.request.getParameterValues(parameter);
    }

    @Override
    public String getProtocol() {
        return this.request.getProtocol();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return this.request.getReader();
    }

    @Override
    public String getRemoteAddr() {
        return this.request.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return this.request.getRemoteHost();
    }

    @Override
    public String getScheme() {
        return this.request.getScheme();
    }

    @Override
    public String getServerName() {
        return this.request.getServerName();
    }

    @Override
    public int getServerPort() {
        return this.request.getServerPort();
    }

    @Override
    public void removeAttribute(String attribute) {
        this.request.removeAttribute(attribute);
    }

    @Override
    public void setAttribute(String key, Object value) {
        this.request.setAttribute(key, value);
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        this.request.setCharacterEncoding(encoding);
    }

}
package com.miaoqi.webserver.connector;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class ResponseFacade implements ServletResponse {

    private ServletResponse response;

    public ResponseFacade(Response response) {
        this.response = response;
    }

    @Override
    public void flushBuffer() throws IOException {
        this.response.flushBuffer();
    }

    @Override
    public int getBufferSize() {
        return this.response.getBufferSize();
    }

    @Override
    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return this.response.getContentType();
    }

    @Override
    public Locale getLocale() {
        return this.response.getLocale();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public boolean isCommitted() {
        return this.response.isCommitted();
    }

    @Override
    public void reset() {
        this.response.reset();
    }

    @Override
    public void resetBuffer() {
        this.response.resetBuffer();
    }

    @Override
    public void setBufferSize(int size) {
        this.response.setBufferSize(size);
    }

    @Override
    public void setContentLength(int length) {
        this.response.setContentLength(length);
    }

    @Override
    public void setContentType(String type) {
        this.response.setContentType(type);
    }

    @Override
    public void setLocale(Locale locale) {
        this.response.setLocale(locale);
    }

}
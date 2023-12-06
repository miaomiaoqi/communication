import com.miaoqi.webserver.connector.Request;
import com.miaoqi.webserver.processor.ServletProcessor;
import org.junit.Test;

import javax.servlet.Servlet;
import java.net.MalformedURLException;
import java.net.URLClassLoader;

public class ProcessorTest {

    private static final String servletRequest = "GET /servlet/TimeServlet HTTP/1.1";

    @Test
    public void givenServletRequestThenLoadServlet() throws MalformedURLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Request request = TestUtils.createRequest(servletRequest);
        ServletProcessor processor = new ServletProcessor();
        URLClassLoader loader = processor.getServletLoader();
        Servlet servlet = processor.getServlet(loader, request);
        System.out.println(servlet);
    }

}

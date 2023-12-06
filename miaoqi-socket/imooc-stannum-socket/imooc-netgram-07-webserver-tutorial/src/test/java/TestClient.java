import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        OutputStream output = socket.getOutputStream();
        output.write("GET /servlet/index.html HTTP/1.1".getBytes());
        socket.shutdownOutput();
        InputStream input = socket.getInputStream();
        byte[] buffer = new byte[2048];
        int length = input.read(buffer);
        String response = new String(buffer, 0, length);
        System.out.println(response);
        socket.shutdownInput();

        socket.close();
    }

}

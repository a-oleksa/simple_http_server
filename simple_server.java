import java.io.*;
import java.util.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

public class simple_server
{
	// todo: catch exception instead of throwing
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
		server.createContext("/", new GetHandler());
		server.setExecutor(null);
		server.start();
	}

	public static void writeResponse(HttpExchange t, String response) throws IOException {
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	static class GetHandler implements HttpHandler {
		// todo: catch exception instead of throwing
		public void handle(HttpExchange t) throws IOException {
			String reqURI = "./public_html" + t.getRequestURI();
			File ff = new File(reqURI);

			if( ff.exists())
			{
				byte[] bytearray = new byte [(int)ff.length()]; 
				FileInputStream fis = new FileInputStream(ff);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(bytearray, 0, bytearray.length);

				t.sendResponseHeaders(200, ff.length());
				OutputStream os = t.getResponseBody();
				os.write(bytearray, 0, bytearray.length);
				os.close();
			} else {
				StringBuilder response = new StringBuilder();
				response.append("<html><title>404 - File Not Found</title>");
				response.append("<body>File Not Found!</body>");
				response.append("</html>");

				t.sendResponseHeaders(404, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.toString().getBytes());
				os.close();
				
			}
				
		}
	}
}

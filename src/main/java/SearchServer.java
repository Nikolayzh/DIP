import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.List;

public class SearchServer {

    static class Request {

        public String word;

        public Request(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return "Request { " +
                    "word = '" + word + '\'' +
                    " } ";
        }
    }

    private final int port;
    private final BooleanSearchEngine engine;

    public SearchServer(int port) throws IOException {
        this.port = port;
        engine = new BooleanSearchEngine(new File("pdfs"));
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("\nStarting server at " + this.port + "... \nServer started...\n");
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    System.out.println("New connection accepted!");
                    System.out.println("Client address: " + clientSocket.getInetAddress() + " , port: " + clientSocket.getPort());
                    String json = in.readLine();
                    Request r = new Gson().fromJson(json, Request.class);

                    if (r.word != null && !r.word.isEmpty()) {
                        List<PageEntry> result = this.engine.search(r.word);
                        System.out.println(listToJson(result));
                        out.println(listToJson(result));
                    }
                    System.out.println("Client message: " + json);
                    out.println("Hello!");
                }
            }
        } catch (IOException e) {
            System.out.println("Can't start server!");
            e.printStackTrace();
        }
    }

    public static <T> Object listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(list, listType);
    }
}

import java.io.File;
import java.net.ServerSocket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        // Uncomment bottom strings for testing Search Engine

        /*BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));
        System.out.println(engine.search("DevOps"));*/

        // Here create server, who's response to requests
        // Server will listen on port 8989
        // and answer /{word} -> returning value of search(word) in JSON-format

        SearchServer searchServer = new SearchServer(8989);
        searchServer.start();
    }
}
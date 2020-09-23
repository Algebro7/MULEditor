import core.Client;
import core.SearchResult;

import java.util.List;

public class MULEditor {
    public static void main(String[] args) {
        Client client = new Client();
        List<SearchResult> results = null;
        try {
            results = client.searchMUL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert results != null;
    }
}

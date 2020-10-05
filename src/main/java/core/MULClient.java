package core;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import picocli.CommandLine;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MULClient {
    private final String MUL_HOST = "masterunitlist.info";
    private final String MUL_PROTOCOL = "http";
    private final String MUL_SEARCH_PATH = "/Unit/Filter";
    private CloseableHttpClient httpClient;

    public MULClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        this.httpClient = httpClient;
    }

    public List<MulUnit> searchMUL() throws Exception {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(MUL_PROTOCOL)
                .setHost(MUL_HOST)
                .setPath(MUL_SEARCH_PATH)
                .addParameters(getMULSearchParameters());
        URI uri = uriBuilder.build();
        List<MulUnit> results;
        HttpGet request = new HttpGet(uri);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            results = parseMULTables(html);
            EntityUtils.consume(entity);
        }
        return results;
    }

    private List<NameValuePair> getMULSearchParameters() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("HasBV", "true"));
        params.add(new BasicNameValuePair("Types", "18"));
        params.add(new BasicNameValuePair("Types", "19"));
        params.add(new BasicNameValuePair("Types", "20"));
        params.add(new BasicNameValuePair("Types", "23"));
        params.add(new BasicNameValuePair("Types", "24"));
        params.add(new BasicNameValuePair("Types", "22"));
        params.add(new BasicNameValuePair("Types", "79"));
        params.add(new BasicNameValuePair("Types", "76"));
        return params;
    }

    private List<MulUnit> parseMULTables(String html) throws Exception {
        List<MulUnit> results = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        for (int i = 0; i < 7; i++) {
            Element resultsTable = doc.select("table").get(i);
            Elements rows = resultsTable.select("tr");
            for (int j = 1; j < rows.size(); j++) { // skip header row
                Element node = rows.get(j);
                String name = node.select("td").get(0).text();
                String BV = node.select("td").get(3).text().replace(",", "");
                String PV = node.select("td").get(4).text().replace(",", "");
                String rules = node.select("td").get(6).text();
                String relPath = node.select("td").get(0).select("a").first().attr("href");
                String intro = node.select("td").get(8).text();
                results.add(new MulUnit(name, BV, PV, rules, MUL_PROTOCOL + "://" + MUL_HOST + relPath, intro));
            }
        }
        return results;
    }
}

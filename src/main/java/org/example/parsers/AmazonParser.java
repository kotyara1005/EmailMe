package org.example.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class AmazonParser implements Parser {
    HttpClient client;

    public AmazonParser(HttpClient client) {
        this.client = client;
    }
    public ArrayList<ParsingEntry> parse(ZonedDateTime start_from) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://developer.amazon.com/blogs/appstore/feed/entries/atom"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Document doc = Jsoup.parse(response.body());
        Elements entries = doc.getElementsByTag("entry");

        ArrayList<ParsingEntry> result = new ArrayList<>();

        for (Element el:entries) {
            ParsingEntry am = new ParsingEntry();
            am.title = el.getElementsByTag("title").text().trim();
            am.link = "https://developer.amazon.com" + el.getElementsByTag("link").attr("href").trim();
            am.published = ZonedDateTime.parse(el.getElementsByTag("published").text().trim());
            if (am.published.isAfter(start_from)) {
                result.add(am);
            }
        }
        return result;
    }
}

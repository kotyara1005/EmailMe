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
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class RedditParser implements Parser {
    HttpClient client;
    public RedditParser(HttpClient client) {
        this.client = client;
    }
    @Override
    public ArrayList<ParsingEntry> parse(ZonedDateTime start_from) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.redditinc.com/blog/topic/technology"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(response.body());

        Elements entries = doc.getElementsByTag("article");
//        System.out.println(entries.size());
        ArrayList<ParsingEntry> result = new ArrayList<>();

        for (Element el:entries) {
            ParsingEntry am = new ParsingEntry();
            am.title = el.getElementsByTag("h2").text().trim();
            am.link = el.getElementsByTag("a").attr("href").trim();

            String dt = el.getElementsByClass("entry-date published").attr("datetime").trim();

            if (dt.equals("")) continue;

            am.published = ZonedDateTime.parse(dt);
//            System.out.println(am);
            if (am.published.isAfter(start_from)) {
                result.add(am);
            }
        }

        return result;
    }
}

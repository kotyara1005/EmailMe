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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SoundCloudParser implements Parser {
    public HttpClient client;

    public SoundCloudParser(HttpClient client) {
        this.client = client;
    }
    @Override
    public ArrayList<ParsingEntry> parse(ZonedDateTime start_from) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://developers.soundcloud.com/blog/blog.rss"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(response.body());

        Elements entries = doc.getElementsByTag("item");
        System.out.println(entries.size());
        ArrayList<ParsingEntry> result = new ArrayList<>();

        for (Element el:entries) {
            ParsingEntry am = new ParsingEntry();
            am.title = el.getElementsByTag("title").text().trim().replace("<![CDATA[", "").replace("]]>", "");
            am.link = el.getElementsByTag("guid").text().trim();
            am.published = ZonedDateTime.parse(el.getElementsByTag("pubDate").text().trim(), DateTimeFormatter.RFC_1123_DATE_TIME);
//            System.out.println(am);
//            System.out.println(el);
            if (am.published.isAfter(start_from)) {
                result.add(am);
            }
        }
        return result;
    }
}

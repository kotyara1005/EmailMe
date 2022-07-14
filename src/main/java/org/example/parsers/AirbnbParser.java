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

public class AirbnbParser implements Parser {
    @Override
    public ArrayList<ParsingEntry> parse() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://medium.com/airbnb-engineering/airbnb-engineering-infrastructure/home"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(response.body());

        Elements entries = doc.getElementsByAttribute("data-index");
        System.out.println(entries.size());
        ZonedDateTime start_from = ZonedDateTime.now().minus(Duration.ofDays(7));
        ArrayList<ParsingEntry> result = new ArrayList<>();

        for (Element el:entries) {
            ParsingEntry am = new ParsingEntry();
            String index = el.attr("data-index");
            if (index.equals("0")) {
                el = el.nextElementSibling();
            }

            am.title = el.getElementsByTag("a").text().trim();
            am.link = el.getElementsByTag("a").attr("href").trim();
            am.published = ZonedDateTime.parse(el.getElementsByTag("time").attr("datetime").trim());
            if (am.published.isAfter(start_from)) {
                result.add(am);
            }
        }
        return result;
    }
}

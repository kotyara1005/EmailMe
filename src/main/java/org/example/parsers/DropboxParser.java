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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DropboxParser implements Parser {
    @Override
    public ArrayList<ParsingEntry> parse(ZonedDateTime start_from) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://dropbox.tech/application"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(response.body());
//        System.out.println(response.body());

        Elements entries = doc.getElementsByTag("li");
        System.out.println(entries.size());

        ArrayList<ParsingEntry> result = new ArrayList<>();

        for (Element el:entries) {
            ParsingEntry am = new ParsingEntry();
            am.title = el.getElementsByAttributeValue("data-element-id", "article-title").text().trim();
            am.link = el.getElementsByTag("a").attr("href").trim();

            String dt = el.getElementsByAttributeValue("data-element-id", "article-date").text().trim();

            if (dt.equals("")) continue;

            am.published = LocalDate.parse(dt, DateTimeFormatter.ofPattern("LLL dd, yyyy")).atStartOfDay(ZoneId.of("UTC"));
            if (am.published.isAfter(start_from)) {
                result.add(am);
            }
        }

        return result;
    }
}

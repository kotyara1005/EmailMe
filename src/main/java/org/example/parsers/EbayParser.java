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

public class EbayParser implements Parser {

    @Override
    public ArrayList<ParsingEntry> parse(ZonedDateTime start_from) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://tech.ebayinc.com/engineering/"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(response.body());
//        System.out.println(response.body());

        Elements entries = doc.getElementsByAttribute("data-id");
        ArrayList<ParsingEntry> result = new ArrayList<>();

        for (Element el:entries) {
            ParsingEntry am = new ParsingEntry();
            am.title = el.getElementsByTag("h3").text().trim();
            am.link = "https://tech.ebayinc.com/" + el.getElementsByTag("a").attr("href").trim();

            String dt = el.getElementsByTag("time").attr("datetime").trim();

            if (dt.equals("")) continue;

            am.published = LocalDate.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.of("UTC"));
//            System.out.println(am);
            if (am.published.isAfter(start_from)) {
                result.add(am);
            }
        }

        return result;
    }
}

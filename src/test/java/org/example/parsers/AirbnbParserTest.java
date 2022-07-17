package org.example.parsers;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class AirbnbParserTest {

    @Test
    void parse() throws IOException, InterruptedException {
        HttpClient client = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(Files.readString(Path.of("test_data/airbnb")));
        when(client.send(any(), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);
        System.out.println(client);
        AirbnbParser p = new AirbnbParser(client);

        ZonedDateTime dt = ZonedDateTime.of(2022, 7, 17, 0,0,0,0,Clock.systemUTC().getZone());
        ArrayList<ParsingEntry> expected = new ArrayList<>();
        expected.add(new ParsingEntry("Unified Payments Data Read at Airbnb How we redesigned payments data read flow to optimize client integrations, while achieving up to 150x performance gains.  Alican GÖKSEL", "https://medium.com/airbnb-engineering/unified-payments-data-read-at-airbnb-e613e7af1a39?source=collection_category---4------0-----------------------", ZonedDateTime.parse("2022-06-09T22:23:30.734Z")));

        ArrayList<ParsingEntry> result = p.parse(dt.minus(Duration.ofDays(40)));
        assertIterableEquals(expected, result);
    }
}

package org.example.parsers;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.Arrays;
import java.util.Collection;


public class ParsersTest {
    HttpClient client;

    @BeforeEach
    void setUpClient() {
        client = mock(HttpClient.class);
    }

    @TestFactory
    Collection<DynamicTest> testParsers() {
        Collection<DynamicTest> tests = new ArrayList<>();
        ArrayList<ParsingEntry> e1 = new ArrayList<>();
        e1.add(new ParsingEntry(
                "PR Templates for Effective Pull Requests",
                "https://developers.soundcloud.com/blog/pr-templates-for-effective-pull-requests",
                ZonedDateTime.parse("2022-07-12T00:00Z")
        ));
        tests.add(
                DynamicTest.dynamicTest(
                        "SoundCloudParser",
                        () -> testParse(
                                new SoundCloudParser(client),
                                e1,
                                ZonedDateTime.parse("2022-07-11T00:00-05:00"),
                                Path.of("test_data/soundcould")
                        )
                )
        );

        ArrayList<ParsingEntry> e2 = new ArrayList<>();
        e2.add(new ParsingEntry(
                "Unified Payments Data Read at Airbnb How we redesigned payments data read flow to optimize client integrations, while achieving up to 150x performance gains.  Alican GÖKSEL",
                "https://medium.com/airbnb-engineering/unified-payments-data-read-at-airbnb-e613e7af1a39?source=collection_category---4------0-----------------------",
                ZonedDateTime.parse("2022-06-09T22:23:30.734Z")
        ));

        tests.add(
                DynamicTest.dynamicTest(
                        "AirbnbParser",
                        () -> testParse(
                                new AirbnbParser(client),
                                e2,
                                ZonedDateTime.parse("2022-06-08T22:23:30.734Z"),
                                Path.of("test_data/airbnb")
                        )
                )
        );

        ArrayList<ParsingEntry> e3 = new ArrayList<>();
        e3.add(new ParsingEntry(
                "Scaling Reporting at Reddit",
                "https://www.redditinc.com/blog/topic/technology",
                ZonedDateTime.parse("2021-02-26T00:00-05:00")
        ));

        tests.add(
                DynamicTest.dynamicTest(
                        "RedditParser",
                        () -> testParse(
                                new RedditParser(client),
                                e3,
                                ZonedDateTime.parse("2021-02-20T00:00-05:00"),
                                Path.of("test_data/reddit")
                        )
                )
        );
        return tests;
    }

    void testParse(Parser p, ArrayList<ParsingEntry> expected, ZonedDateTime start_from, Path path)
            throws IOException, InterruptedException {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(Files.readString(path));
        when(client.send(any(), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);

        ArrayList<ParsingEntry> result = p.parse(start_from);
        assertIterableEquals(expected, result);
    }
}

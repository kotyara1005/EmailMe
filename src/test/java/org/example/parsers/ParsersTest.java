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

        ArrayList<ParsingEntry> e4 = new ArrayList<>();
        e4.add(new ParsingEntry(
                "客户现在可以在Windows 11上预览亚马逊应用商店（发布时仅在美国提供）",
                "https://developer.amazon.com/blogs/appstore/post/a1c4daaa-fd6d-4943-959f-3b84e35f6b9c/cn-customers-can-now-preview-the-amazon-appstore-on-windows-11",
                ZonedDateTime.parse("2022-03-21T22:13:29Z")
        ));

        tests.add(
                DynamicTest.dynamicTest(
                        "AmazonParser",
                        () -> testParse(
                                new AmazonParser(client),
                                e4,
                                ZonedDateTime.parse("2022-03-21T22:10:29Z"),
                                Path.of("test_data/amazon")
                        )
                )
        );

        ArrayList<ParsingEntry> e5 = new ArrayList<>();
        e5.add(new ParsingEntry("Fighting the forces of clock skew when syncing password payloads", "https://dropbox.tech/application/dropbox-passwords-clock-skew-payload-sync-merge", ZonedDateTime.parse("2022-05-17T00:00Z[UTC]")));
        e5.add(new ParsingEntry("Fighting the forces of clock skew when syncing password payloads", "https://dropbox.tech/application/dropbox-passwords-clock-skew-payload-sync-merge", ZonedDateTime.parse("2022-05-17T00:00Z[UTC]")));

        tests.add(
                DynamicTest.dynamicTest(
                        "DropboxParser",
                        () -> testParse(
                                new DropboxParser(client),
                                e5,
                                ZonedDateTime.parse("2022-05-16T00:00Z[UTC]"),
                                Path.of("test_data/dropbox")
                        )
                )
        );

        ArrayList<ParsingEntry> e6 = new ArrayList<>();
        e6.add(new ParsingEntry(
                "eBay's Notification Streaming Platform: How eBay Handles Real-Time Push Notifications at Scale",
                "https://tech.ebayinc.com//engineering/ebays-notification-streaming-platform-how-ebay-handles-real-time-push-notifications-at-scale/",
                ZonedDateTime.parse("2022-07-13T00:00Z[UTC]")));

        tests.add(
                DynamicTest.dynamicTest(
                        "EbayParser",
                        () -> testParse(
                                new EbayParser(client),
                                e6,
                                ZonedDateTime.parse("2022-07-12T00:00Z[UTC]"),
                                Path.of("test_data/ebay")
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
        System.out.println(result);
        assertIterableEquals(expected, result);
    }
}

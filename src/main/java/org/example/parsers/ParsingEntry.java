package org.example.parsers;

import java.time.ZonedDateTime;

public class ParsingEntry {
    public String title;
    public String link;
    public ZonedDateTime published;

    public ParsingEntry() {
    }

    public ParsingEntry(String title, String link, ZonedDateTime published) {
        this.title = title;
        this.link = link;
        this.published = published;
    }

    public String toString() {
        return String.format("title %s link %s published %s", title, link, published);
    }
}

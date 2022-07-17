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
        return String.format("ParsingEntry(%s, %s, %s)", title, link, published);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ParsingEntry that) {
            return (this.title.equals(that.title) && this.link.equals(that.link) && this.published.equals(that.published));
        }
        return false;
    }
}

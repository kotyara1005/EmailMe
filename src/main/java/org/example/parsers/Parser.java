package org.example.parsers;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public interface Parser {
    ArrayList<ParsingEntry> parse(ZonedDateTime start_from) throws IOException, InterruptedException;
}

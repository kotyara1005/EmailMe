package org.example.parsers;
import java.io.IOException;
import java.util.ArrayList;

public interface Parser {
    public ArrayList<ParsingEntry> parse() throws IOException, InterruptedException;
}

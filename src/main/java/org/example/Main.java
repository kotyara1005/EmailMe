package org.example;

import org.example.parsers.DropboxParser;
import org.example.parsers.EbayParser;
import org.example.parsers.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Hello world!");
        Parser p = new EbayParser();
        System.out.println(p.parse().size());

    }
}
package org.example;

import org.example.parsers.SoundCloudParser;
import org.example.parsers.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Hello world!");
        Parser p = new SoundCloudParser();
        System.out.println(p.parse().size());

    }
}
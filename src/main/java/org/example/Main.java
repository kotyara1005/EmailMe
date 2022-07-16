package org.example;

import java.util.ArrayList;
import java.util.List;
import org.example.parsers.*;
import org.apache.commons.mail.*;
import java.io.IOException;


class Emailer {
    private final String password;
    Emailer(String password) {
        this.password = password;
    }

    public void sendEmail(String msg) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krivonos.artm@gmail.com", password));
        email.setSSLOnConnect(true);
        email.setFrom("krivonos.artm@gmail.com");
        email.setSubject("TestMail");
        email.setMsg(msg);
        email.addTo("krivonos.artm@gmail.com");
        email.send();
    }
}

class App {
    Emailer emailer;
    List<Parser> parsers;
    App(Emailer emailer, List<Parser> parsers) {
        this.emailer = emailer;
        this.parsers = parsers;
    }

    public void run() throws IOException, InterruptedException, EmailException {
        ArrayList<ParsingEntry> results = new ArrayList<>();

        for (Parser parser: parsers) {
            results.addAll(parser.parse());
        }

        StringBuilder b = new StringBuilder();
        for (ParsingEntry pe: results) {
            b.append(String.format("%s(%s)\n%s\n\n", pe.title, pe.published.toLocalDate(), pe.link));
        }
        System.out.println(b);
        emailer.sendEmail(b.toString());
    }
}

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, EmailException {
        String password = System.getenv("EMAIL_PASSWORD");
        if (password.equals("")) {
            return;
        }

        List<Parser> parsers = new ArrayList<>();
        parsers.add(new AirbnbParser());
        parsers.add(new AmazonParser());
        parsers.add(new DropboxParser());
        parsers.add(new EbayParser());
        parsers.add(new RedditParser());
        parsers.add(new SoundCloudParser());

        new App(new Emailer(password), parsers).run();
    }
}
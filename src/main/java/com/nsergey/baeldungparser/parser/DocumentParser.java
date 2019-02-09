package com.nsergey.baeldungparser.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentParser {

    /**
     * https://jsoup.org/apidocs/org/jsoup/select/Selector.html
     */
    public void parseDocument(Document doc) {
        System.out.println("Header:");
        Element headerH1 = doc.select("header h1")
                              .first();
        System.out.println(String.format("\"%s\"\n", headerH1.text()));

        Elements article = doc.select("section[itemprop=\"articleBody\"]");
        article.forEach(System.out::println);
    }

}

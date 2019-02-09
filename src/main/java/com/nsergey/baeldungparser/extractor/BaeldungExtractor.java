package com.nsergey.baeldungparser.extractor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BaeldungExtractor {

    public Document loadDocument(String url) {
        try {
            return Jsoup.connect(url)
                        .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

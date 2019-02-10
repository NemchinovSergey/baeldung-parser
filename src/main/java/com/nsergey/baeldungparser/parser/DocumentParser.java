package com.nsergey.baeldungparser.parser;

import com.nsergey.baeldungparser.render.Render;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DocumentParser {

    private final Render render;

    @Autowired
    public DocumentParser(Render render) {
        this.render = render;
    }

    /**
     * https://jsoup.org/apidocs/org/jsoup/select/Selector.html
     */
    public void parse(Document doc) {
        System.out.println("Header:");
        Element headerH1 = doc.select("header h1")
                              .first();
        System.out.println(String.format("\"%s\"\n", headerH1.text()));

        Elements article = doc.select("section[itemprop=\"articleBody\"]");
        assert article.size() == 1;

        List<String> list = article.first()
                                      .children()
                                      .stream()
                                      .peek(element -> log.info("HTML: {}", element.outerHtml()))
                                      .map(render::render)
                                      .peek(markdown -> log.info("Markdown: {}", markdown))
                                      .collect(Collectors.toList());
        //list.forEach(log::info);
    }

}

package com.nsergey.baeldungparser.parser;

import com.nsergey.baeldungparser.render.Render;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<String> parse(Document doc) {
        List<String> list = new ArrayList<>();

        Element headerH1 = doc.select("header h1")
                              .first();
        list.add(render.render(headerH1));

        Elements article = doc.select("section[itemprop=\"articleBody\"]");
        list.addAll(article.first()
                           .children()
                           .stream()
                           .map(render::render)
                           .collect(Collectors.toList()));
        return list;
    }

}

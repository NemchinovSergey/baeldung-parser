package com.nsergey.baeldungparser.render;

import com.vladsch.flexmark.convert.html.FlexmarkHtmlParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

@Slf4j
public class MarkdownRender implements Render {

    @Override
    public String render(Element element) {
        log.debug("HTML: {}", element.outerHtml());
        String md = FlexmarkHtmlParser.parse(element.outerHtml());
        log.debug("Markdown: {}", md);
        return md;
    }

}

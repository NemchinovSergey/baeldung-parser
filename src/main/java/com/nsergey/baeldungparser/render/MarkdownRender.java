package com.nsergey.baeldungparser.render;

import com.vladsch.flexmark.convert.html.FlexmarkHtmlParser;
import org.jsoup.nodes.Element;

public class MarkdownRender implements Render {

    @Override
    public String render(Element element) {
        return FlexmarkHtmlParser.parse(element.outerHtml());
    }

}

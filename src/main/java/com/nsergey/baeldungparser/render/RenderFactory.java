package com.nsergey.baeldungparser.render;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RenderFactory {

    private static final String MARKDOWN = "MARKDOWN";
    private static final String WORDPRESS = "WORDPRESS";

    private RenderFactory() {
    }

    public static Render getInstance(@NonNull String renderName) {
        switch (renderName.toUpperCase()) {
            case MARKDOWN:
                return new MarkdownRender();
            case WORDPRESS:
                return new WordPressRender();
            default:
                throw new IllegalArgumentException("Not supported render type: " + renderName);
        }
    }
}

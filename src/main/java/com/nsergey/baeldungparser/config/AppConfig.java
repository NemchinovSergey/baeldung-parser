package com.nsergey.baeldungparser.config;

import com.nsergey.baeldungparser.render.Render;
import com.nsergey.baeldungparser.render.RenderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Render getRender(@Value("${render.name}") String renderName) {
        return RenderFactory.getInstance(renderName);
    }

}

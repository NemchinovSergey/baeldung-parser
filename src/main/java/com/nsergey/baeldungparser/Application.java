package com.nsergey.baeldungparser;

import com.nsergey.baeldungparser.extractor.BaeldungExtractor;
import com.nsergey.baeldungparser.parser.DocumentParser;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void start(ApplicationReadyEvent event) {
        ConfigurableApplicationContext ctx = event.getApplicationContext();

        BaeldungExtractor extractor = ctx.getBean(BaeldungExtractor.class);
        DocumentParser parser = ctx.getBean(DocumentParser.class);

        Document document = extractor.loadDocument("https://www.baeldung.com/java-with-jsoup");
        if (document != null) {
            parser.parse(document);
        }
    }

}


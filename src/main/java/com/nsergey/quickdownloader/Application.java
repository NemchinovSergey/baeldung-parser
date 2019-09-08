package com.nsergey.quickdownloader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Application {

    protected static List<String> DOWNLOADABLE = Arrays.asList("zip", "mp3", "avi", "pdf", "mov", "m4v", "mp4");

    private static DownloadFileHttpClient httpClient = new DownloadFileHttpClient();

	public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            showUsageHelp();
            return;
        }

        String startingPage = args[0];
        Path dir = Paths.get(args[1]);
        createDirectory(dir);

        Document document = loadPageDocument(startingPage);
        if (document != null) {
            Elements body = getBody(document);
            saveElementsToFile(dir.resolve("index.html").toString(), body);

            Elements links = getLinks(body);
            int successNumber = 1;
            for (Element e : links) {
                int count = processPage(e.text(), e.attr("href"), dir, successNumber);
                if (count > 0) {
                    successNumber++;
                }
            }
        }
	}

    protected static int processPage(String title, String pageUrl, Path dir, int number) {
        System.out.println(String.format("Start Page: [%s] -> %s", title, pageUrl));
        int count = 0;
        try {
            Path pageDir = dir.resolve(buildPageDirectoryName(title, number));

            Document page = loadPageDocument(pageUrl);
            Elements links = page.select("a");
            for (Element link : links) {
                String href = link.attr("href");
                if (processLink(pageDir, resolveLink(pageUrl, href))) {
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception while page loading: " + e.getMessage());
        } finally {
            System.out.println(String.format("Finished page: [%s]", title));
        }
        return count;
    }

    protected static URI resolveLink(String pageUrl, String link) {
        try {
            URI fileUri = new URI(link);
            if (fileUri.isAbsolute()) {
                return fileUri;
            } else {
                URI pageUri = new URI(pageUrl);
                return pageUri.resolve(fileUri);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static boolean processLink(Path pageDir, URI fileUri) {
        System.out.println("Process link: " + fileUri.toString());
        try {
            String fileNameOnly = extractFileNameOnly(fileUri);
            if (isUrlDownloadable(fileNameOnly)) {
                createDirectory(pageDir);
                Path saveToFileName = pageDir.resolve(fileNameOnly);
                long bytes = httpClient.download(fileUri.toString(), saveToFileName.toString());
                if (bytes > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception while link processing: " + e.getMessage());
        }
        return false;
    }

    protected static String extractFileNameOnly(URI fileUri) throws UnsupportedEncodingException {
        String decodedUrl = URLDecoder.decode(fileUri.toString(), "UTF-8");
        String[] strings = decodedUrl.split("/");
        if (strings.length > 0) {
            return strings[strings.length - 1];
        }
        return "";
    }

    protected static boolean isUrlDownloadable(String fileNameOnly) {
        System.out.println("URL: " + fileNameOnly);
        String extension = getFileNameExtension(fileNameOnly).orElse(null);
        System.out.println("File ext: " + extension);
        if (extension != null && !"".equals(extension)) {
            return DOWNLOADABLE.contains(extension.toLowerCase());
        }
        return false;
    }

    private static Optional<String> getFileNameExtension(String filename) {
        return Optional.ofNullable(filename)
                       .filter(f -> f.contains("."))
                       .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private static void showUsageHelp() {
        System.out.println("Usage: java com.nsergey.quickdownloader.Application url directory");
        System.out.println("Exit...");
    }

    private static Document loadPageDocument(String startingPage) throws IOException {
        return Jsoup.connect(startingPage).get();
    }

    private static String buildPageDirectoryName(String title, int number) {
        return String.format("%d. %s", number, filterDirectoryName(title));
    }

    private static String filterDirectoryName(String title) {
        return title.replace('/', '-');
    }

    private static void createDirectory(Path dir) throws IOException {
        if (!(Files.exists(dir) && Files.isDirectory(dir))) {
            Files.createDirectories(dir);
        }
    }

    private static void saveElementsToFile(String fileName, Elements body) {
        List<String> strings = body.first()
                                      .children()
                                      .stream()
                                      .map(Node::outerHtml)
                                      .collect(Collectors.toList());

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName),
                                                             StandardOpenOption.CREATE)) {
            for (String s : strings) {
                writer.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Elements getBody(Document document) {
        return document.select("div .post-body");
    }

    private static Elements getLinks(Elements body) {
        return body.select("a");
    }


}


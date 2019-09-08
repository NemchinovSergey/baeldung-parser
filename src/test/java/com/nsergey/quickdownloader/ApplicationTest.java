package com.nsergey.quickdownloader;

import static com.nsergey.quickdownloader.Application.isUrlDownloadable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

public class ApplicationTest {

    private static final String PAGE = "https://somesite.com/path1/path2/page.php";
    private static final String RELATIVE_FILE_URL = "../download.php?u=..%2Flessons%2Fbonus%2Fvalues-questions%2FLessonVideo.mov";
    private static final String RESOLVED_FILE_URL = "https://somesite.com/path1/download.php?u=..%2Flessons%2Fbonus%2Fvalues-questions%2FLessonVideo.mov";
    private static final String ABSOLUTE_FILE_URL = "http://somesite.com/podcast/ValuesQuestionsVIP.zip";

    @Ignore
    @Test
    public void processLink() {
        int count = Application.processPage("A page title",
                                            "http://somesite.com/path/page.php",
                                            Paths.get("U:\\Education\\CourseDirectory\\CourseName"),
                                            62);
        assertTrue(count > 0);
    }

    @Test
    public void test_isUrlDownloadable_WhenTrue() {
        assertTrue(isUrlDownloadable("file.Zip"));
        assertTrue(isUrlDownloadable("file.MP3"));
        assertTrue(isUrlDownloadable("file.MP4"));
        assertTrue(isUrlDownloadable("file.PDF"));
        assertTrue(isUrlDownloadable("file.m4v"));
        assertTrue(isUrlDownloadable("file.avi"));
    }

    @Test
    public void test_isUrlDownloadable_WhenFalse() {
        assertFalse(isUrlDownloadable("file.php"));
        assertFalse(isUrlDownloadable("file.html"));
        assertFalse(isUrlDownloadable("file.htm"));
        assertFalse(isUrlDownloadable("file.js"));
    }

    @Test
    public void test_resolveLink_WhenLinkIsRelative()
            throws URISyntaxException, UnsupportedEncodingException {
        URI uri = Application.resolveLink(PAGE, RELATIVE_FILE_URL);
        assertNotNull(uri);

        System.out.println(URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8.name()));
        assertEquals(uri, new URI(RESOLVED_FILE_URL));
    }

    @Test
    public void test_resolveLink_WhenLinkIsAbsolute() throws URISyntaxException {
        URI uri = Application.resolveLink(PAGE, ABSOLUTE_FILE_URL);
        System.out.println(uri);
        assertEquals(uri, new URI(ABSOLUTE_FILE_URL));
    }

    @Test
    public void test_getFileNameOnly_AbsoluteUrl() throws URISyntaxException, UnsupportedEncodingException {
        assertEquals("ValuesQuestionsVIP.zip", Application.extractFileNameOnly(new URI(ABSOLUTE_FILE_URL)));
        assertEquals("LessonVideo.mov", Application.extractFileNameOnly(new URI(RESOLVED_FILE_URL)));
    }

    @Test
    public void test_getFileNameOnly_RelativeUrl() throws URISyntaxException, UnsupportedEncodingException {
        assertEquals("LessonVideo.mov", Application.extractFileNameOnly(new URI(RELATIVE_FILE_URL)));
    }
}
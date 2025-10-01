package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests in addition to the general tests from {@link org.jabref.logic.formatter.FormatterTest}
 */
class HtmlToLatexFormatterTest {

    private HtmlToLatexFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new HtmlToLatexFormatter();
    }

    @Test
    void formatWithoutHtmlCharactersReturnsSameString() {
        assertEquals("abc", formatter.format("abc"));
    }

    @Test
    void formatIeeeHtml() {
        assertEquals("Towards situation-aware adaptive workflows: SitOPT --- A general purpose situation-aware workflow management system", formatter.format("Towards situation-aware adaptive workflows: SitOPT &amp;#x2014; A general purpose situation-aware workflow management system"));
    }

    @Test
    void formatMultipleHtmlCharacters() {
        assertEquals("{{\\aa}}{\\\"{a}}{\\\"{o}}", formatter.format("&aring;&auml;&ouml;"));
    }

    @Test
    void formatCombinedAccent() {
        assertEquals("{\\'{\\i}}", formatter.format("i&#x301;"));
    }

    @Test
    void basic() {
        assertEquals("aaa", formatter.format("aaa"));
    }

    @ParameterizedTest
    @CsvSource({"{\\\"{a}}, &auml;", "{\\\"{a}}, &#228;", "{\\\"{a}}, &#xe4;", "{{$\\Epsilon$}}, &Epsilon;"})
    void html(String expected, String text) {
        assertEquals(expected, formatter.format(text));
    }

    @Test
    void htmlRemoveTags() {
        assertEquals("aaa", formatter.format("<b>aaa</b>"));
    }

    @ParameterizedTest
    @CsvSource({"{\\\"{a}}, a&#776;", "{\\\"{a}}, a&#x308;", "{\\\"{a}}b, a&#776;b", "{\\\"{a}}b, a&#x308;b"})
    void htmlCombiningAccents(String expected, String text) {
        assertEquals(expected, formatter.format(text));
    }

    @Test
    void keepsSingleLessThan() {
        String text = "(p < 0.01)";
        assertEquals(text, formatter.format(text));
    }

    @Test
    void formatExample() {
        assertEquals("JabRef", formatter.format(formatter.getExampleInput()));
    }
}

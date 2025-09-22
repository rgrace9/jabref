package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests in addition to the general tests from {@link org.jabref.logic.formatter.FormatterTest}
 */
class OrdinalsToSuperscriptFormatterTest {

    private OrdinalsToSuperscriptFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new OrdinalsToSuperscriptFormatter();
    }

    @ParameterizedTest
    @CsvSource({
            "1st, 1\\textsuperscript{st}",
            "2nd, 2\\textsuperscript{nd}",
            "3rd, 3\\textsuperscript{rd}",
            "4th, 4\\textsuperscript{th}",
            "21th, 21\\textsuperscript{th}"
    })
    void replacesSuperscript(String input, String expected) {
        expectCorrect(input, expected);
    }

    @ParameterizedTest
    @CsvSource({
            "1st, 1\\textsuperscript{st}",
            "1ST, 1\\textsuperscript{ST}",
            "1sT, 1\\textsuperscript{sT}"
    })
    void replaceSuperscriptsIgnoresCase(String input, String expected) {
        expectCorrect(input, expected);
    }

    @Test
    void replaceSuperscriptsInMultilineStrings() {
        expectCorrect(
                "replace on 1st line\nand on 2nd line.",
                "replace on 1\\textsuperscript{st} line\nand on 2\\textsuperscript{nd} line."
        );
    }

    @Test
    void replaceAllSuperscripts() {
        expectCorrect(
                "1st 2nd 3rd 4th",
                "1\\textsuperscript{st} 2\\textsuperscript{nd} 3\\textsuperscript{rd} 4\\textsuperscript{th}"
        );
    }

    @Test
    void ignoreSuperscriptsInsideWords() {
        expectCorrect("1st 1stword words1st inside1stwords", "1\\textsuperscript{st} 1stword words1st inside1stwords");
    }

    @Test
    void formatExample() {
        assertEquals("11\\textsuperscript{th}", formatter.format(formatter.getExampleInput()));
    }

    private void expectCorrect(String input, String expected) {
        assertEquals(expected, formatter.format(input));
    }
}

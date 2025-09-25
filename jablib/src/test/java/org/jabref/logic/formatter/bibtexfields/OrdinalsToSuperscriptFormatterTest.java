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
            "1\\textsuperscript{st}, 1st",
            "2\\textsuperscript{nd}, 2nd",
            "3\\textsuperscript{rd}, 3rd,",
            "4\\textsuperscript{th}, 4th",
            "21\\textsuperscript{th}, 21th"
    })
    void replacesSuperscript(String expected, String input) {
        expectCorrect(expected, input);
    }

    @ParameterizedTest
    @CsvSource({
            "1\\textsuperscript{st}, 1st",
            "1\\textsuperscript{ST}, 1ST",
            "1\\textsuperscript{sT}, 1sT"
    })
    void replaceSuperscriptsIgnoresCase(String expected, String input) {
        expectCorrect(expected, input);
    }

    @Test
    void replaceSuperscriptsInMultilineStrings() {
        expectCorrect(
                "replace on 1\\textsuperscript{st} line\nand on 2\\textsuperscript{nd} line.", "replace on 1st line\nand on 2nd line."
        );
    }

    @Test
    void replaceAllSuperscripts() {
        expectCorrect(
                "1\\textsuperscript{st} 2\\textsuperscript{nd} 3\\textsuperscript{rd} 4\\textsuperscript{th}", "1st 2nd 3rd 4th"
        );
    }

    @Test
    void ignoreSuperscriptsInsideWords() {
        expectCorrect("1\\textsuperscript{st} 1stword words1st inside1stwords", "1st 1stword words1st inside1stwords");
    }

    @Test
    void formatExample() {
        assertEquals("11\\textsuperscript{th}", formatter.format(formatter.getExampleInput()));
    }

    private void expectCorrect(String expected, String input) {
        assertEquals(expected, formatter.format(input));
    }
}

package org.jabref.logic.bst.util;

import java.util.stream.Stream;

import org.jabref.model.entry.AuthorList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BstNameFormatterTest {

    @Test
    void umlautsFullNames() {
        AuthorList list = AuthorList.parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");

        assertEquals("de~laVall{\\'e}e~PoussinCharles Louis Xavier~Joseph",
                BstNameFormatter.formatName(list.getAuthor(0), "{vv}{ll}{jj}{ff}"));
    }

    @Test
    void umlautsAbbreviations() {
        AuthorList list = AuthorList.parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");

        assertEquals("de~la Vall{\\'e}e~Poussin, C.~L. X.~J.",
                BstNameFormatter.formatName(list.getAuthor(0), "{vv~}{ll}{, jj}{, f.}"));
    }

    @Test
    void umlautsAbbreviationsWithQuestionMark() {
        AuthorList list = AuthorList.parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");

        assertEquals("de~la Vall{\\'e}e~Poussin, C.~L. X.~J?",
                BstNameFormatter.formatName(list.getAuthor(0), "{vv~}{ll}{, jj}{, f}?"));
    }

    @Test
    void formatName() {
        AuthorList list = AuthorList.parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");
        assertEquals("dlVP", BstNameFormatter.formatName(list.getAuthor(0), "{v{}}{l{}}"));
    }

    @ParameterizedTest
    @CsvSource({
            "Jonathan Meyer, Jonathan Meyer and Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin",
            "{\\'{E}}douard Masterly, {\\'{E}}douard Masterly",
            "Ulrich {\\\"{U}}nderwood, Ulrich {\\\"{U}}nderwood and Ned {\\~N}et and Paul {\\={P}}ot",
            "Paul~{\\'E}mile Victor, Paul {\\'E}mile Victor and and de la Cierva y Codorn{\\’\\i}u, Juan"})
    void formatNameC(String string, String string2) {
        assertEquals(string, BstNameFormatter.formatName(string2, 1, "{ff }{vv }{ll}{ jj}"));
    }

    @ParameterizedTest
    @CsvSource({
            "J.~Meyer, Jonathan Meyer and Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin",
            "{\\'{E}}.~Masterly, {\\'{E}}douard Masterly",
            "U.~{\\\"{U}}nderwood, Ulrich {\\\"{U}}nderwood and Ned {\\~N}et and Paul {\\={P}}ot",
            "P.~{\\'E}. Victor, Paul {\\'E}mile Victor and and de la Cierva y Codorn{\\’\\i}u, Juan"
    })
    void formatNameB(String string, String string2) {
        assertEquals(string, BstNameFormatter.formatName(string2, 1, "{f.~}{vv~}{ll}{, jj}"));
    }

    static Stream<Arguments> provideNames() {
        return Stream.of(
                Arguments.of("Meyer, J?", "Jonathan Meyer and Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin"),
                Arguments.of("Masterly, {\\'{E}}?", "{\\'{E}}douard Masterly"),
                Arguments.of("{\\\"{U}}nderwood, U?", "Ulrich {\\\"{U}}nderwood and Ned {\\~N}et and Paul {\\={P}}ot"),
                Arguments.of("Victor, P.~{\\'E}?", "Paul {\\'E}mile Victor and and de la Cierva y Codorn{\\’\\i}u, Juan")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNames")
    void formatNameA(String string, String string2) {
        assertEquals(string, BstNameFormatter.formatName(string2, 1, "{vv~}{ll}{, jj}{, f}?"));
    }

    @Test
    void matchingBraceConsumedForCompleteWords() {
        StringBuilder sb = new StringBuilder();
        assertEquals(6, BstNameFormatter.consumeToMatchingBrace(sb, "{HELLO} {WORLD}".toCharArray(), 0));
        assertEquals("{HELLO}", sb.toString());
    }

    @Test
    void matchingBraceConsumedForBracesInWords() {
        StringBuilder sb = new StringBuilder();
        assertEquals(18, BstNameFormatter.consumeToMatchingBrace(sb, "{HE{L{}L}O} {WORLD}".toCharArray(), 12));
        assertEquals("{WORLD}", sb.toString());
    }

    @Test
    void consumeToMatchingBrace() {
        StringBuilder sb = new StringBuilder();
        assertEquals(10, BstNameFormatter.consumeToMatchingBrace(sb, "{HE{L{}L}O} {WORLD}".toCharArray(), 0));
        assertEquals("{HE{L{}L}O}", sb.toString());
    }

    @ParameterizedTest
    @CsvSource({"C, Charles", "V, Vall{\\'e}e", "{\\'e}, {\\'e}", "{\\'e, {\\'e", "E, {E"})
    void getFirstCharOfString(String expected, String s) {
        assertEquals(expected, BstNameFormatter.getFirstCharOfString(s));
    }

    @ParameterizedTest
    @CsvSource({"6, Vall{\\'e}e, -1",
            "2, Vall{\\'e}e, 2",
            "1, Vall{\\'e}e, 1",
            "6, Vall{\\'e}e, 6",
            "6, Vall{\\'e}e, 7",
            "8, Vall{e}e, -1",
            "6, Vall{\\'e this will be skipped}e, -1"
    })
    void numberOfChars(int expected, String token, int inStop) {
        assertEquals(expected, BstNameFormatter.numberOfChars(token, inStop));
    }
}

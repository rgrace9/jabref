package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests in addition to the general tests from {@link org.jabref.logic.formatter.FormatterTest}
 */
class NormalizeNamesFormatterTest {

    private NormalizeNamesFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new NormalizeNamesFormatter();
    }

    @ParameterizedTest
    @CsvSource({
            "{Society of Automotive Engineers}, {Society of Automotive Engineers}",
            "'{Company Name, LLC}', '{Company Name, LLC}'",
            "'Bilbo, Staci D.', Staci D Bilbo",
            "'Bilbo, Staci D.', Staci D. Bilbo",
            "'Bilbo, Staci D. and Smith, S. H. and Schwarz, Jaclyn M.', Staci D Bilbo and Smith SH and Jaclyn M Schwarz",
            "'Ølver, M. A.', Ølver MA",
            "'Ølver, M. A. and Øie, G. G. and Øie, G. G. and Alfredsen, J. Å. Å. and Alfredsen, Jo and Olsen, Y. Y. and Olsen, Y. Y.', Ølver MA; GG Øie; Øie GG; Alfredsen JÅÅ; Jo Alfredsen; Olsen Y.Y. and Olsen YY.",
            "'Ølver, M. A. and Øie, G. G. and Øie, G. G. and Alfredsen, J. Å. Å. and Alfredsen, Jo and Olsen, Y. Y. and Olsen, Y. Y.', Ølver MA; GG Øie; Øie GG; Alfredsen JÅÅ; Jo Alfredsen; Olsen Y.Y.; Olsen YY.",
            "'Alver, Morten and Alver, Morten O. and Alfredsen, J. A. and Olsen, Y. Y.', 'Alver, Morten and Alver, Morten O and Alfredsen, JA and Olsen, Y.Y.'",
            "'Alver, M. A. and Alfredsen, J. A. and Olsen, Y. Y.', 'Alver, MA; Alfredsen, JA; Olsen Y.Y.'",
            "'Kolb, Stefan and Lenhard, J{\\\"o}rg and Wirtz, Guido', 'Kolb, Stefan and J{\\\"o}rg Lenhard and Wirtz, Guido'"
    })
    void normalizeAuthorList(String expected, String nameList) {
        assertEquals(expected, formatter.format(nameList));
    }

    @Test
    void twoAuthorsSeperatedByColon() {
        assertEquals("Bilbo, Staci and Alver, Morten", formatter.format("Staci Bilbo; Morten Alver"));
    }

    @Test
    void threeAuthorsSeperatedByColon() {
        assertEquals("Bilbo, Staci and Alver, Morten and Name, Test", formatter.format("Staci Bilbo; Morten Alver; Test Name"));
    }

    // Test for https://github.com/JabRef/jabref/issues/318
    @Test
    void threeAuthorsSeperatedByAnd() {
        assertEquals("Kolb, Stefan and Lenhard, J{\\\"o}rg and Wirtz, Guido", formatter.format("Stefan Kolb and J{\\\"o}rg Lenhard and Guido Wirtz"));
    }

    // Test for https://github.com/JabRef/jabref/issues/318
    @Test
    void threeAuthorsSeperatedByAndWithDash() {
        assertEquals("Jian, Heng-Yu and Xu, Z. and Chang, M.-C. F.", formatter.format("Heng-Yu Jian and Xu, Z. and Chang, M.-C.F."));
    }

    // Test for https://github.com/JabRef/jabref/issues/318
    @Test
    void threeAuthorsSeperatedByAndWithLatex() {
        assertEquals("Gustafsson, Oscar and DeBrunner, Linda S. and DeBrunner, Victor and Johansson, H{\\aa}kan",
                formatter.format("Oscar Gustafsson and Linda S. DeBrunner and Victor DeBrunner and H{\\aa}kan Johansson"));
    }

    @Test
    void lastThenInitial() {
        assertEquals("Smith, S.", formatter.format("Smith S"));
    }

    @Test
    void lastThenInitials() {
        assertEquals("Smith, S. H.", formatter.format("Smith SH"));
    }

    @Test
    void initialThenLast() {
        assertEquals("Smith, S.", formatter.format("S Smith"));
    }

    @Test
    void initialDotThenLast() {
        assertEquals("Smith, S.", formatter.format("S. Smith"));
    }

    @Test
    void initialsThenLast() {
        assertEquals("Smith, S. H.", formatter.format("SH Smith"));
    }

    @Test
    void lastThenJuniorThenFirst() {
        assertEquals("Name, della, first", formatter.format("Name, della, first"));
    }

    @ParameterizedTest
    @CsvSource({
            "'Ali Babar, M. and Dingsøyr, T. and Lago, P. and van der Vliet, H.', 'Ali Babar, M., Dingsøyr, T., Lago, P., van der Vliet, H.'",
            "'Ali Babar, M.', 'Ali Babar, M.'"
    })
    void concatenationOfAuthorsWithCommas(String expected, String nameList) {
        assertEquals(expected, formatter.format(nameList));
    }

    @Test
    void oddCountOfCommas() {
        assertEquals("Ali Babar, M., Dingsøyr T. Lago P.", formatter.format("Ali Babar, M., Dingsøyr, T., Lago P."));
    }

    @Test
    void formatExample() {
        assertEquals("Einstein, Albert and Turing, Alan", formatter.format(formatter.getExampleInput()));
    }

    @Test
    void nameAffixe() {
        assertEquals("Surname, jr, First and Surname2, First2", formatter.format("Surname, jr, First, Surname2, First2"));
    }

    @Test
    void avoidSpecialCharacter() {
        assertEquals("Surname, {, First; Surname2, First2", formatter.format("Surname, {, First; Surname2, First2"));
    }

    @Test
    void andInName() {
        assertEquals("Surname and , First, Surname2 First2", formatter.format("Surname, and , First, Surname2, First2"));
    }

    @Test
    void multipleNameAffixes() {
        assertEquals("Mair, Jr, Daniel and Brühl, Sr, Daniel", formatter.format("Mair, Jr, Daniel, Brühl, Sr, Daniel"));
    }

    @Test
    void commaSeperatedNames() {
        assertEquals("Bosoi, Cristina and Oliveira, Mariana and Sanchez, Rafael Ochoa and Tremblay, Mélanie and TenHave, Gabrie and Deutz, Nicoolas and Rose, Christopher F. and Bemeur, Chantal",
                formatter.format("Cristina Bosoi, Mariana Oliveira, Rafael Ochoa Sanchez, Mélanie Tremblay, Gabrie TenHave, Nicoolas Deutz, Christopher F. Rose, Chantal Bemeur"));
    }

    @Test
    void multipleSpaces() {
        assertEquals("Bosoi, Cristina and Oliveira, Mariana and Sanchez, Rafael Ochoa and Tremblay, Mélanie and TenHave, Gabrie and Deutz, Nicoolas and Rose, Christopher F. and Bemeur, Chantal",
                formatter.format("Cristina    Bosoi,    Mariana Oliveira, Rafael Ochoa Sanchez   ,   Mélanie Tremblay  , Gabrie TenHave, Nicoolas Deutz, Christopher F. Rose, Chantal Bemeur"));
    }

    @Test
    void avoidPreposition() {
        assertEquals("von Zimmer, Hans and van Oberbergern, Michael and zu Berger, Kevin", formatter.format("Hans von Zimmer, Michael van Oberbergern, Kevin zu Berger"));
    }

    @Test
    void preposition() {
        assertEquals("von Zimmer, Hans and van Oberbergern, Michael and zu Berger, Kevin", formatter.format("Hans von Zimmer, Michael van Oberbergern, Kevin zu Berger"));
    }

    @Test
    void oneCommaUntouched() {
        assertEquals("Canon der Barbar, Alexander der Große", formatter.format("Canon der Barbar, Alexander der Große"));
    }

    @Test
    void avoidNameAffixes() {
        assertEquals("der Barbar, Canon and der Große, Alexander and der Alexander, Peter", formatter.format("Canon der Barbar, Alexander der Große, Peter der Alexander"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Canon der Barbar AND Alexander der Große",
            "Canon der Barbar aNd Alexander der Große",
            "Canon der Barbar AnD Alexander der Große"
    })
    void upperCaseSensitiveList(String nameList) {
        assertEquals("der Barbar, Canon and der Große, Alexander", formatter.format(nameList));
    }

    @ParameterizedTest
    @CsvSource({
            "'Last, First and Last2, First2 and Last3, First3', 'Last, First; Last2, First2; Last3, First3'",
            "'Last, Jr, First and Last2, First2', 'Last, Jr, First; Last2, First2'",
            "'Last, First and Last2, First2 and Last3, First3 and Last4, First4', 'Last, First and Last2, First2 and Last3, First3 and Last4, First4'",
            "'Last and Last2, First2 and Last3, First3 and Last4, First4', 'Last; Last2, First2; Last3, First3; Last4, First4'"
    })
    void semiCorrectNamesWithSemicolon(String expected, String nameList) {
        assertEquals(expected, formatter.format(nameList));
    }
}

package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LatexCleanupFormatterTest {

    private LatexCleanupFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new LatexCleanupFormatter();
    }

    @ParameterizedTest
    @CsvSource({
            "$\\alpha\\beta$, $\\alpha$$\\beta$",
            "{VLSI DSP}, {VLSI} {DSP}",
            "\\textbf{VLSI} {DSP}, \\textbf{VLSI} {DSP}",
            "A ${\\Delta\\Sigma}$ modulator for {FPGA DSP}, A ${\\Delta}$${\\Sigma}$ modulator for {FPGA} {DSP}"
    })
    void test(String expected, String oldString) {
        assertEquals(expected, formatter.format(oldString));
    }

    @Test
    void preservePercentSign() {
        assertEquals("\\%", formatter.format("%"));
    }

    @Test
    void escapePercentSignOnlyOnce() {
        assertEquals("\\%", formatter.format("\\%"));
    }

    @Test
    void escapePercentSignOnlnyOnceWithNumber() {
        assertEquals("50\\%", formatter.format("50\\%"));
    }

    @Test
    void formatExample() {
        assertEquals("{VLSI DSP}", formatter.format(formatter.getExampleInput()));
    }
}

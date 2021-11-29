package assignment.utils.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHelpersTest {

    @Test
    void wrapMultiLineTest() {

        String s = "this string is exactly 70 characters long and fits in 5 lines, *nice*!";

        String line = StringHelpers.wrapMultiLine(s, 20, "\n");

        assertEquals("this string is \n" +
                "exactly 70 \n" +
                "characters long and\n" +
                "fits in 5 lines, \n" +
                "*nice*!", line);

        s = "this string has a_sequence_without_any_whitespace_and_needs_to_get_chopped";

        line = StringHelpers.wrapMultiLine(s, 20, "\n");

        assertEquals("this string has a_s\n" +
                "equence_without_any\n" +
                "_whitespace_and_nee\n" +
                "ds_to_get_chopped", line);
    }

    @Test
    void wrapLongWordTest() {

        String s = "a_sequence_without_any_whitespace_needs_to_get_chopped";

        String line = StringHelpers.wrapLongWord(s, 3, 20, "\n");

        assertEquals("a_\n" +
                "sequence_without_an\n" +
                "y_whitespace_needs_\n" +
                "to_get_chopped", line);

        line = StringHelpers.wrapLongWord(s, 0, 20, "\n");

        assertEquals("a_sequence_without_\n" +
                "any_whitespace_need\n" +
                "s_to_get_chopped", line);
    }

    @Test
    void lineSelectionTest() {
        String s = "line 1\nline 2\nline 3";

        String firstLine = StringHelpers.firstLineOf(s, "\n");

        assertEquals(firstLine, "line 1");

        String lastLine = StringHelpers.lastLineOf(s, "\n");

        assertEquals(lastLine, "line 3");
    }

    @Test
    void twoColumnLayoutTest() {
        String left = "left";
        String right = "right";
        int width = left.length() + right.length() + 1;
        char pad = '*';

        String line = StringHelpers.twoColumnLayout(left,right,width,pad);

        assertEquals(line, "left*right");
    }
}
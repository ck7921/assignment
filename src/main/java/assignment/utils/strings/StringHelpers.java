package assignment.utils.strings;

import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Apache commons has several libraries providing much better support
 * for formatting text, numbers and any other values.
 * For the purpose of this assignment this just acts as a support utility.
 */
public final class StringHelpers {

    private StringHelpers() {
        throw new IllegalStateException("instance not permitted");
    }

    public static String wrapMultiLine(String s, int limit, String lineBreak) {
        if(limit-lineBreak.length()<1) {
            throw new IllegalArgumentException("limit too small TODO explain better");
        }
        final int netLimit = limit - lineBreak.length();
        final String[] chunks = s.split("\\s");
        final StringBuilder buffer = new StringBuilder();
        int spaceLeft = netLimit;

        for (int i = 0; i < chunks.length; i++) {
            String chunk =  chunks[i]
                    + (i+1 == chunks.length || chunks[i].length() == spaceLeft ? "" : " ");
            int requiredSpace = chunk.length();

            if(requiredSpace<=spaceLeft) {
                buffer.append(chunk);
                spaceLeft-=chunk.length();
            } else {
                if(requiredSpace<netLimit) {
                    buffer.append(lineBreak).append(chunk);
                    spaceLeft = netLimit - chunk.length();
                } else {
                    final String wrappedChunk =  wrapLongWord(chunk, spaceLeft+1, limit, lineBreak);
                    buffer.append(wrappedChunk);
                    spaceLeft = netLimit - wrappedChunk.lastIndexOf(lineBreak) + lineBreak.length();
                }
            }

            if(spaceLeft==0 && i+1 < chunks.length) {
                buffer.append(lineBreak);
                spaceLeft = netLimit;
            }
        }
        return buffer.toString();
    }

    public static String wrapLongWord(final String s, final int begin, final int lineLength,
                                      final String lineBreak) {

        final int netLimit = lineLength - lineBreak.length();
        int initialSpace = begin - lineBreak.length();
        final char[] characters = s.toCharArray();
        final StringBuilder buffer = new StringBuilder();
        int spaceLeft = netLimit;
        for (int i = 0; i < characters.length; i++) {
            buffer.append(characters[i]);
            final boolean isLastChar = i+1 == characters.length;

            if(initialSpace>0) {
                initialSpace--;
                if(initialSpace==0 && !isLastChar) buffer.append(lineBreak);
            } else {
                spaceLeft--;
            }

            if(spaceLeft==0 && !isLastChar) {
                buffer.append(lineBreak);
                spaceLeft = netLimit;
            }
        }

        return buffer.toString();
    }

    public static String firstLineOf(final String s, final String lineBreak) {
        final Scanner scanner = new Scanner(s)
                .useDelimiter(lineBreak);
        return scanner.tokens().findFirst().orElse(null);
    }

    public static String lastLineOf(final String s, final String lineBreak) {
        final Scanner scanner = new Scanner(s)
                .useDelimiter(lineBreak);
        return scanner.tokens()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public static Stream<String> tokenStreamOf(final String s, final String lineBreak) {
        return new Scanner(s)
                .useDelimiter(lineBreak)
                .tokens();
    }

    public static String twoColumnLayout(final String left, final String right, final int width,
                                         final char padding) {
        int totalWidth = (left!=null ? left.length() : 0)
                + (right!=null ? right.length() : 0);
        if(totalWidth>=width) {
            return (left!=null ? left : "")
                    + (right!=null ? right : "");
        }
        int padLen = width - totalWidth;
        return (left!=null ? left : "") + (""+padding).repeat(padLen) + (right!=null ? right : "");
    }

}

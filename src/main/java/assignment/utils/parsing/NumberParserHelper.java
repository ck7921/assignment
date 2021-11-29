package assignment.utils.parsing;

public final class NumberParserHelper {

    private NumberParserHelper() {
        throw new IllegalStateException("instance not permitted");
    }

    public static int parseInt(final String s, final int defaultValue) {
        try {
            return Integer.parseInt(s,10);
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }
}

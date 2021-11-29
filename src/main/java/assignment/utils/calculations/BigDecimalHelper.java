package assignment.utils.calculations;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class BigDecimalHelper {

    private static final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.GERMANY);

    private BigDecimalHelper() {
        throw new IllegalStateException("instance not permitted");
    }

    public static boolean gt(final BigDecimal left, final BigDecimal right) {
        return left.compareTo(right) > 0;
    }

    public static boolean lt(final BigDecimal left, final BigDecimal right) {
        return left.compareTo(right) < 0;
    }

    public static String formatPrice(final String currency, final BigDecimal value) {
        DecimalFormat df = new DecimalFormat("#,##0.00", decimalFormatSymbols);
        return df.format(value!=null ? value : BigDecimal.ZERO) + " " + currency;
    }

}

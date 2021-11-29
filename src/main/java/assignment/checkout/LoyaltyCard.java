package assignment.checkout;

import java.util.Properties;

public class LoyaltyCard {

    private Properties stamps;

    public LoyaltyCard(final Properties stamps) {
        this.stamps = stamps;
    }

    public LoyaltyCard(final LoyaltyCard src) {
        this.stamps = new Properties();
        src.stamps.forEach((key, value) -> stamps.put(key, value));
    }

    public Properties getStamps() {
        return stamps;
    }

    public int getStampCounter(final String itemClass) {
        if(!stamps.containsKey(itemClass)) {
            return 0;
        }
        return Integer.parseInt(stamps.getOrDefault(itemClass,0)
                .toString(),10);
    }

    public void incrementStampCounter(final String itemClass) {
        stamps.put(itemClass, getStampCounter(itemClass) + 1);
    }

    public void decrementStampCounter(final String itemClass, final int count) {
        stamps.put(itemClass, Math.max(getStampCounter(itemClass) - count,0));
    }
}

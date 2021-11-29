package assignment.receipt;

import java.time.Instant;
import java.util.List;

public interface Receipt {
    String getId();

    Instant getCreationTimestamp();

    List<LineItem> getLineItems();

    List<String> getAdditionalText();

    String getGrandTotalDisplay();

    String getGrandTotalDiscount();

}

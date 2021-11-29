package assignment.receipt;

import java.util.List;

public interface LineItem {
    String getDisplayCount();

    String getDisplayName();

    String getDisplayDiscount();

    String getDisplayPrice();

    String getDisplayTotal();

    List<LineItem> getSubItems();
}

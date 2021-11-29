package assignment.receipt;

import assignment.utils.strings.StringHelpers;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Rendering receipts to the given stream.
 * For more details please read {@link assignment.receipt.ReceiptRenderer}
 */
class ConsoleReceiptRenderer implements ReceiptRenderer {

    private String header;
    private String footer;
    private int minimalWidthInCharacters = 80;
    private int totalWidthInCharacters = minimalWidthInCharacters;
    private Charset outputCharset = StandardCharsets.UTF_8;
    private String lineBreak = "\n";
    private String timestampPattern = "YYYY-MM-DD HH:mm";
    private ZoneId timeZone = ZoneId.of("Europe/Zurich");

    private int countFieldLen = 5;
    private int descriptionFieldLenMin = 19;
    private int descriptionFieldLen = descriptionFieldLenMin;
    private int priceFieldLen = 15;
    private int discountFieldLen = 16;
    private int totalFieldLen = 15;

    public void setHeader(final String header) {
        this.header = header;
    }

    public void setFooter(final String footer) {
        this.footer = footer;
    }

    public void setTimeZone(final ZoneId timeZone) {
        if(timeZone==null) {
            throw new IllegalArgumentException("time zone must not be null");
        }
        this.timeZone = timeZone;
    }

    public void setTimestampPattern(final String timestampPattern) {
        if(timestampPattern==null) {
            throw new IllegalArgumentException("timestamp pattern must not be null");
        }
        // just validation
        DateTimeFormatter timestampFormatter = DateTimeFormatter
                .ofPattern(timestampPattern);
        this.timestampPattern = timestampPattern;
    }

    public void setTotalWidthInCharacters(final int totalWidthInCharacters) {
        if(totalWidthInCharacters<minimalWidthInCharacters) {
            throw new IllegalArgumentException("minimal width is " + minimalWidthInCharacters);
        }
        // adjust field length
        final int widthMin = countFieldLen + descriptionFieldLenMin + priceFieldLen + discountFieldLen
                + totalFieldLen;
        if(totalWidthInCharacters>widthMin) {
            descriptionFieldLen += totalWidthInCharacters-widthMin;
        }
        this.totalWidthInCharacters = totalWidthInCharacters;
    }

    @Override
    public void render(final Receipt receipt, final OutputStream out) {

        final DateTimeFormatter timestampFormatter = DateTimeFormatter
                .ofPattern(timestampPattern)
                .withZone(timeZone);
        final PrintWriter writer = new PrintWriter(out,true, outputCharset);

        writer.print(header);
        writer.print(lineBreak);
        writer.print(lineBreak);

        writer.print("Receipt Id: ");
        writer.print(receipt.getId());
        writer.print(lineBreak);

        writer.print("Purchase Time: ");
        writer.print(timestampFormatter.format(receipt.getCreationTimestamp()));
        writer.print(lineBreak);
        writer.print(lineBreak);

        renderTableHeader(writer);
        writer.print(lineBreak);
        writer.print("-".repeat(totalWidthInCharacters));
        writer.print(lineBreak);

        // render positions
        receipt.getLineItems().forEach(item -> {
            renderLineItem(writer, item, false);
        });

        writer.print("-".repeat(totalWidthInCharacters));
        writer.print(lineBreak);

        // total
        writer.print(StringHelpers.twoColumnLayout("", "Total: " + receipt.getGrandTotalDisplay(),totalWidthInCharacters,' '));
        writer.print(lineBreak);

        // discount total
        if(receipt.getGrandTotalDiscount()!=null && receipt.getGrandTotalDiscount().length()>0) {
            writer.print(StringHelpers.twoColumnLayout("", "included Total discount: " + receipt.getGrandTotalDiscount(),totalWidthInCharacters,' '));
            writer.print(lineBreak);
        }

        // additional text
        receipt.getAdditionalText()
                .forEach(writer::println);

        writer.print(lineBreak);
        writer.print(footer);
        writer.print(lineBreak);

        writer.flush();
    }

    private void renderLineItem(final PrintWriter writer, final LineItem item, final boolean isSubItem) {
        final String position = (item.getDisplayCount()!=null ? item.getDisplayCount() : "") + " ";
        final String price = (item.getDisplayPrice()!=null ? item.getDisplayPrice() : "");
        final String discount = (item.getDisplayDiscount()!=null ? item.getDisplayDiscount() : "");
        final String total = (item.getDisplayTotal()!=null ? item.getDisplayTotal() : "");
        String description = item.getDisplayName();
        final boolean isMultiLine = description!=null && description.length() >= descriptionFieldLen;

        if(isMultiLine) {
            description = StringHelpers.wrapMultiLine(description, descriptionFieldLen, lineBreak);
            final String firstLine = StringHelpers.firstLineOf(description, lineBreak);
            final String lastLine =  StringHelpers.lastLineOf(description, lineBreak);
            final long lineCountTotal = StringHelpers.tokenStreamOf(description, lineBreak).count();

            // first line
            renderLineItem(writer, isSubItem ? "" : position, firstLine, "", "", "");

            if(lineCountTotal>2) {
                StringHelpers.tokenStreamOf(description, lineBreak)
                        .skip(1)
                        .limit(lineCountTotal-2)
                        .forEach( line -> {
                            renderLineItem(writer, "", line, "", "", "");
                        });
            }

            // last line
            renderLineItem(writer, "", lastLine, price, discount, total);
        } else {
            renderLineItem(writer, isSubItem ? "" : position, description, price, discount, total);
        }

        if(!isSubItem && item.getSubItems()!=null && item.getSubItems().size()>0) {
            for (final LineItem subItem : item.getSubItems()) {
                renderLineItem(writer,subItem, true);
            }
        }

    }

    private void renderLineItem(final PrintWriter writer, final String qty, final String description,
                                final String price, final String discount, final String total) {
        final String line = String.format("%"+countFieldLen+"s%-"+descriptionFieldLen
                        + "s%" + priceFieldLen + "s%"+ discountFieldLen
                        + "s%" + totalFieldLen + "s" + lineBreak,
                qty, description, price, discount, total);
        writer.print(line);
    }

    private void renderTableHeader(final PrintWriter writer) {
        writer.print(String.format("%"+countFieldLen+"s%-"+descriptionFieldLen
                        + "s%" + priceFieldLen + "s%" + discountFieldLen
                        + "s%" + totalFieldLen + "s",
                "# ", "Description ", "Price", "Discount", "Total"));
    }

}

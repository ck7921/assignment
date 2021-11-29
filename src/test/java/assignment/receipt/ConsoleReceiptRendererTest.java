package assignment.receipt;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleReceiptRendererTest {

    private Supplier<Instant> nowProvider = Instant::now;

    @Test
    public void simpleRenderTest() {

        final ReceiptRendererBuilder rendererBuilder = new ReceiptRendererBuilder(RendererType.PLAIN_TEXT);
        rendererBuilder.withHeader("Test_Header");
        rendererBuilder.withFooter("Test_Footer");
        rendererBuilder.withLineWidthInCharacters(80);
        final ReceiptRenderer renderer = rendererBuilder.build();
        final Receipt receipt = simpleReceipt();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.render(receipt, baos);
        final String result = baos.toString(StandardCharsets.UTF_8);

        assertNotNull(result);
        assertTrue(result.contains("Test_Header"));
        assertTrue(result.contains("Test_Footer"));
        assertTrue(result.contains("Total: CHF 3.444,90"));
    }

    private Receipt simpleReceipt() {
        return new ReceiptBuilder()
                .withId("12345")
                .withCreationTimestamp(nowProvider.get())

                .buildLineItem()
                .withDisplayCount("1")
                .withDisplayName("Test_pos has a very long text that doesnt fit in one line")
                .withDisplayPrice("CHF 4.567,90")
                .withDisplayDiscount("CHF 1.123,00")
                .withDisplayTotal("CHF 3.444,90")
                .add()

                .withGrandTotalDisplay("CHF 3.444,90")
                .withGrandTotalDiscountDisplay("CHF 1.123,00")
                .build();
    }

}
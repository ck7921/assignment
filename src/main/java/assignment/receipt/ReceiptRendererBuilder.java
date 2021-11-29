package assignment.receipt;

import java.io.OutputStream;

public class ReceiptRendererBuilder {

    private final RendererType rendererType;

    private String header;
    private String footer;
    private int totalWidthInCharacters = 80;

    public ReceiptRendererBuilder(final RendererType rendererType) {
        this.rendererType = rendererType;
    }

    public ReceiptRendererBuilder withHeader(final String header) {
        this.header = header;
        return this;
    }

    public ReceiptRendererBuilder withFooter(final String footer) {
        this.footer = footer;
        return this;
    }

    public ReceiptRendererBuilder withLineWidthInCharacters(final int totalWidthInCharacters) {
        this.totalWidthInCharacters = totalWidthInCharacters;
        return this;
    }

    public ReceiptRenderer build() {
        switch (rendererType) {
            case PLAIN_TEXT -> {
                return buildConsoleRenderer();
            }
            case DEV_NULL -> {
                return devNullRenderer();
            }
            default -> throw new IllegalStateException("render type not supported");
        }
    }

    private ReceiptRenderer buildConsoleRenderer() {
        final ConsoleReceiptRenderer renderer = new ConsoleReceiptRenderer();
        renderer.setHeader(header);
        renderer.setFooter(footer);
        renderer.setTotalWidthInCharacters(totalWidthInCharacters);
        return renderer;
    }

    private ReceiptRenderer devNullRenderer() {
        return new ReceiptRenderer() {
            @Override
            public void render(Receipt receipt, OutputStream out) {

            }
        };
    }
}

package assignment.receipt;

import java.io.OutputStream;

/**
 * A Receipt Renderer renders a given receipt to a given destination.
 * In this sample application only a console renderer is implemented.
 * Additional render implementation could render to JSON, PDF, XML or
 * even template engines like Freemarker or Velocity etc.
 * could be used by implementors.
 * Changing how the receipt should be display wouldn't effect
 * any other parts of the application.
 */
public interface ReceiptRenderer {

    void render(final Receipt receipt, final OutputStream out);

}

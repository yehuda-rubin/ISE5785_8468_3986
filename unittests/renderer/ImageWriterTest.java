package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {
    /**
     * A table of 16 by 10 squares, each square measuring 50 by 50 pixels, with the inside of the square colored yellow and the outside red.
     */
    @Test
    void testWriteToImage() {
        assertDoesNotThrow(() -> {
            Color yellow = new Color(java.awt.Color.YELLOW);
            Color red = new Color(java.awt.Color.RED);
            ImageWriter imageWriter = new ImageWriter(801, 501);
            for (int i = 0; i < 801; i++) {
                for (int j = 0; j < 501; j++) {
                    if (i % 50 == 0 || j % 50 == 0) {
                        imageWriter.writePixel(i, j, red);
                    } else {
                        imageWriter.writePixel(i, j, yellow);
                    }
                }
            }
            imageWriter.writeToImage("testImage");
        }, "Failed to create image");
    }
}
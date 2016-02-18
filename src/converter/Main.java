package converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;

/**
 * Created by teodor on 18.02.2016.
 */
public class Main {
    public static void main(String[] args ) throws IOException {
        PDDocument document = new PDDocument();
        // Create a new blank page and add it to the document
        PDPage blankPage = new PDPage();
        document.addPage( blankPage );

        // Save the newly created document
        document.save("BlankPage.pdf");

        // finally make sure that the document is properly
        // closed.
        document.close();
    }
}

package converter;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by teodor on 18.02.2016.
 */
public class Main {
    public static void main(String[] args ) throws IOException {

        PDFTextStripper pdfStripper = null;
        PDDocument pdfDocument = null;
        COSDocument cosDocument = null;
        File inputFile = null;
        PDFParser parser = null;

        ArrayList<Publication> publications = new ArrayList<Publication>();

        int jumpIndex = 0;

        try {
            inputFile = new File("RoCHI-2015-proceedings-vfinal.pdf");
            parser = new PDFParser(new RandomAccessBufferedFileInputStream(inputFile));
            parser.parse();
            cosDocument = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdfDocument = new PDDocument(cosDocument);

            // aici incepe cuprinsul
            // daca nu ii dau setEndPage atunci o sa citeasca by default tot pdf-ul
            pdfStripper.setStartPage(3);
            pdfStripper.setEndPage(4);
            String parsedText = pdfStripper.getText(pdfDocument);
            if( parsedText.contains("TABLE OF CONTENTS") ) {
                jumpIndex = parsedText.indexOf("TABLE OF CONTENTS");
                jumpIndex += 18; // 17 lungimea + \n
                parsedText = parsedText.substring(jumpIndex);
                jumpIndex = 0;
                String title = "";
                String author = "" ;
                String currentString = "";
                int pageIndex = 0;
                //start parsing
                // i will exit when i will have the author index or smth

                //System.out.println(parsedText);

//
//                while( parsedText.length() != 0 )  {
//                    jumpIndex = parsedText.indexOf('\n');
//                    currentString = parsedText.substring(jumpIndex);
//                    parsedText = parsedText.substring(jumpIndex + 1);
//
//                    // it's empty line
//
//
//                    if( !currentString.contains("\\w+") )
//                        continue;
//                    System.out.println(currentString);
//
//                    if( currentString.charAt(currentString.length() - 2) == '-' ) {
//                        jumpIndex = parsedText.indexOf('\n');
//                        currentString += parsedText.substring(jumpIndex);
//                        parsedText = parsedText.substring(jumpIndex + 1);
//                    }
//                    System.out.println(currentString);
//
//                    if( currentString.contains("..") || currentString.contains("…") ) {
//                        System.out.println(currentString);
//                    }
//
//                }
//

                for( String thing : parsedText.split("\\n\\s+\\n") )
                    System.out.println("!" + thing + "!");

//
//                int jumpIndexAuxiliar = 0;
//                while ( parsedText.length() != 0 ) {
//
//                    jumpIndex =
//                    //next thing
//                    if( jumpIndex != -1 && jumpIndex < jumpIndexAuxiliar ) {
//                        jumpIndex += 3;
//                        currentString = parsedText.substring(0, jumpIndex - 3);
//                    }
//                    else
//                        if( jumpIndexAuxiliar != -1 && jumpIndexAuxiliar < jumpIndex ) {
//                            jumpIndex = jumpIndexAuxiliar;
//                            jumpIndex += 4;
//                            currentString = parsedText.substring(0, jumpIndex - 4);
//
//                        }
//                    else
//                        {
//                            System.out.println("GATA");
//                            break;
//                        }
//
//                    System.out.println("!!!" + currentString + "!!!\n\n");
//
//                    if( !currentString.contains("..") || !currentString.contains("…") ) {
//                        continue;
//                    }
//
//                }

                //System.out.println("!" + parsedText + "+");
            }





        } catch (IOException e) {
            System.out.println("Eroare deschidere fisier");
            System.exit(1);
        }

        /* Presupun ca mai toate pdf-uri de conferinte o sa aiba pe
        *  pe prima pagina titlu, iar pe a doua detalii despre organizatori
        *  sau alte lucruri. Asa ca o sa sar la pagina 3 direct, unde este cuprisnul
        * */

    }
}

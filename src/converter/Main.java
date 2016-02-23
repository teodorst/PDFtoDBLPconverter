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
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by teodor on 18.02.2016.
 */



public class Main {

    private static boolean isNumber ( char character ) {
        return (
                character >= 48 && character <= 67
        );
    }

    private static boolean isLetterOrNumber( char character ) {
        return (
            isNumber(character) || // number
            character >= 65 && character <= 90 || // big letter
            character >= 97 && character <= 122   // small letter
        );
    }

    private static int min( int a, int b ) {
        if ( a == -1 && b == -1 )
            return -1;
        else
            if( a == -1 )
                return b;
        else
            if( b == -1 )
                return a;
        else
            return a < b ? a : b;
    }


    private static int max( int a, int b ) {
        if ( a == -1 && b == -1 )
            return -1;
        else
        if( a == -1 )
            return b;
        else
        if( b == -1 )
            return a;
        else
            return a > b ? a : b;
    }

    public static void main(String[] args ) throws IOException {

        PDFTextStripper pdfStripper = null;
        PDDocument pdfDocument = null;
        COSDocument cosDocument = null;
        File inputFile = null;
        PDFParser parser = null;

        ArrayList<Publication> publications = new ArrayList<Publication>();

        int jumpIndex = 0;
        int paperContor = 0;

        String title;
        String authors;


        try {

        /* Presupun ca mai toate pdf-uri de conferinte o sa aiba pe
        *  pe prima pagina titlu, iar pe a doua detalii despre organizatori
        *  sau alte lucruri. Asa ca o sa sar la pagina 3 direct, unde este cuprisnul
        * */

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

                //start parsing
                for( String chunk : parsedText.split("\\n\\s+\\n") ) {
                    /* sunt cazuri in care am mai multe ' ' si cazuri
                    cand am unul singur, asa ca am decis sa las doar
                    un singur spatiu. */
                    chunk = chunk.replaceAll("\\s+", " ");
                    System.out.println("!" + chunk + "!");

                    if( ( chunk.contains("..") || chunk.contains("…") ) && chunk.contains("\uF0B7 ") ) {

                        if( isLetterOrNumber(chunk.charAt(0)) ) {
                            //it's catergory - voi sari pana la acel caracter. Trebuie sa vedem daca putem pastra acest
                            // caracter ca punct de reper.
                            chunk = chunk.substring(
                                    chunk.indexOf("\uF0B7 "), // aici trebuie sa pun un regex. Inca ma gandesc cum sa fac
                                    chunk.length()
                            );
                        }

                        int startIndex = min( chunk.indexOf('…'), chunk.indexOf("..") ) ;
                        int finishIndex = max( chunk.lastIndexOf("…… "), chunk.lastIndexOf(".. ") );
                        finishIndex = max( finishIndex, chunk.lastIndexOf("…. "));

                        if ( startIndex != -1 && finishIndex != -1 ) {
                            title = chunk.substring(1, startIndex).trim();
                            authors = chunk.substring( finishIndex + 3, chunk.length() );
                            Vector<String> authorZ = new Vector<String>(Arrays.asList(authors.split(" ,")));
                            publications.add( new Publication(title, authorZ) );

                        }
                    }
                    else
                        if( isNumber(chunk.trim().charAt(0)) && chunk.length() < 6 ) {
                            // voi presupune ca nu o sa existe pagini mai mari ca 999999
                            int pageNumber = Integer.parseInt(chunk.trim());
                            if( paperContor > 0 && paperContor < publications.size() + 1) {
                                publications.get(paperContor - 1).setFinishPage(pageNumber - 1);
                            }
                            if( paperContor < publications.size() ) {
                                publications.get(paperContor).setStartPage(pageNumber);
                                paperContor++;
                            }
                        }
                }
                for( int i = 0; i < publications.size(); i ++ ) {
                    System.out.println("\n" + publications.get(i) + "\n");
                }
            }

        } catch (IOException e) {
            System.out.println("Eroare deschidere fisier");
            System.exit(1);
        }

    }
}

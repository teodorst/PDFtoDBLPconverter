package converter;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by teodor on 28.02.2016.
 */
public class Publish {
    private ArrayList<Publication> publications;
    private String ISBN, ISSN, conferenceTitle, filePath, date, location, url;
    private Vector<String> editors;
    public static String[] months = {
            "January", "February", "March",
            "April", "May", "June",
            "July", "August", "September",
            "October", "November", "December"
    };
    public Publish( String filePath ) {
        ISBN = ISSN = conferenceTitle = null;
        this.filePath = filePath;
        this.publications = new ArrayList<Publication>();
        this.editors = new Vector<String>();
    }

    public String getConferenceTitle() {
        return conferenceTitle;
    }

    public void setConferenceTitle(String conferenceTitle) {
        this.conferenceTitle = conferenceTitle;
    }

    public Vector<String> getEditors() {
        return editors;
    }

    public void setEditors(Vector<String> editors) {
        this.editors = editors;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean containsMonth(String text) {
        boolean res = false;
        for( String month : months ) {
            res = res || text.contains(month);
            if( res )
                break;
        }
        return res;
    }

    public void extractInformationFromPDF() throws IOException {
        PDFTextStripper pdfStripper = null;
        PDDocument pdfDocument = null;
        COSDocument cosDocument = null;
        File inputFile = null;
        PDFParser parser = null;

        int jumpIndex = 0;
        int paperContor = 0;

        String title, authors, parsedText, section;
        title = authors = parsedText = section = "";

        /* Presupun ca mai toate pdf-uri de conferinte o sa aiba pe
        *  pe prima pagina titlu, iar pe a doua detalii despre organizatori
        *  sau alte lucruri. Asa ca o sa sar la pagina 3 direct, unde este cuprisnul
        * */

        inputFile = new File(filePath);
        parser = new PDFParser(new RandomAccessBufferedFileInputStream(inputFile));
        parser.parse();
        cosDocument = parser.getDocument();
        pdfStripper = new PDFTextStripper();
        pdfDocument = new PDDocument(cosDocument);


        //extrag Numele Conferintei
        pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(2);
        parsedText = pdfStripper.getText(pdfDocument);
        jumpIndex = parsedText.indexOf("\n \n");
        conferenceTitle = parsedText.substring(0, jumpIndex).trim();

        //sar peste titlu
        jumpIndex = parsedText.indexOf(conferenceTitle) + conferenceTitle.length();
        parsedText = parsedText.substring(jumpIndex);

        while( conferenceTitle.contains("\n") ) {
            jumpIndex = conferenceTitle.indexOf(" \n");
            conferenceTitle = conferenceTitle.substring(0, jumpIndex) + conferenceTitle.substring(jumpIndex + 2);
            conferenceTitle.trim();
        }
        System.out.println(conferenceTitle);


        //extrag editorii
        boolean editorsState = false;
        boolean descriptionState = false;
        boolean locationState = false;
        for(String linie : parsedText.split("\n")) {
            if( containsMonth(linie) ) { // get date
                jumpIndex = linie.indexOf(", ");
                date = linie.substring(jumpIndex + 2).trim();
                locationState = true;
            }
            else
                if( locationState ) { // get location
                    location = linie.trim();
                    locationState = false;
                }
            else
                if( linie.contains("Edited")) {
                    editorsState = true;
                }
            else
                if( editorsState && linie.contains("and") ) {
                    continue;
                }
            else
                if(editorsState && (linie.trim().equals(" ") || linie.trim().isEmpty())) {
                    editorsState = false;
                }
            else
                if( editorsState && descriptionState) {
                    descriptionState = false;
                    continue;
                }
            else
                if( editorsState ) {
                    editors.add(linie.trim());
                    descriptionState = true;
                }
            else
                if( !editorsState && !descriptionState && linie.trim().contains("ISSN") ) {
                    jumpIndex = linie.indexOf("ISSN");
                    jumpIndex += 4;
                    ISSN = linie.substring(jumpIndex).trim();
                }
            else
                if( !editorsState && !descriptionState && linie.trim().contains("ISBN") ) {
                    jumpIndex = linie.indexOf("ISBN");
                    jumpIndex += 4;
                    ISBN = linie.substring(jumpIndex).trim();
                }
        }

        System.out.println(editors);
        System.out.println(ISSN);
        System.out.println(ISBN);
        System.out.println(date);
        System.out.println(location);

        // aici incepe cuprinsul
        // daca nu ii dau setEndPage atunci o sa citeasca by default tot pdf-ul
        pdfStripper.setStartPage(3);
        pdfStripper.setEndPage(4);
        parsedText = pdfStripper.getText(pdfDocument);
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
//                    System.out.println("!" + chunk + "!");

                if( ( chunk.contains("..") || chunk.contains("…") ) && chunk.contains("\uF0B7 ") ) {

                    if( isLetterOrNumber(chunk.charAt(0)) ) {
                        //it's section - voi sari pana la acel caracter. Trebuie sa vedem daca putem pastra acest
                        // caracter ca punct de reper.

                        // intai iau sectiunea
                        section = chunk.substring(0, chunk.indexOf("\uF0B7 ")).trim();
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
                        publications.add( new Publication(title, section, authorZ) );
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
            // afisez lucrarile
            for( int i = 0; i < publications.size(); i ++ ) {
                System.out.println("\n" + publications.get(i) + "\n");
            }
        }

    }


    private boolean isNumber ( char character ) {
        return (
                character >= 48 && character <= 67
        );
    }

    private boolean isLetterOrNumber( char character ) {
        return (
                isNumber(character) || // number
                        character >= 65 && character <= 90 || // big letter
                        character >= 97 && character <= 122   // small letter
        );
    }

    private int min( int a, int b ) {
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


    private int max( int a, int b ) {
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


    public void addPublication(Publication newPublication) {
        publications.add(newPublication);
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

    public void setPublications(ArrayList<Publication> publications) {
        this.publications = publications;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public String getConfTitle() {
        return conferenceTitle;
    }

    public void setConfTitle(String confTitle) {
        this.conferenceTitle = confTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

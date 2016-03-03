package converter;

import java.io.File;
import java.io.IOException;
import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

///**
// * Created by teodor on 18.02.2016.
// */


public class Main {



    public static void dbplXMLCreate(Publish publish) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // submissionElement elements
        Document doc = docBuilder.newDocument();
        Element submissionElement = doc.createElement("dblpsubmission");
        doc.appendChild(submissionElement);

        // proceedings element

        Element proceedingsElement = doc.createElement("proceedings");
        submissionElement.appendChild(proceedingsElement);

        // key
        Element keyElement = doc.createElement("key");
        keyElement.setTextContent("Computer Science");
        proceedingsElement.appendChild(keyElement);

        for( String editor : publish.getEditors()) {
            Element editorElement = doc.createElement("editor");
            editorElement.setTextContent(editor);
            proceedingsElement.appendChild(editorElement);
        }

        //title
        Element titleElement = doc.createElement("title");
        titleElement.setTextContent(publish.getConferenceTitle());
        proceedingsElement.appendChild(titleElement);

        Element publisherElement = doc.createElement("publisher");
        publisherElement.setTextContent(publish.getPublishBy());
        proceedingsElement.appendChild(publisherElement);


        //year
        Element yearElement = doc.createElement("year");
        yearElement.setTextContent("" + publish.getYear());
        proceedingsElement.appendChild(yearElement);


//        //ISSN
//        if( publish.getISSN() != null ) {
//            Element issnElement = doc.createElement("issn");
//            issnElement.setTextContent(publish.getISSN());
//            issnElement.setAttribute("type", "electronic");
//            proceedingsElement.appendChild(issnElement);
//        }


        //ISBN
        if( publish.getISBN() != null ) {
            Element isbnElement = doc.createElement("isbn");
            isbnElement.setTextContent(publish.getISBN());
            isbnElement.setAttribute("type", "electronic");
            proceedingsElement.appendChild(isbnElement);
        }

        //conf Element
        Element confElement = doc.createElement("conf");
        proceedingsElement.appendChild(confElement);

        //location Element
        Element locationElement = doc.createElement("location");
        locationElement.setTextContent(publish.getLocation());
        confElement.appendChild(locationElement);

        // date Element
        Element dateElement = doc.createElement("date");
        dateElement.setTextContent(publish.getDate());
        confElement.appendChild(dateElement);

        // toc Element
        Element tocElement = doc.createElement("toc");
        proceedingsElement.appendChild(tocElement);

        String currentSection = "";
        Element currentSectionElement, publElement, publAuthorElement, publTitleElement, publPagesElement;
        for( Publication publ : publish.getPublications()) {
            if( !currentSection.equals(publ.getSection()) ) {
                currentSection = publ.getSection();
                // section Element
                currentSectionElement = doc.createElement("section");
                currentSectionElement.setTextContent(currentSection);
                tocElement.appendChild(currentSectionElement);
            }
            else {

                // publ element
                publElement = doc.createElement("publ");
                tocElement.appendChild(publElement);

                // publ_author element
                for( String author : publ.getAuthors() ) {
                    publAuthorElement = doc.createElement("author");
                    publAuthorElement.setTextContent(author);
                    publElement.appendChild(publAuthorElement);
                }

                // publ_title element
                publTitleElement = doc.createElement("title");
                publTitleElement.setTextContent(publ.getName());
                publElement.appendChild(publTitleElement);


                // publ_pages element
                publPagesElement = doc.createElement("pages");
                publPagesElement.setTextContent(publ.getStartPage() + "-" + publ.getFinishPage());
                publElement.appendChild(publPagesElement);

            }
        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "dblpsubmission.dtd");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("dblpFile.xml"));

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        System.out.println("File saved!");

    }

    public static void main(String[] args ) throws IOException {

        Publish publish = new Publish("RoCHI-2015-proceedings-vfinal.pdf");

        try {
            publish.extractInformationFromPDF();
        } catch (IOException e) {
            System.out.println("Eroare deschidere fisier");
            System.exit(1);
        }
        try {
            dbplXMLCreate(publish);
        } catch (ParserConfigurationException e) {
            System.out.println("Eroare creare xml");
            System.exit(1);
        } catch (TransformerException e) {
            System.out.println("Eroare salvare xml");
            System.exit(1);
        }

    }
}

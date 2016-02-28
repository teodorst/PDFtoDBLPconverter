package converter;

import java.util.Vector;

/**
 * Created by teodor on 18.02.2016.
 */
public class Publication {
    private Vector<String> authors;
    private String email, name, section;
    private int startPage, finishPage;

    public Publication() {
        name = "";
        authors = new Vector<String>();
        email = "";
        section = "";
        startPage = 0;
        finishPage = 0;
    }

    public Publication( String name, String section ) {
        this.name = name;
        this.section = section;
    }

    public Publication( String name, String section, Vector<String> authors ) {
        this(name, section);
        this.authors = authors;
    }

    public Vector<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Vector<String> authors) {
        this.authors = authors;
    }

    public void addAuthor(String newAuthor) {
        this.authors.add( newAuthor );
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getFinishPage() {
        return finishPage;
    }

    public void setFinishPage(int finishPage) {
        this.finishPage = finishPage;
    }

    public String toString() {
        String output = "--- Name: " + name +
                "\n--- Section: " + section +
                "\n--- Author: " + authors +
                "\n--- StarPage: " + startPage +
                "\n--- FinishPage: " + finishPage ;
        return output;
    }

}

package converter;

import java.util.Vector;

/**
 * Created by teodor on 18.02.2016.
 */
public class Publication {
    private Vector<String> authors;
    private String email, name, topic;
    private int startPage, finishPage;
    //String content; (maybe)

    public Publication() {
        name = "";
        authors = new Vector<String>();
        email = "";
        startPage = 0;
        finishPage = 0;
    }

    public Publication( String name ) {
        this.name = name;
    }

    public Publication( String name, Vector<String> authors ) {
        this(name);
        this.authors = authors;
    }



    public Vector<String> getAuthor() {
        return authors;
    }

    public void setAuthor(Vector<String> authors) {
        this.authors = authors;
    }

    public void addAuthro(String newAuthor) {
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

//    public String getTopic() {
//        return topic;
//    }
//
//    public void setTopic(String topic) {
//        this.topic = topic;
//    }

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
                "\n--- Author: " + authors +
                "\n--- StarPage: " + startPage +
                "\n--- FinishPage: " + finishPage ;
        return output;
    }

}

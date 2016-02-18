package converter;

/**
 * Created by teodor on 18.02.2016.
 */
public class Publication {
    String author, email, name, topic;
    int startPage, finishPage;
    //String content; (maybe)

    public Publication() {
        name = "";
        author = "";
        email = "";
        startPage = 0;
        finishPage = 0;
    }

    public Publication( String name, String author, String topic, int startPage, int finishPage ) {
        this();
        this.name = name;
        this.author = author;
        this.topic = topic;
        this.startPage = startPage;
        this.finishPage = finishPage;
    }


    public Publication( String name, String author, String topic, String email, int startPage, int finishPage ) {
        this(name, author, topic, startPage, finishPage);
        this.email = email;
    }

    public String toString() {
        String output = "--- Name: " + name +
                "\n--- Author: " + author +
                "\n--- StarPage: " + startPage +
                "\n--- FinishPage: " + finishPage ;
        return output;
    }

}

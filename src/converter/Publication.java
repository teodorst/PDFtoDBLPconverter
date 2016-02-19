package converter;

/**
 * Created by teodor on 18.02.2016.
 */
public class Publication {
    private String author, email, name, topic;
    private int startPage, finishPage;
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


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
                "\n--- Author: " + author +
                "\n--- StarPage: " + startPage +
                "\n--- FinishPage: " + finishPage ;
        return output;
    }

}

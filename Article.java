package structures;

/**
 * Java equivalent of the C++ Article struct.
 * Simple data holder for an article's id, title, and content.
 */
public class Article {
    public int id;
    public String title;
    public String content;

    public Article() {
        this.id = -1;
        this.title = "";
        this.content = "";
    }

    public Article(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}

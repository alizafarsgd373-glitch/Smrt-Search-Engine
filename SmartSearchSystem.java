package system;

import structures.Article;
import structures.HashMap;
import structures.PriorityQueue;
import structures.Queue;
import structures.Stack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Java port of SmartSearchSystem (originally SmartSearchSystem.h / .cpp).
 */
public class SmartSearchSystem {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "and", "for", "with", "that", "this", "from", "have", "were", "will"
    ));

    private final ArrayList<Article> articles;
    private final HashMap database;
    private final ArrayList<ArrayList<Integer>> graph;
    private final Stack searchHistory;
    private final Queue recentArticles;
    private final ArrayList<Integer> bookmarks;

    public SmartSearchSystem() {
        articles = new ArrayList<>();
        database = new HashMap();
        graph = new ArrayList<>();
        searchHistory = new Stack();
        recentArticles = new Queue();
        bookmarks = new ArrayList<>();
    }

    public String cleanText(String text) {
        StringBuilder sb = new StringBuilder(text.toLowerCase());
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                sb.setCharAt(i, ' ');
            }
        }
        return sb.toString();
    }

    public void loadArticles() {
        File file = new File("Data/Articles.txt");

        if (!file.exists()) {
            System.out.println("Unable To Open File");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int currentID = -1;

            while ((line = reader.readLine()) != null) {
                if (line.contains("ID:")) {
                    currentID++;

                    Article article = new Article();
                    article.id = currentID;
                    article.title = reader.readLine();
                    article.content = reader.readLine();
                    if (article.title == null) article.title = "";
                    if (article.content == null) article.content = "";

                    articles.add(article);

                    String temp = cleanText(article.title + " " + article.content);
                    StringTokenizer tokenizer = new StringTokenizer(temp);

                    while (tokenizer.hasMoreTokens()) {
                        String word = tokenizer.nextToken();
                        database.insert(word, currentID);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable To Open File");
            return;
        }

        for (int i = 0; i < articles.size(); i++) {
            graph.add(new ArrayList<>());
        }
        createGraph();

        System.out.println("Articles Loaded Successfully");
    }

    public void createGraph() {
        for (int i = 0; i < articles.size(); i++) {
            for (int j = i + 1; j < articles.size(); j++) {
                String t1 = cleanText(articles.get(i).title + " " + articles.get(i).content);
                String t2 = cleanText(articles.get(j).title + " " + articles.get(j).content);

                List<String> words1 = new ArrayList<>();
                List<String> words2 = new ArrayList<>();

                StringTokenizer st1 = new StringTokenizer(t1);
                while (st1.hasMoreTokens()) words1.add(st1.nextToken());

                StringTokenizer st2 = new StringTokenizer(t2);
                while (st2.hasMoreTokens()) words2.add(st2.nextToken());

                boolean related = false;

                for (String a : words1) {
                    if (STOP_WORDS.contains(a)) {
                        continue;
                    }

                    for (String b : words2) {
                        if (a.equals(b)) {
                            related = true;
                            break;
                        }
                    }

                    if (related) break;
                }

                if (related) {
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }
            }
        }
    }

    public void search(String query) {
        if (query == null || query.isEmpty()) {
            System.out.println("Search Query Cannot Be Empty");
            return;
        }

        searchHistory.push(query);
        String cleaned = cleanText(query);

        boolean valid = false;
        for (int i = 0; i < cleaned.length(); i++) {
            if (cleaned.charAt(i) != ' ') {
                valid = true;
                break;
            }
        }

        if (!valid) {
            System.out.println("Invalid Search Query");
            return;
        }

        int[] scores = new int[articles.size()];
        StringTokenizer tokenizer = new StringTokenizer(cleaned);

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            int[] freq = database.getFrequency(word);
            for (int i = 0; i < freq.length; i++) {
                scores[i] += freq[i];
            }
        }

        PriorityQueue ranking = new PriorityQueue();
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > 0) {
                ranking.push(new int[]{scores[i], i});
            }
        }

        if (ranking.empty()) {
            System.out.println("No Articles Found");
            return;
        }

        System.out.println("\nTop Search Results\n");
        int top = 5;
        while (!ranking.empty() && top-- > 0) {
            int score = ranking.top()[0];
            int articleID = ranking.top()[1];
            ranking.pop();

            System.out.println("Article ID: " + articleID);
            System.out.println("Title: " + articles.get(articleID).title);
            System.out.println("Score: " + score);
            System.out.println("----------------------------------");
        }
    }

    public void readArticle(int id) {
        if (id < 0 || id >= articles.size()) {
            System.out.println("Invalid Article ID");
            return;
        }

        System.out.println("\n==================================");
        System.out.println(articles.get(id).title);
        System.out.println("==================================\n");
        System.out.println(articles.get(id).content);

        recentArticles.enqueue(id);
        if (recentArticles.size() > 5) {
            recentArticles.dequeue();
        }

        recommendArticles(id);
    }

    public void recommendArticles(int start) {
        if (graph.get(start).isEmpty()) {
            System.out.println("\nNo Related Articles Found");
            return;
        }

        System.out.println("\nRecommended Articles");

        boolean[] visited = new boolean[articles.size()];
        Queue q = new Queue();
        q.enqueue(start);
        visited[start] = true;

        while (!q.empty()) {
            int current = q.front();
            q.dequeue();

            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    System.out.println(articles.get(neighbor).title);
                    q.enqueue(neighbor);
                }
            }
        }
    }

    private void dfs(int node, boolean[] visited) {
        visited[node] = true;
        System.out.println(articles.get(node).title);

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited);
            }
        }
    }

    public void exploreTopics(int start) {
        if (start < 0 || start >= articles.size()) {
            System.out.println("Invalid Article ID");
            return;
        }

        boolean[] visited = new boolean[articles.size()];
        System.out.println("\nDiscover Related Articles");
        dfs(start, visited);
    }

    public void showHistory() {
        System.out.println("\nSearch History");

        if (searchHistory.empty()) {
            System.out.println("No Searches Yet");
            return;
        }

        searchHistory.display();
    }

    public void showRecentArticles() {
        ArrayList<Integer> temp = recentArticles.getData();
        System.out.println("\nRecently Viewed Articles");

        if (temp.isEmpty()) {
            System.out.println("No Articles Viewed Yet");
            return;
        }

        for (int id : temp) {
            System.out.println(articles.get(id).title);
        }
    }

    public void addBookmark(int id) {
        if (id < 0 || id >= articles.size()) {
            System.out.println("Invalid Article ID");
            return;
        }

        for (int bookmark : bookmarks) {
            if (bookmark == id) {
                System.out.println("Article Already Bookmarked");
                return;
            }
        }

        bookmarks.add(id);
        System.out.println("Bookmark Added");
    }

    public void showBookmarks() {
        System.out.println("\nBookmarks");

        if (bookmarks.isEmpty()) {
            System.out.println("No Bookmarks Added");
            return;
        }

        for (int id : bookmarks) {
            System.out.println(articles.get(id).title);
        }
    }

    public void displayArticles() {
        System.out.println("\nAll Articles\n");

        for (Article article : articles) {
            System.out.println("ID: " + article.id);
            System.out.println(article.title);
            System.out.println("--------------------------------");
        }
    }
}

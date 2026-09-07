import system.SmartSearchSystem;

import java.util.Scanner;

/**
 * Java port of main.cpp - menu-driven driver for the Smart Article
 * Search System.
 */
public class Main {

    public static void main(String[] args) {
        SmartSearchSystem system = new SmartSearchSystem();
        Scanner scanner = new Scanner(System.in);

        system.loadArticles();

        while (true) {
            System.out.println("\n====================================");
            System.out.println("SMART ARTICLE SEARCH SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Search Articles");
            System.out.println("2. Read Article");
            System.out.println("3. Search History");
            System.out.println("4. Recently Viewed");
            System.out.println("5. Add Bookmark");
            System.out.println("6. Show Bookmarks");
            System.out.println("7. Discover Related Articles");
            System.out.println("8. Display Articles");
            System.out.println("0. Exit");
            System.out.print("Enter Choice: ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Please Enter A Number.");
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Exiting Program...");
                    return;

                case 1: {
                    System.out.print("Enter Search Query: ");
                    String query = scanner.nextLine();

                    if (query.isEmpty()) {
                        System.out.println("Search Query Cannot Be Empty");
                        break;
                    }

                    system.search(query);
                    break;
                }

                case 2: {
                    Integer id = readArticleId(scanner);
                    if (id != null) {
                        system.readArticle(id);
                    }
                    break;
                }

                case 3:
                    system.showHistory();
                    break;

                case 4:
                    system.showRecentArticles();
                    break;

                case 5: {
                    Integer id = readArticleId(scanner);
                    if (id != null) {
                        system.addBookmark(id);
                    }
                    break;
                }

                case 6:
                    system.showBookmarks();
                    break;

                case 7: {
                    System.out.print("Enter Article ID: ");
                    System.out.println("\nRelated Articles");

                    String idLine = scanner.nextLine().trim();
                    try {
                        int id = Integer.parseInt(idLine);
                        system.exploreTopics(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Article ID");
                    }
                    break;
                }

                case 8:
                    system.displayArticles();
                    break;

                default:
                    System.out.println("Invalid Choice. Please Select Between 0 And 8.");
                    break;
            }
        }
    }

    private static Integer readArticleId(Scanner scanner) {
        System.out.print("Enter Article ID: ");
        String idLine = scanner.nextLine().trim();

        try {
            return Integer.parseInt(idLine);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Article ID");
            return null;
        }
    }
}

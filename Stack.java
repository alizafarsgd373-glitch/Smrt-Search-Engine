package structures;

import java.util.ArrayList;

/**
 * Custom LIFO stack of Strings, matching the interface used by
 * SmartSearchSystem's searchHistory (push / empty / display).
 */
public class Stack {

    private final ArrayList<String> data;

    public Stack() {
        data = new ArrayList<>();
    }

    public void push(String value) {
        data.add(value);
    }

    public String pop() {
        if (empty()) {
            return null;
        }
        return data.remove(data.size() - 1);
    }

    public String top() {
        if (empty()) {
            return null;
        }
        return data.get(data.size() - 1);
    }

    public boolean empty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    /**
     * Prints the stack from most recent to least recent (LIFO order),
     * matching typical C++ stack display behavior.
     */
    public void display() {
        for (int i = data.size() - 1; i >= 0; i--) {
            System.out.println(data.get(i));
        }
    }
}

package structures;

import java.util.ArrayList;

/**
 * Custom FIFO queue of ints, matching the interface used by
 * SmartSearchSystem's recentArticles and the BFS in recommendArticles
 * (enqueue / dequeue / front / empty / size / getData).
 */
public class Queue {

    private final ArrayList<Integer> data;

    public Queue() {
        data = new ArrayList<>();
    }

    public void enqueue(int value) {
        data.add(value);
    }

    public int dequeue() {
        if (empty()) {
            return -1;
        }
        return data.remove(0);
    }

    public int front() {
        if (empty()) {
            return -1;
        }
        return data.get(0);
    }

    public boolean empty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    /**
     * Returns a copy of the queue's contents in FIFO order
     * (used by showRecentArticles).
     */
    public ArrayList<Integer> getData() {
        return new ArrayList<>(data);
    }
}

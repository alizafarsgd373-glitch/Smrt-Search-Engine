package structures;

import java.util.ArrayList;

/**
 * Custom max-heap priority queue storing {score, articleId} pairs,
 * ordered so the highest score is always at the top. Matches the
 * push / top / pop / empty interface used in SmartSearchSystem.search().
 */
public class PriorityQueue {

    private final ArrayList<int[]> heap;

    public PriorityQueue() {
        heap = new ArrayList<>();
    }

    /**
     * @param pair a 2-element array: {score, articleId}
     */
    public void push(int[] pair) {
        heap.add(pair);
        int i = heap.size() - 1;

        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(parent)[0] < heap.get(i)[0]) {
                swap(parent, i);
                i = parent;
            } else {
                break;
            }
        }
    }

    /**
     * @return the {score, articleId} pair with the highest score
     */
    public int[] top() {
        return heap.get(0);
    }

    public void pop() {
        int lastIndex = heap.size() - 1;
        heap.set(0, heap.get(lastIndex));
        heap.remove(lastIndex);

        int i = 0;
        int n = heap.size();

        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (left < n && heap.get(left)[0] > heap.get(largest)[0]) {
                largest = left;
            }
            if (right < n && heap.get(right)[0] > heap.get(largest)[0]) {
                largest = right;
            }
            if (largest == i) {
                break;
            }

            swap(i, largest);
            i = largest;
        }
    }

    public boolean empty() {
        return heap.isEmpty();
    }

    private void swap(int i, int j) {
        int[] temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}

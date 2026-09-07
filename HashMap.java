package structures;

import java.util.ArrayList;

/**
 * Custom hash table with separate chaining, mapping each word to the
 * frequency it appears in every article. Matches the insert / getFrequency
 * interface used by SmartSearchSystem.
 *
 * insert(word, articleId): increments the count of `word` for that article.
 * getFrequency(word): returns an array indexed by article id, where each
 * slot holds how many times `word` appears in that article (0 if absent).
 */
public class HashMap {

    private static final int TABLE_SIZE = 211; // prime bucket count

    private static class Entry {
        String word;
        ArrayList<Integer> articleIds = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        Entry next;

        Entry(String word) {
            this.word = word;
        }
    }

    private final Entry[] table;
    private int articleCount;

    @SuppressWarnings("unchecked")
    public HashMap() {
        table = new Entry[TABLE_SIZE];
        articleCount = 0;
    }

    private int hash(String word) {
        int hashValue = 0;
        for (int i = 0; i < word.length(); i++) {
            hashValue = (hashValue * 31 + word.charAt(i)) % TABLE_SIZE;
        }
        return Math.floorMod(hashValue, TABLE_SIZE);
    }

    public void insert(String word, int articleId) {
        if (articleId + 1 > articleCount) {
            articleCount = articleId + 1;
        }

        int idx = hash(word);
        Entry current = table[idx];

        while (current != null) {
            if (current.word.equals(word)) {
                int pos = current.articleIds.indexOf(articleId);
                if (pos == -1) {
                    current.articleIds.add(articleId);
                    current.counts.add(1);
                } else {
                    current.counts.set(pos, current.counts.get(pos) + 1);
                }
                return;
            }
            current = current.next;
        }

        Entry entry = new Entry(word);
        entry.articleIds.add(articleId);
        entry.counts.add(1);
        entry.next = table[idx];
        table[idx] = entry;
    }

    /**
     * @return array of size articleCount; freq[i] = occurrences of `word`
     *         in article i (0 if the word never appears in that article)
     */
    public int[] getFrequency(String word) {
        int[] freq = new int[articleCount];
        int idx = hash(word);
        Entry current = table[idx];

        while (current != null) {
            if (current.word.equals(word)) {
                for (int i = 0; i < current.articleIds.size(); i++) {
                    freq[current.articleIds.get(i)] = current.counts.get(i);
                }
                return freq;
            }
            current = current.next;
        }

        return freq; // word not found -> all zeros
    }
}

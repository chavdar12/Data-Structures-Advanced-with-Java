import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        List<String> words =
                Arrays.stream(reader.readLine().split(",\\s"))
                        .collect(Collectors.toList());

        String target = reader.readLine();

        words = words
                .stream()
                .filter(target::contains)
                .collect(Collectors.toList());

        Map<Integer, TreeSet<String>> tree = new LinkedHashMap<>();
        Map<String, Integer> wordsWithCount = new LinkedHashMap<>();

        for (String word : words) {
            int index = target.indexOf(word);

            wordsWithCount.putIfAbsent(word, 0);
            wordsWithCount.put(word, wordsWithCount.get(word) + 1);

            while (index != -1) {

                if (!tree.containsKey(index)) {
                    tree.put(index, new TreeSet<>());
                }

                tree.get(index).add(word);
                index = target.indexOf(word, index + 1);
            }
        }

        combine(0, target, tree, wordsWithCount, new LinkedHashMap<>());
    }

    private static void combine(Integer index,
                                String target,
                                Map<Integer, TreeSet<String>> tree,
                                Map<String, Integer> wordsWithCount,
                                Map<Integer, String> result) {

        if (index >= target.length()) {
            System.out.println(String.join(" ", result.values()));
        } else {

            if (tree.containsKey(index)) {

                for (String wordsAtIndex : tree.get(index)) {

                    if (wordsWithCount.get(wordsAtIndex) > 0) {

                        result.putIfAbsent(index, wordsAtIndex);
                        wordsWithCount.put(wordsAtIndex, wordsWithCount.get(wordsAtIndex) - 1);

                        combine(index + wordsAtIndex.length(),
                                target,
                                tree,
                                wordsWithCount,
                                result);

                        wordsWithCount.put(wordsAtIndex, wordsWithCount.get(wordsAtIndex) + 1);
                        result.remove(index, wordsAtIndex);
                    }
                }
            }
        }
    }
}

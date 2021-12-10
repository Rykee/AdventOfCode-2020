package rykee.net.solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Task01Solver {

    public Map<Integer, Integer> solve(List<Integer> input, Integer numberCount) {
        System.out.println("Solving first task with input:");
        System.out.println(Arrays.toString(input.toArray()));
        NavigableMap<Integer, Integer> occurrences = input.stream()
                .collect(Collectors.toMap(Function.identity(), key -> 1, Integer::sum, () -> new TreeMap<>(Integer::compareTo)));
        return Optional.ofNullable(getSolutionForValue(0, 2020, occurrences, numberCount, new HashMap<>()))
                .orElse(null);
    }

    private Map<Integer, Integer> getSolutionForValue(int currentSum, int remainingValue, NavigableMap<Integer, Integer> lowerEntries, Integer numberCount, Map<Integer, Integer> solutionWithCount) {
        if (numberCount == 1) {
            if (lowerEntries.containsKey(remainingValue) && doesOccurEnoughTimes(remainingValue, solutionWithCount, lowerEntries.get(remainingValue))) {
                putOrIncreaseCount(remainingValue, solutionWithCount);
                return solutionWithCount;
            } else {
                return null;
            }
        } else {
            return lowerEntries.keySet().stream()
                    .filter(value -> doesOccurEnoughTimes(value, solutionWithCount, lowerEntries.get(value)))
                    .map(value -> {
                        Map<Integer, Integer> currentSolution = new HashMap<>(solutionWithCount);
                        putOrIncreaseCount(value, currentSolution);
                        return getSolutionForValue(currentSum + value, remainingValue - value, lowerEntries.headMap(remainingValue, true), numberCount - 1, currentSolution);
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
    }

    private boolean doesOccurEnoughTimes(int value, Map<Integer, Integer> occurrences, int limit) {
        return occurrences.get(value) == null || limit > occurrences.get(value);
    }

    private void putOrIncreaseCount(int value, Map<Integer, Integer> solutionWithCount) {
        solutionWithCount.computeIfPresent(value, (key, oldValue) -> oldValue + 1);
        solutionWithCount.putIfAbsent(value, 1);
    }

}

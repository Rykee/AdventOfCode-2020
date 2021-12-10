package rykee.net.solver.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class TreeUtils {

    public Map<Long, Integer> getSumParts(int currentSum, int remainingValue, NavigableMap<Integer, Integer> lowerEntries, Integer numberCount, Map<Long, Integer> solutionWithCount) {
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
                        Map<Long, Integer> currentSolution = new HashMap<>(solutionWithCount);
                        putOrIncreaseCount(value, currentSolution);
                        return getSumParts(currentSum + value, remainingValue - value, lowerEntries.headMap(remainingValue, true), numberCount - 1, currentSolution);
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
    }

    public Set<Long> getSumPartsWithoutDuplication(long currentSum, long remainingValue, NavigableSet<Long> lowerEntries, Integer numberCount, Set<Long> solution) {
        if (numberCount == 1) {
            if (lowerEntries.contains(remainingValue)) {
                solution.add(remainingValue);
                return solution;
            } else {
                return null;
            }
        } else {
            return lowerEntries.stream()
                    .map(number -> {
                        Set<Long> currentSolution = new HashSet<>(solution);
                        currentSolution.add(number);
                        return getSumPartsWithoutDuplication(currentSum + number, remainingValue - number, lowerEntries.headSet(remainingValue, false), numberCount - 1, currentSolution);
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
    }

    private boolean doesOccurEnoughTimes(long value, Map<Long, Integer> occurrences, long limit) {
        return occurrences.get(value) == null || limit > occurrences.get(value);
    }

    private void putOrIncreaseCount(long value, Map<Long, Integer> solutionWithCount) {
        solutionWithCount.computeIfPresent(value, (key, oldValue) -> oldValue + 1);
        solutionWithCount.putIfAbsent(value, 1);
    }

}

package rykee.net.solver;

import rykee.net.solver.util.TreeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Task09Solver {

    public long getFirstInvalidNumber(List<Long> allNumbers) {
        List<Long> preamble = new ArrayList<>(allNumbers.subList(0, 25));
        for (int i = 25; i < allNumbers.size(); i++) {
            if (TreeUtils.getSumPartsWithoutDuplication(0, allNumbers.get(i), new TreeSet<>(preamble), 2, new HashSet<>()) == null) {
                return allNumbers.get(i);
            } else {
                preamble.remove(0);
                preamble.add(allNumbers.get(i));
            }
        }
        return -1;
    }

    private List<Long> parseToLong(List<String> lines) {
        return lines.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public List<Long> getNumbersOfSum(List<String> lines) {
        List<Long> allNumbers = parseToLong(lines);
        long invalidNumber = getFirstInvalidNumber(allNumbers);
        int sum = 0;
        int lowerIndex = 0;
        int upperIndex = 0;
        while (sum != invalidNumber) {
            if (sum < invalidNumber) {
                sum += allNumbers.get(upperIndex);
                upperIndex++;
            } else {
                sum -= allNumbers.get(lowerIndex);
                lowerIndex++;
            }
        }
        return allNumbers.subList(lowerIndex, upperIndex + 1);
    }
}

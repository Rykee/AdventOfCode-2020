package rykee.net.solver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Task03Solver {

    private Queue<Integer> nextPositionGenerator;


    public long getNumberOfTrees(List<String> input, int increment, int slope) {
        setupGenerator(increment, input.get(0).length() - 1);
        long count = 0;
        for (int i = 0; i < input.size(); i += slope) {
            String line = input.get(i);
            if (line.charAt(getNextIndex()) == '#') {
                count++;
            }
        }
        return count;
    }

    private void setupGenerator(int increment, int lastIndex) {
        nextPositionGenerator = new LinkedList<>();
        Set<Integer> foundNumbers = new HashSet<>();
        int currentIndex = 0;
        while (!foundNumbers.contains(currentIndex)) {
            int diff = lastIndex - currentIndex;
            if (diff < 0) {
                currentIndex = Math.abs(diff) - 1;
                if (foundNumbers.contains(currentIndex)) {
                    break;
                }
            }
            nextPositionGenerator.add(currentIndex);
            foundNumbers.add(currentIndex);
            currentIndex += increment;
        }
    }

    private int getNextIndex() {
        int nextIndex = nextPositionGenerator.poll();
        nextPositionGenerator.add(nextIndex);
        return nextIndex;
    }

}



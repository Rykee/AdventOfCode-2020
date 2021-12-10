package rykee.net.solver;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Task06Solver {

    public int getYesSum(String wholeFile) {
        String[] groups = wholeFile.split("\r\n\r\n");
        return Arrays.stream(groups)
                .map(answers -> answers.replaceAll("\r\n|\s*", "").trim().chars()
                        .boxed()
                        .collect(Collectors.toSet())
                        .size())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int getSameVoteCount(String wholeFile) {
        String[] groups = wholeFile.split("\r\n\r\n");
        return Arrays.stream(groups)
                .map(answers -> Arrays.stream(answers.split("\r\n"))
                        .map(vote -> vote.trim().chars().boxed().collect(Collectors.toSet())))
                .map(votesPerGroup -> votesPerGroup
                        .reduce(this::getIntersection)
                        .orElseThrow(() -> new RuntimeException("huh"))
                        .size())
                .mapToInt(Integer::intValue)
                .sum();
    }

    private Set<Integer> getIntersection(Set<Integer> vote1, Set<Integer> vote2) {
        vote1.retainAll(vote2);
        return vote1;
    }

}

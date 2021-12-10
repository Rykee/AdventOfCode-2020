package rykee.net.solver;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class Task16Solver {

    Map<Integer, TicketDataInfo> bounds = new HashMap<>();

    public long getErrorRate(String wholeFile) {
        String[] parts = wholeFile.split("\r\n\r\n");
        AtomicInteger counter = new AtomicInteger(0);
        Arrays.stream(parts[0].split("\r\n"))
                .forEach(line -> mapToBound(line, counter));
        List<Integer> yourTicket = Arrays.stream(parts[1].split("\r\n")[1].split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<List<Integer>> otherTickets = Arrays.stream(parts[2].split("\r\n"))
                .skip(1)
                .map(s -> Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList()))
                .collect(Collectors.toList());
        Map<Integer, List<Integer>> possibleArrangements = initialize(bounds.size());

        IntPredicate everyOption = bounds.values().stream()
                .flatMap(ticketDataInfo -> ticketDataInfo.getBounds().stream())
                .reduce(IntPredicate::or)
                .orElseThrow();
        otherTickets = otherTickets.stream()
                .filter(ticketData ->
                        ticketData.stream()
                                .allMatch(everyOption::test))
                .collect(Collectors.toList());

        for (AtomicInteger i = new AtomicInteger(0); i.get() < bounds.size(); i.incrementAndGet()) {
            List<Integer> column = otherTickets.stream().map(values -> values.get(i.get())).collect(Collectors.toList());
            for (int j = 0; j < bounds.size(); j++) {
                if (column.stream().mapToInt(Integer::intValue).allMatch(bounds.get(j).getAccumulatedPredicate())) {
                    possibleArrangements.get(j).add(i.get());
                }
            }
        }
        Set<Integer> foundIndexes = new HashSet<>();
        Integer boundIndex = getColumnWithOnePossibility(possibleArrangements, foundIndexes);
        while (boundIndex != null) {
            Integer matchingColumn = possibleArrangements.get(boundIndex).get(0);
            for (int i = 0; i < possibleArrangements.size(); i++) {
                if (i == boundIndex) {
                    continue;
                }
                possibleArrangements.get(i).remove(matchingColumn);
            }
            foundIndexes.add(boundIndex);
            boundIndex = getColumnWithOnePossibility(possibleArrangements, foundIndexes);
        }
        List<Integer> neededIndexes = bounds.entrySet().stream()
                .filter(entry -> entry.getValue().getName().startsWith("departure"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return neededIndexes.stream()
                .map(index -> possibleArrangements.get(index).get(0))
                .map(yourTicket::get)
                .map(Long::valueOf)
                .reduce(1L, (integer, integer2) -> integer * integer2);
    }

    private Integer getColumnWithOnePossibility(Map<Integer, List<Integer>> possibleArrangements, Set<Integer> foundIndexes) {
        return possibleArrangements.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1 && !foundIndexes.contains(entry.getKey()))
                .findAny()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Map<Integer, List<Integer>> initialize(int size) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(i, new ArrayList<>());
        }
        return map;
    }

    private void mapToBound(String line, AtomicInteger index) {
        String[] boundDef = line.split("\s*:\s*");
        List<IntPredicate> boundPredicates = Arrays.stream(boundDef[1].split("\s*or\s*"))
                .map(bound -> {
                    String[] split = bound.split("-");
                    return getPredicate(split[0], split[1]);
                }).collect(Collectors.toList());
        bounds.put(index.getAndIncrement(), new TicketDataInfo(boundDef[0], boundPredicates));
    }

    private IntPredicate getPredicate(String lowerBound, String upperBound) {
        return a -> a >= Integer.parseInt(lowerBound) && a <= Integer.parseInt(upperBound);
    }


    @Data
    private static class TicketDataInfo {
        private final String name;
        private final List<IntPredicate> bounds;

        public IntPredicate getAccumulatedPredicate() {
            return bounds.stream().reduce(IntPredicate::or).orElseThrow(() -> new RuntimeException("bruh"));
        }

    }

}

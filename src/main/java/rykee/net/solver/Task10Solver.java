package rykee.net.solver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

public class Task10Solver {

    public static <T> Stream<T> flatten(T element, Function<T, Collection<T>> mapper) {
        Collection<T> collection = mapper.apply(element);
        if (collection.isEmpty()) {
            return Stream.of(element);
        } else {
            return Stream.concat(
                    Stream.of(element),
                    mapper.apply(element).stream().flatMap(childElement -> flatten(childElement, mapper)));
        }
    }

    public int get1Jolt3JoltDiffMultiplied(List<String> lines) {
        Map<Integer, Integer> diffs = new HashMap<>();
        Integer minValue = lines.stream()
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder())
                .reduce((adapter1, adapter2) -> {
                    int diff = adapter1 - adapter2;
                    addOrIncrement(diffs, diff);
                    return adapter2;
                })
                .orElseThrow();
        addOrIncrement(diffs, minValue);
        diffs.put(3, diffs.get(3) + 1);
        return diffs.getOrDefault(1, 0) * diffs.getOrDefault(3, 0);
    }

    public long possibleConfigurations(List<String> lines) {
        NavigableMap<Integer, Adapter> adapters = new TreeMap<>(Integer::compareTo);
        Map<Integer, Long> twoThreeOccurrences = new HashMap<>();
        twoThreeOccurrences.put(2, 0L);
        twoThreeOccurrences.put(3, 0L);
        lines.stream()
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder())
                .forEach(joltValue -> {
                    Adapter adapter = new Adapter(joltValue);
                    adapters.put(joltValue, adapter);
                    for (int i = 1; i <= 3; i++) {
                        Adapter connectable = adapters.get(i + joltValue);
                        if (connectable != null) {
                            connectable.getCanConnect().add(adapter);
                        }
                    }
                    int connections = adapter.getCanConnect().size();
                    if (connections >= 2) {
                        twoThreeOccurrences.computeIfPresent(connections, (key, oldValue) -> oldValue + 1);
                    }
                });
        adapters.values().forEach(this::calculateWays);
        return adapters.lastEntry().getValue().getThiIsThisWay();
    }

    private void calculateWays(Adapter adapter) {
        if (adapter.getCanConnect().isEmpty()) {
            adapter.setThiIsThisWay(1);
        } else {
            adapter.setThiIsThisWay(adapter.getCanConnect().stream()
                    .map(Adapter::getThiIsThisWay)
                    .mapToLong(Long::longValue)
                    .sum());
        }
    }

    private void addOrIncrement(Map<Integer, Integer> diffs, int diff) {
        diffs.computeIfPresent(diff, (key, oldValue) -> oldValue + 1);
        diffs.putIfAbsent(diff, 1);
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class Adapter implements Comparable<Adapter> {
        private final long value;
        private final List<Adapter> canConnect = new ArrayList<>();
        private long thiIsThisWay = 0;

        @Override
        public int compareTo(Adapter o) {
            return Comparator.comparing(Adapter::getValue).compare(o, this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Adapter adapter = (Adapter) o;
            return getValue() == adapter.getValue();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getValue());
        }
    }
}

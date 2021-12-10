package rykee.net.solver;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Task13Solver {
    public long getBusIdMultipliedByWaitTime(List<String> lines) {
        long travelTime = Long.parseLong(lines.get(0));
        //n mod busId --> n + busid-n mod busId

        return Arrays.stream(lines.get(1).split(","))
                .filter(s -> !s.equals("x"))
                .map(Long::parseLong)
                .map(busId -> {
                    long distance = travelTime % busId;
                    if (distance == 0) {
                        return new Bus01(travelTime, busId);
                    }
                    return new Bus01(travelTime + busId - distance, busId);
                })
                .min(Bus01::compareTo)
                .map(bus01 -> (bus01.departureTime - travelTime) * bus01.busId)
                .orElseThrow();
    }

    public long getEarliestTimestamp(List<String> lines) {
        AtomicInteger index = new AtomicInteger(0);
        List<Bus02> buses = Arrays.stream(lines.get(1).split(","))
                .filter(s -> {
                    if (s.equals("x")) {
                        index.incrementAndGet();
                    }
                    return !s.equals("x");
                })
                .map(s -> new Bus02(Long.parseLong(s), index.getAndIncrement()))
                .collect(Collectors.toList());

        Bus02 startingBus = buses.get(0);
        long time = startingBus.getBusId();
        long period = startingBus.getBusId();
        buses.remove(0);
        for (Bus02 bus02 : buses) {
            while ((time + bus02.delay) % bus02.busId != 0) {
                time += period;
            }
            period *= bus02.busId;
        }
        return time;
    }

    @Data
    private static class Bus01 implements Comparable<Bus01> {
        final long departureTime;
        final long busId;

        @Override
        public int compareTo(Bus01 o) {
            return Long.compare(departureTime, o.departureTime);
        }
    }

    @Data
    private static class Bus02 {
        final long busId;
        final long delay;
    }
}

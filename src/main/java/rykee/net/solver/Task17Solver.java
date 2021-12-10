package rykee.net.solver;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Task17Solver {

    private static final List<Coordinate> NEIGHBOUR_VECTORS = generateCartesianProduct(4);
    private static final Map<Coordinate, List<Coordinate>> ADJACENCY_MAP = Collections.synchronizedMap(new HashMap<>());

    private static List<Coordinate> generateCartesianProduct(int dimension) {
        List<Coordinate> baseCoordinates = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            baseCoordinates.add(new Coordinate(i));
        }
        List<Coordinate> cartesianProduct = new ArrayList<>(baseCoordinates);
        for (int i = 0; i < dimension - 1; i++) {
            cartesianProduct = createCartesianProduct(cartesianProduct, baseCoordinates);
        }
        removeNullVector(dimension, cartesianProduct);
        return cartesianProduct;
    }

    private static void removeNullVector(int dimension, List<Coordinate> cartesianProduct) {
        Map<Integer, Integer> vector0Point = new HashMap<>();
        for (int i = 1; i <= dimension; i++) {
            vector0Point.put(i, 0);
        }
        Coordinate coordinate = new Coordinate(0);
        coordinate.setCoordinateInfo(vector0Point);
        cartesianProduct.remove(coordinate);
    }

    private static List<Coordinate> createCartesianProduct(List<Coordinate> coordinates, List<Coordinate> otherCoordinates) {
        return coordinates.stream()
                .flatMap(coord -> otherCoordinates.stream()
                        .map(otherCoordinate -> {
                            Coordinate coordinate = new Coordinate(coord);
                            coordinate.putAxis(coord.getCoordinateInfo().size() + 1, otherCoordinate.coordinateInfo.get(1));
                            return coordinate;
                        }))
                .collect(Collectors.toList());
    }

    private static List<Coordinate> getOrGenerateNeighbours(Coordinate coordinate) {
        return NEIGHBOUR_VECTORS.stream().map(coordinate::applyVector).collect(Collectors.toList());
        //ADJACENCY_MAP.putIfAbsent(coordinate, NEIGHBOUR_VECTORS.stream().map(coordinate::applyVector).collect(Collectors.toList()));
        //return ADJACENCY_MAP.get(coordinate);
    }

    public long getActiveCubesAfter(List<String> lines, int iteration) {
        Set<Coordinate> activeCoordinates = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int c = 0; c < line.toCharArray().length; c++) {
                if (line.charAt(c) == '#') {
                    activeCoordinates.add(new Coordinate(c, -i, 0, 0));
                }
            }
        }

        for (int i = 0; i < iteration; i++) {
            Set<Coordinate> toDeactivate = activeCoordinates.stream()
                    .filter(coordinate -> {
                        long activeNeighbours = getActiveNeighbourCount(activeCoordinates, coordinate);
                        return activeNeighbours > 3 || activeNeighbours < 2;
                    }).collect(Collectors.toSet());
            Set<Coordinate> toActivate = getAllDeactivatedNeighboursForAllActive(activeCoordinates).stream()
                    .filter(coordinate -> getActiveNeighbourCount(activeCoordinates, coordinate) == 3)
                    .collect(Collectors.toSet());
            activeCoordinates.removeAll(toDeactivate);
            activeCoordinates.addAll(toActivate);
            System.out.println(activeCoordinates.size());
        }
        return activeCoordinates.size();
    }

    private Set<Coordinate> getAllDeactivatedNeighboursForAllActive(Set<Coordinate> activeCoordinates) {
        return activeCoordinates.stream()
                .flatMap(coordinate -> Task17Solver.getOrGenerateNeighbours(coordinate).stream())
                .filter(coordinate -> !activeCoordinates.contains(coordinate))
                .collect(Collectors.toSet());
    }

    private long getActiveNeighbourCount(Set<Coordinate> activeCoordinates, Coordinate coordinate) {
        return Task17Solver.getOrGenerateNeighbours(coordinate).stream()
                .filter(activeCoordinates::contains)
                .count();
    }

    @Data
    private static class Coordinate {

        protected Map<Integer, Integer> coordinateInfo = new HashMap<>();

        public Coordinate(int x, int y, int z) {
            coordinateInfo.put(1, x);
            coordinateInfo.put(2, y);
            coordinateInfo.put(3, z);
        }

        public Coordinate(int x, int y, int z, int w) {
            coordinateInfo.put(1, x);
            coordinateInfo.put(2, y);
            coordinateInfo.put(3, z);
            coordinateInfo.put(4, w);
        }

        public Coordinate(int x) {
            coordinateInfo.put(1, x);
        }

        private Coordinate(Coordinate coordinate) {
            coordinateInfo = new HashMap<>(coordinate.coordinateInfo);
        }

        private void putAxis(int dimension, int value) {
            coordinateInfo.put(dimension, value);
        }

        private Coordinate applyVector(Coordinate vector) {
            if (coordinateInfo.size() != vector.getCoordinateInfo().size()) {
                throw new IllegalArgumentException("Not the same dimension, where have you been vectoring bro");
            }
            Coordinate newCoordinate = new Coordinate(this);
            vector.getCoordinateInfo().forEach((key, value) -> newCoordinate.getCoordinateInfo().merge(key, value, Integer::sum));
            return newCoordinate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Coordinate that = (Coordinate) o;
            return getCoordinateInfo().equals(that.getCoordinateInfo());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCoordinateInfo());
        }
    }
}

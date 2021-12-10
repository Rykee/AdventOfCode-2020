package rykee.net.solver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class Task11Solver {

    public long getOccupiedSeatCount(List<String> lines) throws InterruptedException {
        SeatState[][] seating = new SeatState[lines.size()][lines.get(0).length()];
        AtomicInteger atomicInteger = new AtomicInteger(0);
        lines.stream()
                .map(s -> s.chars()
                        .boxed()
                        .map(character -> (char) character.intValue())
                        .map(SeatState::fromValue)
                        .collect(Collectors.toList())
                ).forEachOrdered(seatStates -> {
            int index = atomicInteger.getAndIncrement();
            seating[index] = seatStates.toArray(seating[index]);
        });
        boolean isChanged;
        SeatState[][] endState = deepCopy(seating);
        SeatState[][] currentState = deepCopy(seating);

        do {
            isChanged = false;
            for (int i = 0; i < currentState.length; i++) {
                for (int j = 0; j < currentState[i].length; j++) {
                    if (currentState[i][j] == SeatState.FLOOR) {
                        continue;
                    }
                    SeatState newState = switch (currentState[i][j]) {
                        case OCCUPIED -> applyToOccupied(currentState, i, j);
                        case EMPTY -> applyToEmpty(currentState, i, j);
                        default -> currentState[i][j];
                    };
                    if (newState != endState[i][j]) {
                        isChanged = true;
                        endState[i][j] = newState;
                    }
                }
            }
            currentState = deepCopy(endState);
            //printCurrent(currentState);
        } while (isChanged);
        return Arrays.stream(endState)
                .flatMap(Arrays::stream)
                .filter(seatState -> seatState == SeatState.OCCUPIED)
                .count();
    }

    <T> T[][] deepCopy(T[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    private void printCurrent(SeatState[][] currentState) {
        Arrays.stream(currentState).forEach(seatStates -> {
            Arrays.stream(seatStates).map(SeatState::getValue).forEach(System.out::print);
            System.out.println();
        });
        System.out.println();
    }

    private SeatState applyToOccupied(SeatState[][] currentState, int i, int j) {
        if (getOccupiedNeighbours(currentState, i, j) >= 5) {
            return SeatState.EMPTY;
        } else {
            return SeatState.OCCUPIED;
        }
    }

    private int getOccupiedNeighbours(SeatState[][] currentState, int i, int j) {
        int counter = 0;

        counter += checkInDirection(currentState, i, j, row -> row + 1, column -> column);//lefelé
        counter += checkInDirection(currentState, i, j, row -> row - 1, column -> column);//felfelé
        counter += checkInDirection(currentState, i, j, row -> row - 1, column -> column + 1);//jobbra le
        counter += checkInDirection(currentState, i, j, row -> row - 1, column -> column - 1); // balra le
        counter += checkInDirection(currentState, i, j, row -> row, column -> column + 1); //jobbra
        counter += checkInDirection(currentState, i, j, row -> row, column -> column - 1); //balra
        counter += checkInDirection(currentState, i, j, row -> row + 1, column -> column + 1); //jobbra fel
        counter += checkInDirection(currentState, i, j, row -> row + 1, column -> column - 1); // balra fel

        return counter;
    }

    private int checkInDirection(SeatState[][] seatStates, int i, int j, IntUnaryOperator rowMapper, IntUnaryOperator columnMapper) {
        i = rowMapper.applyAsInt(i);
        j = columnMapper.applyAsInt(j);
        while (i >= 0 && i < seatStates.length && j >= 0 && j < seatStates[i].length) {
            if (seatStates[i][j] == SeatState.OCCUPIED) {
                return 1;
            } else if (seatStates[i][j] == SeatState.EMPTY) {
                return 0;
            }
            i = rowMapper.applyAsInt(i);
            j = columnMapper.applyAsInt(j);
        }
        return 0;
    }

    private SeatState applyToEmpty(SeatState[][] currentState, int i, int j) {
        if (getOccupiedNeighbours(currentState, i, j) == 0) {
            return SeatState.OCCUPIED;
        } else {
            return SeatState.EMPTY;
        }
    }

    private enum SeatState {
        OCCUPIED('#'),
        EMPTY('L'),
        FLOOR('.');

        private final char value;

        SeatState(char c) {
            value = c;
        }

        public static SeatState fromValue(char c) {
            for (SeatState seatState : values()) {
                if (seatState.value == c) {
                    return seatState;
                }
            }
            return null;
        }

        public char getValue() {
            return value;
        }
    }

}

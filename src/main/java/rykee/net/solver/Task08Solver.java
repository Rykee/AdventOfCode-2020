package rykee.net.solver;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Task08Solver {

    public int getAccumulatedValue(List<String> lines) {
        AtomicInteger counter = new AtomicInteger(0);
        List<Command> commands = lines.stream()
                .map(command -> {
                    String[] parts = command.split("\s+");
                    Operation operation = Operation.fromValue(parts[0]);
                    return new Command(counter.getAndIncrement(), operation, Integer.parseInt(parts[1]));
                })
                .collect(Collectors.toList());
        return getAccumulate(commands);
    }

    private int getAccumulate(List<Command> commands) {
        int currentAccumulate = 0;
        int currentIndex = 0;
        LinkedList<Command> visitedCommands = new LinkedList<>();
        Set<Integer> visitedIndexes = new HashSet<>();
        int lastJmpNopToChange = 0;
        boolean isFirstTry = true;
        while (currentIndex != commands.size() - 1) {
            if (visitedIndexes.contains(currentIndex)) {
                if (isFirstTry) {
                    lastJmpNopToChange = findLastJmpOrNop(visitedCommands, visitedCommands.size());
                    isFirstTry = false;
                    switchOperation(visitedCommands.get(lastJmpNopToChange));
                } else {
                    switchOperation(visitedCommands.get(lastJmpNopToChange));
                    lastJmpNopToChange = findLastJmpOrNop(visitedCommands, lastJmpNopToChange);
                }
                currentIndex = visitedCommands.get(lastJmpNopToChange).getRowNumber();
                currentAccumulate = backtrack(visitedCommands.subList(lastJmpNopToChange, visitedCommands.size()), visitedIndexes, currentAccumulate);
            } else {
                Command command = commands.get(currentIndex);
                visitedIndexes.add(currentIndex);
                visitedCommands.add(command);
                if (command.getOperation() == Operation.ACCUMULATE) {
                    currentAccumulate += command.getParameter();
                }
                if (command.getOperation() == Operation.JUMP) {
                    currentIndex += command.getParameter() - 1;
                }
                currentIndex++;
            }
        }
        return currentAccumulate;
    }

    private void switchOperation(Command command) {
        if (command.getOperation() == Operation.JUMP) {
            command.setOperation(Operation.NOP);
        } else {
            command.setOperation(Operation.JUMP);
        }
    }

    private int backtrack(List<Command> commands, Set<Integer> visitedIndexes, int currentAccumulate) {
        for (Command command : commands) {
            visitedIndexes.remove(command.getRowNumber());
            if (command.getOperation() == Operation.ACCUMULATE) {
                currentAccumulate -= command.getParameter();
            }
        }
        commands.clear();
        return currentAccumulate;
    }

    private int findLastJmpOrNop(LinkedList<Command> visitedCommands, int upperBound) {
        for (int i = upperBound - 1; i >= 0; i--) {
            Operation operation = visitedCommands.get(i).getOperation();
            if (operation != Operation.ACCUMULATE) {
                return i;
            }
        }
        return -1;
    }

    private enum Operation {
        ACCUMULATE("acc"),
        JUMP("jmp"),
        NOP("nop");

        private final String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        public static Operation fromValue(String operation) {
            return Arrays.stream(values())
                    .filter(op -> op.operation.equals(operation))
                    .findAny()
                    .orElse(null);
        }
    }

    @Getter
    @Setter
    private static class Command {

        private final int rowNumber;
        private final int parameter;
        private Operation operation;

        private Command(int rowNumber, Operation operation, int parameter) {
            this.rowNumber = rowNumber;
            this.operation = operation;
            this.parameter = parameter;
        }

    }

}

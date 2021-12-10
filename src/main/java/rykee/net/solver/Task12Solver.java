package rykee.net.solver;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Task12Solver {

    public long getManhattanDistance(List<String> lines) {
        List<Instruction> instructions = lines.stream()
                .map(s -> new Instruction(InstructionType.fromValue(s.charAt(0)), Integer.parseInt(s.substring(1))))
                .collect(Collectors.toList());
        ShipRoute shipRoute = new ShipRoute();
        Coordinate finishingPosition = shipRoute.applyInstructions(instructions);
        return (long) Math.abs(finishingPosition.x) + Math.abs(finishingPosition.y);
    }

    //(currentAngle, angle) -> normalizeAngle(currentAngle - angle)
    private enum InstructionType {
        NORTH('N'),
        SOUTH('S'),
        EAST('E'),
        WEST('W'),
        LEFT('L'),
        RIGHT('R'),
        FORWARD('F');

        private final char instruction;

        InstructionType(char instruction) {
            this.instruction = instruction;
        }


        private static InstructionType fromValue(char value) {
            return Arrays.stream(values())
                    .filter(instructionType -> instructionType.instruction == value)
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Bruh"));
        }

    }

    @AllArgsConstructor
    private static class Instruction {
        InstructionType instructionType;
        int value;
    }

    @AllArgsConstructor
    private static class Coordinate {
        int x;
        int y;

        private Coordinate sum(InstructionType instructionType, int value) {
            return switch (instructionType) {
                case NORTH -> new Coordinate(x, y + value);
                case SOUTH -> new Coordinate(x, y - value);
                case EAST -> new Coordinate(x + value, y);
                case WEST -> new Coordinate(x - value, y);
                default -> throw new IllegalArgumentException("Not a valid intruction for sum");
            };
        }

        private Coordinate applyVectorNTimes(Coordinate vector, int n) {
            return new Coordinate(x + vector.x * n, y + vector.y * n);
        }
    }

    private static class ShipRoute {

        private Coordinate shipPosition;
        private Coordinate waypointPosition;

        public ShipRoute() {
            shipPosition = new Coordinate(0, 0);
            waypointPosition = new Coordinate(10, 1);
        }

        private static Integer normalizeAngle(int angle) {
            if (angle >= 0 && angle < 360) {
                return angle;
            } else {
                if (angle >= 360) {
                    return angle % 360;
                } else {
                    return (angle + (angle * -1 / 360 + 1) * 360) % 360;
                }
            }
        }

        public Coordinate applyInstructions(List<Instruction> instructions) {
            System.out.println("Starting rout!");
            for (Instruction instruction : instructions) {
                InstructionType instructionType = instruction.instructionType;
                System.out.println("After instruction: " + instructionType + instruction.value);
                switch (instructionType) {
                    case NORTH, SOUTH, EAST, WEST -> {
                        waypointPosition = waypointPosition.sum(instructionType, instruction.value);
                        printCurrentPos();
                    }
                    case LEFT, RIGHT -> {
                        int rightAngle = instructionType == InstructionType.LEFT ? normalizeAngle(-instruction.value) : normalizeAngle(instruction.value);
                        Coordinate vector = new Coordinate(waypointPosition.x - shipPosition.x, waypointPosition.y - shipPosition.y);
                        vector = switch (rightAngle) {
                            case 90 -> new Coordinate(vector.y, -1 * vector.x);
                            case 180 -> new Coordinate(-1 * vector.x, -1 * vector.y);
                            case 270 -> new Coordinate(-1 * vector.y, vector.x);
                            default -> throw new IllegalArgumentException("Not a valid rotation: " + rightAngle);
                        };
                        waypointPosition = shipPosition.applyVectorNTimes(vector, 1);
                        //90 y -x
                        //180 -x,-y
                        //270  -y,x

                        printCurrentPos();
                    }
                    case FORWARD -> {
                        Coordinate vector = new Coordinate(waypointPosition.x - shipPosition.x, waypointPosition.y - shipPosition.y);
                        waypointPosition = waypointPosition.applyVectorNTimes(vector, instruction.value);
                        shipPosition = shipPosition.applyVectorNTimes(vector, instruction.value);
                        printCurrentPos();
                    }
                }
            }
            return shipPosition;
        }

        private void printCurrentPos() {
            System.out.printf("Waypoint:{%d,%d}\t\tShip:{%d,%d}%n", waypointPosition.x, waypointPosition.y, shipPosition.x, shipPosition.y);
        }

    }

}

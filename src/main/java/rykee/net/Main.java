package rykee.net;

import lombok.Data;
import rykee.net.solver.Task01Solver;
import rykee.net.solver.Task02Solver;
import rykee.net.solver.Task03Solver;
import rykee.net.solver.Task04Solver;
import rykee.net.solver.Task05Solver;
import rykee.net.solver.Task06Solver;
import rykee.net.solver.Task07Solver;
import rykee.net.solver.Task08Solver;
import rykee.net.solver.Task09Solver;
import rykee.net.solver.Task10Solver;
import rykee.net.solver.Task11Solver;
import rykee.net.solver.Task12Solver;
import rykee.net.solver.Task13Solver;
import rykee.net.solver.Task14Solver;
import rykee.net.solver.Task15Solver;
import rykee.net.solver.Task16Solver;
import rykee.net.solver.Task17Solver;
import rykee.net.solver.model.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        //task01(getInputTextPath("task01_input.txt"));
        //task02(getInputTextPath("task02_input.txt"));
        //task03(getInputTextPath("task03_input.txt"));
        //task04(getInputTextPath("task04_input.txt"));
        //task05(getInputTextPath("task05_input.txt"));
        //task06(getInputTextPath("task06_input.txt"));
        //task07(getInputTextPath("task07_input.txt"));
        //task08(getInputTextPath("task08_input.txt"));
        //task09(getInputTextPath("task09_input.txt"));
        //task10(getInputTextPath("task10_input.txt"));
        //task11(getInputTextPath("task11_input.txt"));
        // task12(getInputTextPath("task12_input.txt"));
        //task13(getInputTextPath("task13_input.txt"));
        //task14(getInputTextPath("task14_input.txt"));
        //task15();
        //task16(getInputTextPath("task16_input.txt"));
        task17(getInputTextPath("task17_input.txt"));

    }

    private static void task17(Path inputTextPath) {
        Task17Solver task17Solver = new Task17Solver();
        System.out.println(task17Solver.getActiveCubesAfter(FileReader.getLines(inputTextPath), 6));
    }

    private static void task01(Path inputText) {
        List<Integer> testInput = FileReader.getLines(inputText).stream().map(Integer::parseInt).collect(Collectors.toList());
        Task01Solver task01Solver = new Task01Solver();
        long startTime = System.currentTimeMillis();
        Map<Integer, Integer> solution = task01Solver.solve(testInput, 15);
        System.out.printf("Time taken: %d%n", System.currentTimeMillis() - startTime);
        if (solution == null) {
            System.out.println("No solution!");
        } else {
            StringJoiner sj = new StringJoiner(", ");
            solution.forEach((key, value) -> {
                for (int i = 0; i < value; i++) {
                    sj.add(key.toString());
                }
            });
            System.out.println("Found numbers: " + sj.toString());
            System.out.println("Solution: " + calculateSum(solution));
        }
    }

    private static void task02(Path inputText) {
        Task02Solver task02Solver = new Task02Solver(inputText);
        System.out.println(task02Solver.getValidPasswordCount());
    }

    private static void task03(Path inputTextPath) {
        Task03Solver task03Solver = new Task03Solver();
        List<String> lines = FileReader.getLines(inputTextPath);
        System.out.println("Task 3 Part 1:");
        System.out.println(task03Solver.getNumberOfTrees(lines, 3, 1));

        System.out.println("Task 3 Part2:");
        List<SlopeData> slopeData = createSlopeData();
        long result = slopeData.stream()
                .mapToLong(data -> task03Solver.getNumberOfTrees(lines, data.getRight(), data.getDown()))
                .reduce((left, right) -> left * right)
                .orElse(-1);
        System.out.println(result);


    }

    private static void task04(Path inputTextPath) throws IOException {
        String wholeFile = Files.readString(inputTextPath);
        Task04Solver task04Solver = new Task04Solver();
        System.out.println(task04Solver.countValidPassports(wholeFile));
    }

    private static void task05(Path inputTextPath) {
        Task05Solver task05Solver = new Task05Solver();
        System.out.println("Task 05 Part 1:");
        System.out.println(task05Solver.calculateHighestId(FileReader.getLines(inputTextPath)));
        System.out.println("Task05 Part 2:");
        List<List<Character>> flightMap = task05Solver.getFlightMap(FileReader.getLines(inputTextPath));
        for (int i = 0; i < flightMap.size(); i++) {
            List<Character> characters = flightMap.get(i);
            if (i == 0) {
                System.out.print("|‾‾‾‾");
            }
            System.out.print("|");
            for (int j = 0; j < characters.size(); j++) {
                System.out.print(characters.get(j) + " ");
                if (j == 3) {
                    System.out.print("      ");
                }
            }
            System.out.println("|");
        }

        for (int i = 0; i < flightMap.size(); i++) {
            List<Character> characters = flightMap.get(i);
            for (int j = 1; j < characters.size() - 1; j++) {
                if (characters.get(j) == 'O' && characters.get(j - 1) == 'X' && characters.get(j + 1) == 'X') {
                    System.out.printf("Row: %d, Column: %d, ID: %d%n", i, j, i * 8 + j);
                    break;
                }
            }

        }

    }

    private static void task06(Path inputTextPath) throws IOException {
        Task06Solver task06Solver = new Task06Solver();
        System.out.println(task06Solver.getSameVoteCount(Files.readString(inputTextPath)));
    }

    private static void task07(Path inputTextPath) {
        Task07Solver task07Solver = new Task07Solver();
        System.out.println(task07Solver.getShinyContainedCount(FileReader.getLines(inputTextPath)));
    }

    private static void task08(Path inputTextPath) {
        Task08Solver task08Solver = new Task08Solver();
        System.out.println(task08Solver.getAccumulatedValue(FileReader.getLines(inputTextPath)));
    }

    private static void task09(Path inputTextPath) {
        Task09Solver task09Solver = new Task09Solver();
        List<Long> numbersOfSum = task09Solver.getNumbersOfSum(FileReader.getLines(inputTextPath));
        System.out.println(numbersOfSum);
    }

    private static void task10(Path inputTextPath) {
        Task10Solver task10Solver = new Task10Solver();
        System.out.println(task10Solver.get1Jolt3JoltDiffMultiplied(FileReader.getLines(inputTextPath)));
        long currentMillis = System.currentTimeMillis();
        System.out.println(task10Solver.possibleConfigurations(FileReader.getLines(inputTextPath)));
        System.out.println(System.currentTimeMillis() - currentMillis + " ms");
    }

    private static void task11(Path inputTextPath) throws InterruptedException {
        Task11Solver task11Solver = new Task11Solver();
        System.out.println(task11Solver.getOccupiedSeatCount(FileReader.getLines(inputTextPath)));
    }

    private static void task12(Path inputTextPath) {
        Task12Solver task12Solver = new Task12Solver();
        System.out.println(task12Solver.getManhattanDistance(FileReader.getLines(inputTextPath)));

    }

    private static void task13(Path inputTextPath) {
        Task13Solver task13Solver = new Task13Solver();
        System.out.println(task13Solver.getBusIdMultipliedByWaitTime(FileReader.getLines(inputTextPath)));
        System.out.println(task13Solver.getEarliestTimestamp(FileReader.getLines(inputTextPath)));

    }

    private static void task14(Path inputTextPath) {
        Task14Solver task14Solver = new Task14Solver();
        System.out.println(task14Solver.getLeftOverSum(FileReader.getLines(inputTextPath)));
    }

    private static void task15() {
        Task15Solver task15Solver = new Task15Solver();
        System.out.println(task15Solver.get2020thNumber("0,3,6"));
        System.out.println(task15Solver.get2020thNumber("19,20,14,0,9,1"));
    }

    private static void task16(Path inputTextPath) throws IOException {
        Task16Solver task16Solver = new Task16Solver();
        System.out.println(task16Solver.getErrorRate(Files.readString(inputTextPath)));
    }

    private static List<SlopeData> createSlopeData() {
        List<SlopeData> slopeData = new ArrayList<>();
        slopeData.add(new SlopeData(1, 1));
        slopeData.add(new SlopeData(3, 1));
        slopeData.add(new SlopeData(5, 1));
        slopeData.add(new SlopeData(7, 1));
        slopeData.add(new SlopeData(1, 2));
        return slopeData;
    }

    private static Path getInputTextPath(String fileName) throws URISyntaxException {
        URL input = Main.class.getClassLoader().getResource(fileName);
        Path inputText = Paths.get(input.toURI());
        return inputText;
    }

    private static Integer calculateSum(Map<Integer, Integer> solution) {
        return solution.entrySet().stream()
                .map(occurrence -> Math.pow(occurrence.getKey(), occurrence.getValue()))
                .map(Double::intValue)
                .reduce((integer, integer2) -> integer * integer2)
                .orElse(0);
    }

    @Data
    private static class SlopeData {
        private final int right;
        private final int down;
    }

}

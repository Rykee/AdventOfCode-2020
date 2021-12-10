package rykee.net.solver;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class Task15Solver {

    public long get2020thNumber(String input) {
        Map<Long, NumberInfo> numbers = new HashMap<>();
        String[] inputNumbers = input.split(",");
        long mostRecentNumber = Long.parseLong(inputNumbers[inputNumbers.length - 1]);
        for (int i = 0; i < inputNumbers.length; i++) {
            NumberInfo numberInfo = new NumberInfo();
            numberInfo.setMostRecentTurn(i + 1);
            numbers.put(Long.parseLong(inputNumbers[i]), numberInfo);
        }
        for (int i = inputNumbers.length + 1; i <= 30000000; i++) {
            if (numbers.containsKey(mostRecentNumber)) {
                NumberInfo numberInfo = numbers.get(mostRecentNumber);
                if (numberInfo.getSecondMostRecentTurn() == null) {
                    mostRecentNumber = 0;
                } else {
                    mostRecentNumber = numberInfo.getMostRecentTurn() - numberInfo.getSecondMostRecentTurn();
                }
                numbers.putIfAbsent(mostRecentNumber, new NumberInfo());
                numbers.get(mostRecentNumber).updateMostRecentTurn(i);
            } else {
                NumberInfo numberInfo = new NumberInfo();
                numberInfo.setMostRecentTurn(i);
                numbers.put(mostRecentNumber, numberInfo);
                mostRecentNumber = 0;
                numbers.get(0L).updateMostRecentTurn(i);
            }
        }
        return mostRecentNumber;
    }

    @Data
    private static class NumberInfo {
        private Integer mostRecentTurn;
        private Integer secondMostRecentTurn;

        public void updateMostRecentTurn(Integer turn) {
            secondMostRecentTurn = mostRecentTurn;
            mostRecentTurn = turn;
        }
    }
}

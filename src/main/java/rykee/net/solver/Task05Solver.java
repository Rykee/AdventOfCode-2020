package rykee.net.solver;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task05Solver {


    public int calculateHighestId(List<String> lines) {
        return lines.stream()
                .map(BoardingPassParser::parseBoardingPass)
                .max(Comparator.comparingInt(BoardingPassParser.BoardingPass::getPassportId))
                .orElseThrow(() -> new RuntimeException("You fucked up bruh"))
                .getPassportId();
    }

    public List<List<Character>> getFlightMap(List<String> lines) {
        List<List<Character>> flightMap = new ArrayList<>();
        for (int i = 0; i <= 127; i++) {
            flightMap.add(createDefaultMap());
        }
        lines.stream()
                .map(BoardingPassParser::parseBoardingPass)
                .forEach(pass -> flightMap.get(pass.getRow()).set(pass.getColumn(), 'X'));
        return flightMap;
    }

    private List<Character> createDefaultMap() {
        List<Character> characters = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            characters.add('O');
        }
        return characters;
    }

    private static class BoardingPassParser {

        private static final Set<Character> LOWER_HALF = createLowerHalf();

        private static Set<Character> createLowerHalf() {
            Set<Character> lowerHalf = new HashSet<>();
            lowerHalf.add('F');
            lowerHalf.add('L');
            return lowerHalf;
        }

        private static BoardingPass parseBoardingPass(String boardingPassData) {
            String rowData = boardingPassData.substring(0, 7);
            String columnData = boardingPassData.substring(7);
            int row = parseData(rowData, 0, 127);
            int column = parseData(columnData, 0, 7);
            return BoardingPass.builder()
                    .row(row)
                    .column(column)
                    .passportId(row * 8 + column)
                    .build();
        }

        public static int parseData(String input, int lowerBound, int upperBound) {
            int middle = (upperBound + lowerBound) / 2;
            if (LOWER_HALF.contains(input.charAt(0))) {
                if (input.length() == 1) {
                    return lowerBound;
                } else {
                    return parseData(input.substring(1), lowerBound, middle);
                }
            } else {
                if (input.length() == 1) {
                    return upperBound;
                } else {
                    return parseData(input.substring(1), middle + 1, upperBound);
                }
            }
        }

        @Data
        @Builder
        private static class BoardingPass {
            private final int column;
            private final int row;
            private final int passportId;
        }

    }
}

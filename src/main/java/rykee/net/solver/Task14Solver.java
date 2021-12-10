package rykee.net.solver;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task14Solver {

    public long getLeftOverSum(List<String> lines) {
        Map<Long, Long> storedNumbers = new HashMap<>();
        Mask mask = new Mask("000000000000000000000000000000000000");
        lines.stream()
                .map(line -> line.split("\s*=\s*"))
                .forEach(parts -> {
                    if (parts[0].equals("mask")) {
                        mask.setMask(parts[1]);
                    } else {
                        String memorySegment = parts[0].substring(4, parts[0].length() - 1);
                        String maskedFloatyBinary = applyMemoryMask(toBinary36Bit(memorySegment), mask.getMask());
                        List<String> possibleMemories = getPossibleMemorySegments(maskedFloatyBinary, new ArrayList<>());
                        possibleMemories.forEach(s -> storedNumbers.put(Long.parseLong(s, 2), Long.valueOf(parts[1])));
                    }
                });
        return storedNumbers.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    private List<String> getPossibleMemorySegments(String maskedFloatyBinary, List<String> solution) {
        int lastIndex = maskedFloatyBinary.lastIndexOf('X');
        if (lastIndex == -1) {
            solution.add(maskedFloatyBinary);
        } else {
            getPossibleMemorySegments(replaceAt(maskedFloatyBinary, "0", lastIndex), solution);
            getPossibleMemorySegments(replaceAt(maskedFloatyBinary, "1", lastIndex), solution);
        }
        return solution;
    }

    public String replaceAt(String string, String replacement, int pos) {

        return string.substring(0, pos)
                + replacement
                + string.substring(pos + 1);
    }

    private String toBinary36Bit(String part) {
        String binary = Long.toBinaryString(Long.parseLong(part));
        if (binary.length() < 36) {
            return "0".repeat(36 - binary.length()) + binary;
        }
        return binary;
    }

    private String applyMask(String value, String mask) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == 'X') {
                sb.append(value.charAt(i));
            } else {
                sb.append(mask.charAt(i));
            }
        }
        return sb.toString();
    }


    private String applyMemoryMask(String value, String mask) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '0') {
                sb.append(value.charAt(i));
            } else if (mask.charAt(i) == '1') {
                sb.append(1);
            } else {
                sb.append('X');
            }
        }
        return sb.toString();
    }

    @Data
    @AllArgsConstructor
    private static class Mask {
        private String mask;
    }
}

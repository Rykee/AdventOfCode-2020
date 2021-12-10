package rykee.net.solver.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@UtilityClass
public class PassportValidator {

    private final Map<String, Predicate<Integer>> MEASUREMENTS = getMeasurements();
    private final Set<String> EYE_COLORS = getEyeColors();

    private final Pattern HAIR_COLOR_PATTERN = Pattern.compile("#[0-9a-f]{6}");
    private final Pattern ID_PATTERN = Pattern.compile("[0-9]{9}");


    public boolean isValidBirthYear(String birthYear) {
        return isValidNumber(birthYear, 1920, 2002);
    }

    public boolean isValidIssueYear(String issueYear) {
        return isValidNumber(issueYear, 2010, 2020);
    }

    public boolean isValidExpirationYear(String expYear) {
        return isValidNumber(expYear, 2020, 2030);
    }

    public boolean isValidHeight(String height) {
        String measurement = height.substring(height.length() - 2);
        Integer number = parseToInt(height.substring(0, height.length() - 2));
        if (number == null || !MEASUREMENTS.containsKey(measurement)) {
            return false;
        } else {
            return MEASUREMENTS.get(measurement).test(number);
        }
    }

    public boolean isValidHairColor(String hairColor) {
        return HAIR_COLOR_PATTERN.matcher(hairColor).matches();
    }

    public boolean isValidEyeColor(String eyeColor) {
        return EYE_COLORS.contains(eyeColor);
    }

    public boolean isValidPassportId(String passportId) {
        return ID_PATTERN.matcher(passportId).matches();
    }

    private boolean isValidNumber(String numberText, int lowerBound, int upperBound) {
        return isValidNumber(parseToInt(numberText), lowerBound, upperBound);
    }

    private boolean isValidNumber(Integer number, int lowerBound, int upperBound) {
        if (number == null) {
            return false;
        } else {
            return number >= lowerBound && number <= upperBound;
        }
    }

    private Integer parseToInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, Predicate<Integer>> getMeasurements() {
        Map<String, Predicate<Integer>> measurements = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        measurements.put("cm", number -> isValidNumber(number, 150, 193));
        measurements.put("in", number -> isValidNumber(number, 59, 76));
        return measurements;
    }

    private Set<String> getEyeColors() {
        Set<String> eyeColors = new HashSet<>();
        eyeColors.add("amb");
        eyeColors.add("blu");
        eyeColors.add("brn");
        eyeColors.add("gry");
        eyeColors.add("grn");
        eyeColors.add("hzl");
        eyeColors.add("oth");
        return eyeColors;
    }
}

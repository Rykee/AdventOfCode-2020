package rykee.net.solver;

import lombok.Getter;
import rykee.net.solver.util.PassportValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Task04Solver {

    private static final Map<String, Predicate<String>> REQUIRED_FIELDS = createRequiredFieldsValidators();

    private static Map<String, Predicate<String>> createRequiredFieldsValidators() {
        Map<String, Predicate<String>> fields = new HashMap<>();
        fields.put("byr", PassportValidator::isValidBirthYear);
        fields.put("iyr", PassportValidator::isValidIssueYear);
        fields.put("eyr", PassportValidator::isValidExpirationYear);
        fields.put("hgt", PassportValidator::isValidHeight);
        fields.put("hcl", PassportValidator::isValidHairColor);
        fields.put("ecl", PassportValidator::isValidEyeColor);
        fields.put("pid", PassportValidator::isValidPassportId);
        return fields;
    }

    public long countValidPassports(String wholeFile) {
        String[] passports = wholeFile.split("\r\n\r\n");
        return Arrays.stream(passports)
                .map(passport -> new PassportData(passport.split("\s|\r\n")))
                .filter(PassportData::isValid)
                .count();
    }

    @Getter
    private static class PassportData {

        private final Map<String, String> presentFields;

        public PassportData(String[] passportData) {
            presentFields = new HashMap<>();
            Arrays.stream(passportData)
                    .map(field -> field.split(":"))
                    .forEach(fieldParts -> presentFields.put(fieldParts[0], fieldParts[1]));
        }

        public boolean isValid() {
            return presentFields.keySet().containsAll(REQUIRED_FIELDS.keySet()) && areFieldsValid();
        }

        private boolean areFieldsValid() {
            return presentFields.entrySet().stream()
                    .allMatch(field -> {
                        Predicate<String> validator = REQUIRED_FIELDS.get(field.getKey());
                        return validator == null || validator.test(field.getValue());
                    });
        }

    }
}

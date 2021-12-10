package rykee.net.solver.model;

public class PasswordData {

    private final int lowerBound;
    private final int upperBound;
    private final char character;
    private final String password;

    public PasswordData(String inputString) {
        String[] parts = inputString.split(":?\s+");
        String[] bounds = parts[0].split("-");
        lowerBound = Integer.parseInt(bounds[0]);
        upperBound = Integer.parseInt(bounds[1]);
        character = parts[1].charAt(0);
        password = parts[2];
    }

    public boolean isPossible() {
        return password.charAt(lowerBound - 1) == character ^ password.charAt(upperBound - 1) == character;
    }
}

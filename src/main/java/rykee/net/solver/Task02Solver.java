package rykee.net.solver;

import rykee.net.solver.model.FileReader;
import rykee.net.solver.model.PasswordData;

import java.nio.file.Path;

public class Task02Solver {

    private final Path inputData;

    public Task02Solver(Path inputData) {
        this.inputData = inputData;
    }

    public long getValidPasswordCount() {
        return FileReader.getLines(inputData).stream()
                .map(PasswordData::new)
                .filter(PasswordData::isPossible)
                .count();
    }
}

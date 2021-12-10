package rykee.net.solver.model;

public interface Solvable<T, U> {

    public U solve(T input);

}

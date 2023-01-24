public abstract class Cube {
    public abstract boolean isSolved();
    public abstract Cube[] getNeighbors();
    public abstract double h();
    public abstract String executeInverse(int moveIndex);
    public abstract boolean isValidPair(int i1, int i2);
}

public abstract class Cube {
    public abstract boolean isSolved();
    public abstract Cube[] getNeighbors();
    public abstract int h();
    public abstract String executeInverse(int moveIndex);
}

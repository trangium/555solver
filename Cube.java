public interface Cube {
    public boolean isSolved();
    public Cube[] getNeighbors();
    public int h();
    public String executeInverse(int moveIndex);
}

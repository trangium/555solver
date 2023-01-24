import java.util.ArrayList;

public class CubeNode implements Comparable<CubeNode> {
    private Cube cube;
    private double f;
    private int prevMove;
    public double value = 0;

    public CubeNode(Cube cube) {
        this.cube = cube;
        this.f = cube.h();
        this.prevMove = -1;
    }
    
    public CubeNode(Cube cube, double f, int prevMove) {
        this.cube = cube;
        this.prevMove = prevMove;
        this.f = f;
    }

    public Cube getCube() {
        return cube;
    }

    public double getF() {
        return f;
    }

    public int getPrevMove() {
        return prevMove;
    }

    public ArrayList<CubeNode> getNeighbors() {
        Cube[] cubeNbrs = cube.getNeighbors();
        ArrayList<CubeNode> acc = new ArrayList<CubeNode>();
        for (int i=0; i<cubeNbrs.length; i++) 
            if (prevMove == -1 || cube.isValidPair(prevMove, i))
                acc.add(new CubeNode(cubeNbrs[i], 1+f+cubeNbrs[i].h()-cube.h(), i));
        return acc;
    }

    public int compareTo(CubeNode other) {
        double diff = other.value - this.value;
        if (diff > 0) return -1;
        if (diff < 0) return 1;
        return 0;
    }
}
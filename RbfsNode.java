import java.util.*;

public class RbfsNode implements Comparable<RbfsNode>{
    private Cube cube;
    private RbfsNode prevNode;
    private int prevMove;
    private double g;
    public double value = 0;
    public double bound = Double.POSITIVE_INFINITY;
    public PriorityQueue<RbfsNode> neighborPQ;

    public void expand() {
        Cube[] cubeNbrs = cube.getNeighbors();
        ArrayList<RbfsNode> nodeNbrs = new ArrayList<RbfsNode>();
        for (int i=0; i<cubeNbrs.length; i++) 
            if (prevMove == -1 || cube.isValidPair(prevMove, i))
                nodeNbrs.add(new RbfsNode(cubeNbrs[i], this, g+1, i));

        PriorityQueue<RbfsNode> fpq = new PriorityQueue<RbfsNode>();
        for (RbfsNode ni : nodeNbrs) {
            if (this.getF() < value && ni.getF() < value) ni.value = value;
            else ni.value = ni.getF();
            fpq.add(ni);
        }
        neighborPQ = fpq;
    }

    public void contract() {
        neighborPQ = null;
    }

    public RbfsNode(Cube cube) {
        this.cube = cube;
        this.prevNode = null;
        this.prevMove = -1;
        this.g = 0;
    }

    public RbfsNode(Cube cube, RbfsNode prevNode, double g, int prevMove) {
        this.cube = cube;
        this.prevNode = prevNode;
        this.prevMove = prevMove;
        this.g = g;
    }

    public double getF() {
        return cube.h()+g;
    }

    public double bestNbrValue() {
        if (neighborPQ.isEmpty()) return Double.POSITIVE_INFINITY;
        return neighborPQ.peek().value;
    }

    public Cube getCube() {
        return cube;
    }

    public int getPrevMove() {
        return prevMove;
    }

    public RbfsNode getPrevNode() {
        return prevNode;
    }

    public int compareTo(RbfsNode other) {
        double diff = other.value - this.value;
        if (diff > 0) return -1;
        if (diff < 0) return 1;
        return 0;
    }

    public String getSolution() {
        if (prevNode == null) return "";
        return prevNode.getSolution() + cube.executeInverse(prevMove);
    }

    public RbfsNode advance() {
        FiveByFiveSolver.steps++;

        if (neighborPQ == null) expand();
        double nodeNbrBest = bestNbrValue();
        if (nodeNbrBest > bound) {
            contract();
            value = nodeNbrBest;
            prevNode.neighborPQ.add(this);
            return prevNode;
        } else {
            RbfsNode nextNode = neighborPQ.remove();
            nextNode.bound = Math.min(bound, bestNbrValue());
            return nextNode;
        }
    }
}

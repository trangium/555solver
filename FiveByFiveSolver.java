import java.util.*;
import java.awt.Desktop;
import java.net.URI;

public class FiveByFiveSolver {
    static int steps = 0;

    static Stack<CubeNode> solutionNodes;
    private static double rbfsHelper(CubeNode node, double value, double bound) {
        if (node.getF() > bound) return node.getF();
        if (node.getCube().isSolved()) return -1;

        ArrayList<CubeNode> nodeNbrs = node.getNeighbors();
        if (nodeNbrs.size() == 0) return Double.POSITIVE_INFINITY;
        PriorityQueue<CubeNode> fpq = new PriorityQueue<CubeNode>();
        for (CubeNode ni : nodeNbrs) {
            steps++;
            if (node.getF() < value && ni.getF() < value) ni.value = value;
            else ni.value = ni.getF();
            fpq.add(ni);
        }
        while (fpq.peek().value <= bound) {
            CubeNode topCubeNode = fpq.remove();
            double newBound = fpq.isEmpty() ? bound : Math.min(bound, fpq.peek().value);
            topCubeNode.value = rbfsHelper(topCubeNode, topCubeNode.value, newBound);
            if (topCubeNode.value == -1) {
                solutionNodes.add(topCubeNode);
                return -1;
            }
            fpq.add(topCubeNode);
        }
        return fpq.peek().value;
    }

    public static String rbfs(Cube rootPzl) {
        solutionNodes = new Stack<CubeNode>();
        rbfsHelper(new CubeNode(rootPzl), rootPzl.h(), Double.POSITIVE_INFINITY);
        String acc = "";

        while (!solutionNodes.isEmpty()) {
            CubeNode solutionNode = solutionNodes.pop();
            acc += solutionNode.getCube().executeInverse(solutionNode.getPrevMove());
        }
        
        return acc;
    }

    private static double idaStarHelper(Stack<CubeNode> path, double bound) {
        CubeNode currentNode = path.peek();
        double currentF = currentNode.getF();
        if (currentF > bound) return currentF;
        if (currentNode.getCube().isSolved()) return -1;
        
        double minBound = Double.POSITIVE_INFINITY;
        ArrayList<CubeNode> nodeNbrs = currentNode.getNeighbors();
        PriorityQueue<CubeNode> nbrPq = new PriorityQueue<CubeNode>();
        for (CubeNode ni : nodeNbrs) {
            steps++;
            ni.value = ni.getF();
            nbrPq.add(ni);
        }
        while (!nbrPq.isEmpty()) {
            CubeNode nbr = nbrPq.remove();
            path.push(nbr);
            double subBound = idaStarHelper(path, bound);
            if (subBound == -1) return -1;
            if (subBound < minBound) minBound = subBound;
            path.pop();
        }
        return minBound;
    }

    public static String idaStar(Cube rootPzl) {
        double bound = rootPzl.h();
        Stack<CubeNode> solutionNodes = new Stack<CubeNode>();
        solutionNodes.push(new CubeNode(rootPzl));
        while (true) {
            bound = idaStarHelper(solutionNodes, bound); 
            if (bound == -1) {
                String acc = "";
                while (solutionNodes.size() > 1) {
                    CubeNode solutionNode = solutionNodes.pop();
                    acc = solutionNode.getCube().executeInverse(solutionNode.getPrevMove()) + acc;
                }
                return acc;
            }
        }
    }

    public static void webDemo() {
        String setup = randomMoveScramble(new Random(), 170);
        CenterCube pzl = new CenterCube();
        pzl.executeStr(setup);
        String centerSol = iterativeRBFS(pzl);
        EdgeCube pzl2 = new EdgeCube();
        pzl2.executeStr(setup + " " + centerSol);
        String alg = centerSol + iterativeRBFS(pzl2);
        String demoURI = ("https://alg.cubing.net/?puzzle=5x5x5&alg="+alg+"&setup="+setup).replaceAll(" ","%20");;
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(demoURI);
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String randomMoveScramble(Random rand, int moveCount) {
        final String[] moves = {"U", "D", "R", "L", "F", "B"};
        String acc = "";
        int prev = -99;
        int next = -99;
        for (int j=moveCount; j>0; j--) {
            while (next == prev || Math.abs(next - prev) == 3) next = (int)(rand.nextDouble()*6);
            acc += moves[next];
            if (rand.nextDouble() < 0.7) acc += "w";
            if (rand.nextDouble() < 1/3.0) acc += "2";
            else if (rand.nextDouble() < 1/2.0) acc += "'";
            acc += " ";
            prev = next;
        }
        return acc.substring(0, acc.length()-1);
    }

    public static void edgeCubeSolve(int numScrambles, int seed, boolean centers, boolean edges) {
        String[] scrambles = new String[numScrambles];
        for (int i=0; i<numScrambles; i++) {
            scrambles[i] = randomMoveScramble(new Random(seed), 170);
        }
        int totalSteps = 0;
        int totalLen = 0;
        double totalTime = 0;
        for (int i=0; i<numScrambles; i++) {
            String setup = scrambles[i];
            CenterCube pzl1 = new CenterCube();
            pzl1.executeStr(setup);
            System.out.println(setup);
            
            double startTime = System.currentTimeMillis();
            steps = 0;
            String centerSol = iterativeRBFS(pzl1);
            String fullSol = centerSol;
            int len;
            if (centers) {
                len = (centerSol.length() - centerSol.replaceAll(" ","").length());
            } else {
                len = 0;
                steps = 0;
                startTime = System.currentTimeMillis();
            }

            if (edges) {
                EdgeCube pzl2 = new EdgeCube();
                pzl2.executeStr(setup + " " + centerSol);
                String edgeSol = iterativeRBFS(pzl2);
                fullSol += edgeSol;
                len += (edgeSol.length() - edgeSol.replaceAll(" ","").length());
            }
            System.out.println(fullSol);
            EdgeCube pzl = new EdgeCube();
            pzl.executeStr(setup + " " + fullSol);
            System.out.println("done: "+(i+1)+"/"+numScrambles);
            System.out.println("time: "+(int)(System.currentTimeMillis()-startTime)+" ms");
            System.out.println("step: "+steps);
            //System.out.println("flip: "+(pzl).flipCount(pzl.getWingCycles()));
            //System.out.println("swap: " +(pzl).swapCount(pzl.getWingCycles()));
            //System.out.println("ctr: " + (pzl.centerDistance(0, 40) + pzl.centerDistance(8, 24) + pzl.centerDistance(16, 32)));
            //System.out.println("h: "+(pzl).h());
            System.out.println("len: "+len);
            totalSteps += steps;
            totalLen += (fullSol.length() - fullSol.replaceAll(" ","").length());
            totalTime += System.currentTimeMillis()-startTime;
        }
        System.out.println("\n\nAverage nodes: "+(totalSteps/((double)(numScrambles))));
        System.out.println("  Average len: "+totalLen/((double)(numScrambles)));
        System.out.println(" Average time: "+(((int)(totalTime))/1000.0/numScrambles)+"s");
    }

    public static String iterativeRBFS(Cube c) {
        RbfsNode node = new RbfsNode(c);
        while (!node.getCube().isSolved()) {
            node = node.advance();
        }
        return node.getSolution();
    }

    public static void iterativeRBFStest() {
        // String[] centerMoves = new String[] {"U", "U2", "U'", "R", "R2", "R'", "F", "F2", "F'", "D", "D2", "D'", "L", "L2", "L'", "B", "B2", "B'", "Uw", "Uw2", "Uw'", "Rw", "Rw2", "Rw'", "Fw", "Fw2", "Fw'", "Dw", "Dw2", "Dw'", "Lw", "Lw2", "Lw'", "Bw", "Bw2", "Bw'"};
        String[] edgeMoves = new String[] {"U", "U2", "U'", "R", "R2", "R'", "F", "F2", "F'", "D", "D2", "D'", "L", "L2", "L'", "B", "B2", "B'", "Uw2", "Rw2", "Fw2", "Dw2", "Lw2", "Bw2"};

        String scramble = "Fw' Rw D' Rw' Uw Rw' Rw' Dw' D' Bw Rw2 F2 Fw2 Lw' Bw Rw' Dw2 D2 Bw2 Dw Uw2 Dw Uw' Fw' Lw Rw' Lw' Bw Lw F Uw' L Lw' Dw U Rw' Uw' L' U2 Fw2 B F Lw' Bw' U Fw F Bw2 D U' Uw' Bw2 Lw2 L' Rw' Rw' Uw Uw' Uw' Rw Uw' Dw Fw2 Uw B' Lw' D Rw2 Uw D Dw' F2 Bw2 Dw' Fw D Lw2 Rw' Lw Fw' Dw2 Dw' R Uw' Rw' Dw' Fw2 U2 F' Bw' Uw Lw2 B' B2 Uw2 Fw' Dw Bw' Rw Fw' Bw Bw2 R F2 R Rw Rw' Uw2 B2 Fw2 U2 Lw' Uw Rw Dw' Bw2 Fw Bw Rw L F2 Uw U' Rw Fw Uw Uw' Bw2 Fw Rw2 F' Bw Uw Lw2 R2 Uw Dw' R Rw2 F' L2 F' Lw Bw2 Dw' Uw' Fw' F Lw Rw Dw Dw D' Bw2 Fw R2 Dw B2 Rw2 L D U Lw Lw2 Bw Fw' Uw' Bw' R' B2 Fw' Lw2 B D2 Bw F2 D' Uw' Uw' Lw Rw2 Bw2 L' Uw' Uw2 Bw2 Lw2 Rw2 Lw' Rw Lw' Uw Bw' Bw2 Dw' Fw Fw' L2 B2 R2 Uw2 Bw D' Rw U2 R L Fw' D Bw U Fw Dw2 F B' Rw2 Dw Rw2 F2 Uw' F2 Uw";
        

        EdgeCube e = new EdgeCube();
        e.executeStr(scramble);
        System.out.println("Starting h: "+e.h());

        int totalCount = 0;
        for (int moveID=0; moveID<24; moveID++) {
            String moveStr = edgeMoves[moveID];
            System.out.print(moveStr + " ");
            EdgeCube c = new EdgeCube();
            c.executeStr(scramble);
            c.executeStr(moveStr);
            RbfsNode node = new RbfsNode(c, null, 0, moveID);
            while (!node.getCube().isSolved()) {
                node = node.advance();
            }
            String sol = node.getSolution();
            System.out.println(sol);
            System.out.print("Count: "+steps);
            System.out.println("  Len: "+(1+sol.length()-sol.replaceAll(" ","").length()));
            totalCount += steps;
            steps = 0;
        }
        System.out.println("TOTAL COUNT: " + totalCount);

    }

    public static void crucialEdgeTest() {
        // Crucial edge locations have positive crucialness. They shouldn't be solved.
        // Non-crucial edge locations have negative crucialness. They should be solved.
        EdgeCube e = new EdgeCube();
        e.executeStr("Rw2 U' D2 Fw2 Uw2 Rw2 R U' Fw2");


        // testing the crucialness of the UFr location (edge 4)
        int currentDistance = (e.centerDistance(0, 40));
        int minDistance = Integer.MAX_VALUE;
        for (String seq : new String[] {"Rw2", "D Rw2", "D2 Rw2", "D' Rw2"}) {
            EdgeCube f = new EdgeCube(e);
            f.executeStr(seq);
            minDistance = Math.min(minDistance, f.centerDistance(0, 40));
        }
        System.out.println("Crucialness to the U/D centers: " + (currentDistance - minDistance));

        currentDistance = (e.centerDistance(16, 32));
        minDistance = Integer.MAX_VALUE;
        for (String seq : new String[] {"Uw2", "B Uw2", "B2 Uw2", "B' Uw2"}) {
            EdgeCube f = new EdgeCube(e);
            f.executeStr(seq);
            minDistance = Math.min(minDistance, f.centerDistance(16, 32));
        }
        System.out.println("Crucialness to the F/B centers: " + (currentDistance - minDistance));
        
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            CenterCube.moveWeights = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1.3, 1.3, 1.3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1.3, 1.3, 1.3};
            EdgeCube.moveWeights = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1.3, 1.3, 1.3, 1, 1, 1, 1, 1, 1.3};
        } else {
            CenterCube.moveWeights = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            EdgeCube.moveWeights = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        }
        webDemo();
        // System.out.println(CenterCube.endTable);
        // edgeCubeSolve(100, 1183833536, true, true);
        // iterativeRBFStest();
        // System.out.println(java.lang.Runtime.getRuntime().totalMemory() - java.lang.Runtime.getRuntime().freeMemory());
        // crucialEdgeTest();

    }
      
}

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
        String setup = "R U2 Fw2 Bw Uw Dw2 Bw2 Uw' Uw U' Fw' Dw2 L R Uw Bw Bw Lw' Fw2 Bw2 Dw Bw2 Fw R' Lw' Uw R' Lw Bw Dw Uw' Uw' L2 Fw2 Rw2 Rw' Bw L' Dw2 Dw2 Rw' Dw' R Fw' Fw2 Dw Bw U Rw2 Fw2 Rw' Rw L R2 Lw' D2 Bw Uw2 Fw' Bw Fw Fw' D' Fw2 Dw2 B B Fw' Dw' U' D Uw' L D F Lw F Uw2 Dw2 Lw Uw Lw2 Bw' Lw2 Fw L Dw2 Rw Rw Lw' F' L Lw Dw2 Dw2 Lw' Fw' L2 U Rw' D2 Bw' B Uw' L' Uw Rw Dw' Uw Fw' Bw2 Dw Rw2 Rw2 Fw2 Uw2 Bw' Uw F Lw R Bw' Fw' D' Rw F2 F' Uw2 U' Rw' Lw Lw' D' Lw Bw2 F Dw R2 Fw Uw2 Bw' Lw2 Fw Fw2 F2 Lw Fw L2 Lw' Uw2 B U2 Lw2 Lw2 Bw' R' R' Uw2 Rw' Rw' Lw Uw2 Lw' B2 Uw2 D' U2 Uw2 Bw' F' Uw Rw Uw2 Lw2 Lw2 B Uw' Fw2 Dw2 Fw' Rw2 Fw2 B' Fw' U Lw' Dw2 D2 Lw2 Rw U2 F Fw' Lw' Dw B2 L' U2 R2 Uw";
        CenterCube pzl = new CenterCube();
        pzl.executeStr(setup);
        String centerSol = rbfs(pzl);
        EdgeCube pzl2 = new EdgeCube();
        pzl2.executeStr(setup + " " + centerSol);
        String alg = centerSol + rbfs(pzl2);
        String demoURI = ("https://alg.cubing.net/?puzzle=5x5x5&alg="+alg+"&setup="+setup).replaceAll(" ","%20");;
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(demoURI);
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void edgeCubeSolve(int numScrambles, int seed, boolean centers, boolean edges) {
        Random rand = new Random(seed);
        final String[] moves = {"U", "D", "R", "L", "F", "B"};
        String[] scrambles = new String[numScrambles];
        for (int i=0; i<numScrambles; i++) {
            String acc = "";
            for (int j=0; j<200; j++) {
                acc += moves[(int)(rand.nextDouble()*6)];
                if (rand.nextDouble() < 0.7) acc += "w";
                if (rand.nextDouble() < 1/3.0) acc += "2";
                else if (rand.nextDouble() < 1/2.0) acc += "'";
                acc += " ";
            }
            scrambles[i] = acc.substring(0, acc.length()-1);
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
        String[] centerMoves = new String[] {"U", "U2", "U'", "R", "R2", "R'", "F", "F2", "F'", "D", "D2", "D'", "L", "L2", "L'", "B", "B2", "B'", "Uw", "Uw2", "Uw'", "Rw", "Rw2", "Rw'", "Fw", "Fw2", "Fw'", "Dw", "Dw2", "Dw'", "Lw", "Lw2", "Lw'", "Bw", "Bw2", "Bw'"};
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

    public static void main(String[] args) {
        // webDemo();
        // System.out.println(CenterCube.endTable);
        // edgeCubeSolve(100, 1183833535, true, true);
        iterativeRBFStest();
        // System.out.println(java.lang.Runtime.getRuntime().totalMemory() - java.lang.Runtime.getRuntime().freeMemory());

    }
      
}

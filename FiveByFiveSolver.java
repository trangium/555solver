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
        String setup = "U' Dw' Fw' Dw F' B Fw2 L' R2 B U Bw L2 D Rw D Rw' F Dw' R' Bw B2 Rw' B Bw' Dw' R F' D Uw F2 B Dw2 Lw Dw Uw2 F Fw Rw2 Bw2 Rw2 Fw2 Rw' R2 Uw2 F2 R Rw' L2 B2 R' D' L2 D2 R B' Bw' R Dw Fw' R' Fw' Bw2 L Uw Fw' Dw2 F Lw2 B2 Uw Fw2 Rw2 D' B U' Fw' U2 Rw F2 Rw2 D R' U2 Rw B2 Uw' Fw2 Uw' U Lw Fw' U' F' R' L Fw2 Lw U2 R D2 L2 Bw2 Uw2 L D Lw2 L Uw' Dw F Lw F' D2 Bw L Fw' F' Lw F'";
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

    public static void edgeCubeSolve() {
        int numScrambles = 25;
        Random rand = new Random(369817240);
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
            String centerSol = rbfs(pzl1);

            double startTime = System.currentTimeMillis();
            steps = 0;
            EdgeCube pzl2 = new EdgeCube();
            System.out.println(scrambles[i]);
            System.out.println(centerSol);
            pzl2.executeStr(setup + " " + centerSol);
            String edgeSol = rbfs(pzl2);
            String fullSol = centerSol + edgeSol;
            System.out.println(edgeSol);
            EdgeCube pzl = new EdgeCube();
            pzl.executeStr(setup + " " + fullSol);
            System.out.println("done: "+(i+1)+"/"+numScrambles);
            System.out.println("time: "+(int)(System.currentTimeMillis()-startTime)+" ms");
            System.out.println("step: "+steps);
            //System.out.println("flip: "+(pzl).flipCount(pzl.getWingCycles()));
            //System.out.println("swap: " +(pzl).swapCount(pzl.getWingCycles()));
            //System.out.println("ctr: " + (pzl.centerDistance(0, 40) + pzl.centerDistance(8, 24) + pzl.centerDistance(16, 32)));
            //System.out.println("h: "+(pzl).h());
            System.out.println("len: "+(edgeSol.length() - edgeSol.replaceAll(" ","").length()));
            totalSteps += steps;
            totalLen += (edgeSol.length() - edgeSol.replaceAll(" ","").length());
            totalTime += System.currentTimeMillis()-startTime;
        }
        System.out.println("\n\nAverage nodes: "+(totalSteps/((double)(numScrambles))));
        System.out.println("  Average len: "+totalLen/((double)(numScrambles)));
        System.out.println(" Average time: "+(((int)(totalTime))/1000.0/numScrambles)+"s");
    }

    public static void main(String[] args) {
        // webDemo();
        // edgeCubeSolve();
        int a = 534;
        int b = 129;
        System.out.println((b-a) >>> 31);
    }
      
}

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
        String setup = "D2 F2 Dw' B' U' Bw F' U2 R2 L2 Uw L2 D' Uw2 L2 Rw Fw2 L Uw' Lw2 Fw2 B2 D' Bw' U' R2 L Bw Lw R2 Bw R Bw B2 F Uw' Bw2 Rw' B' F' Fw D' Bw' U R' F2 Rw Dw Uw2 Lw' L' B2 R' Dw U' Uw' B2 Fw' L' U2 D' Dw Rw' Dw2 Bw2 Fw Rw2 D Uw' Fw2 Bw2 Uw L' Rw D R2 U Rw2 Dw2 Uw D Lw R2 B Rw2 R Fw D2 L' R2 Rw2 D' F L Uw U R' B U2 Dw2 L' Lw' Dw U' F L Uw2 B2 F2 Dw' Bw' D2 L2 Bw' L Bw' Fw L Rw Uw L Dw Fw2 Bw R2 Dw' B' Bw' Fw' R F Lw2 Uw' B2 U' Bw' B R2 L2 Rw F2 Fw' D Lw' Uw' Dw2 U2 Fw2 B' F L' Rw B Fw Rw2 F2 B U2 Rw2 Uw D L' Rw2 Fw2 Rw R2 Dw2 Lw B2 L2 Dw' Bw2 D' Rw Lw2 Dw' U R2 F2 U'";
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
        Random rand = new Random(369818312);
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

    //  -------------------------------------start of new stuff--------------------------------------

    private static int combine(int a, int b) {
        int x = Math.max(a, b);
        return x*(x-3)/2 + a + b;
    }

    // precondition: EdgeCube e is Uw2 or Dw2 from reduction
    public static int wingIndex(EdgeCube e) {
        byte[] cyc = e.getWingCycles();
        int a = -1;
        int b = -1;
        int c = -1;
        int d = -1;
        for (int i=0; i<24; i++) {
            if (cyc[i] != i) {
                if (a == -1) a = i;
                else if (cyc[i] == a) b = i;
                else if (c == -1) c = i;
                else d = i;
            }
        }
        return combine(combine(a, b), combine(c, d));
    }

    // precondition: EdgeCube e is Uw2 or Dw2 from reduction
    public static int centerIndex(EdgeCube e) {
        int factor = 1;
        int acc = 0;
        for (int i=9; i<40; i+=2) {
            if (e.getCenterPerm()[i] != EdgeCube.centerSolved[i]) {
                acc += ((i%8)>>1)*factor;
                factor *= 4;
            }
        }
        return acc;
    }

    public static int fullIndex(EdgeCube e) {
        int symData = EquatorPrun.symReference[wingIndex(e)];
        return ((symData >> 5) << 8) + EquatorPrun.centerTransformTable[centerIndex(e)*32+(symData & 31)];
    }

    public static int fullIndex(int wingIdx, int centerIdx) {
        int symData = EquatorPrun.symReference[wingIdx];
        return ((symData >> 5) << 8) + EquatorPrun.centerTransformTable[centerIdx*32+(symData & 31)];
    }

    public static void equatorTest() {
        EdgeCube e = new EdgeCube();
        e.executeStr("Uw2 R U R' Uw2 R2 U L U2 L' Uw B U2 B' Uw' L2 R2 Dw' L2 R2 Uw2 L2 R2 Dw' L2 R2");
        for (byte[] cycle : e.getPossibleSwaps(e.getWingCycles())) {
            for (byte b : cycle) System.out.print(b+" ");
            System.out.println();
        }
        System.out.println(fullIndex(e));
        System.out.println(EquatorPrun.fullDepthTable[fullIndex(e)]);
    }

// ------------------------------- start of cycles stuff ------------------------------------------------------------

    public static void cyclesTest() {
        EdgeCube e = new EdgeCube();
        e.executeStr("Uw2 R U R' F Uw2");
        e.printCycles();

        for (byte[] bar : e.getPossibleSwaps(e.getWingCycles())) {
            for (byte b : bar) {
                System.out.print(b);
                System.out.print(" ");  
            }
            System.out.println();
        }

        System.out.println("UD ctr: " + e.centerDistance(0, 40));
        System.out.println("LR ctr: " + e.centerDistance(8, 24));
        System.out.println("FB ctr: " + e.centerDistance(16, 32));

        System.out.println("full depth: " + e.getMinDepth(2));

        System.out.println("------------");
    }

    public static void main(String[] args) {
        webDemo();
        // edgeCubeSolve();
        // equatorTest();
        // cyclesTest();

        /*
        EdgeCube e = new EdgeCube();
        e.executeStr("U F L' Rw2 U D L R U' D' Rw2 U L D R B R' F B2 L");
        System.out.println(e.h());
        System.out.println(rbfs(e));
        */
        
        
    }
      
}

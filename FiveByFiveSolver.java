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
        String setup = "Dw' Uw2 Bw' B U Rw2 R2 F2 B' Rw F Bw' Dw Lw' Uw L Bw2 B' Fw D2 Rw D2 B R Rw' Fw2 Lw' D Bw Dw Rw Dw2 Lw B' F' Rw' D2 R' U' F2 L2 U2 D L2 D2 Lw B2 L Lw2 F Lw2 Rw' Dw2 L' D F D Dw U2 Bw'";
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
        Random rand = new Random(369818242);
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

    /*
    public static int finalDepth(EdgeCube e) {
        byte[] wingCycles = e.getWingCycles();
        ArrayList<byte[]> swaps = e.getPossibleSwaps(wingCycles);
        if (swaps.size() != 1) return 254;
        byte[] b = swaps.get(0);
        int finalIndex = combine(combine(b[0], b[1]), combine(b[2], b[3]));
        return -1;
    }
    */

    public static void equatorTest() {
        EdgeCube e = new EdgeCube();
        e.executeStr("R F R B U D L F Dw2 R U F R'");
        for (byte[] cycle : e.getPossibleSwaps(e.getWingCycles())) {
            for (byte b : cycle) System.out.print(b+" ");
            System.out.println();
        }
        System.out.println(fullIndex(e));
        System.out.println(EquatorPrun.fullDepthTable[fullIndex(e)]);
    }

// ------------------------------- start of cycles stuff ------------------------------------------------------------
    public static int getMinDepth(EdgeCube e, byte[] cycle) {
        /*
         *             norm            shift        unshift       doubleshift
         *  0 = D U = (3, 3)   - 51 - (2, 0) - 32 - (0, 2) - 2  - (1, 1) - 17
         *  1 = D U2 = (3, 2)  - 50 - (2, 3) - 35 - (0, 1) - 1  - (1, 0) - 16
         *  2 = D U' = (3, 1)  - 49 - (2, 2) - 34 - (0, 0) - 0  - (1, 3) - 19
         *  3 = D = (3, 0)     - 48 - (2, 1) - 33 - (0, 3) - 3  - (1, 2) - 18
         *  4 = D2 U = (2, 3)  - 35 - (1, 0) - 16 - (3, 2) - 50 - (0, 1) - 1
         *  5 = D2 U2 = (2, 2) - 34 - (1, 3) - 19 - (3, 1) - 49 - (0, 0) - 0
         *  6 = D2 U' = (2, 1) - 33 - (1, 2) - 18 - (3, 0) - 48 - (0, 3) - 3
         *  7 = D2 = (2, 0)    - 32 - (1, 1) - 17 - (3, 3) - 51 - (0, 2) - 2
         *  8 = D' U = (1, 3)  - 19 - (0, 0) - 0  - (2, 2) - 34 - (3, 1) - 49
         *  9 = D' U2 = (1, 2) - 18 - (0, 3) - 3  - (2, 1) - 33 - (3, 0) - 48
         * 10 = D' U' = (1, 1) - 17 - (0, 2) - 2  - (2, 0) - 32 - (3, 3) - 51
         * 11 = D' = (1, 0)    - 16 - (0, 1) - 1  - (2, 3) - 35 - (3, 2) - 50
         * 12 = U = (0, 3)     - 3  - (3, 0) - 48 - (1, 2) - 18 - (2, 1) - 33
         * 13 = U2 = (0, 2)    - 2  - (3, 3) - 51 - (1, 1) - 17 - (2, 0) - 32
         * 14 = U' = (0, 1)    - 1  - (3, 2) - 50 - (1, 0) - 16 - (2, 3) - 35
         * 15 = null = (0, 0)  - 0  - (3, 1) - 49 - (1, 3) - 19 - (2, 2) - 34
         */
        int[] prunToCtrIdx = new int[]{51, 50, 49, 48, 35, 34, 33, 32, 19, 18, 17, 16, 3, 2, 1, 0};
        int[] prunToCtrIdxShift = new int[]{32, 35, 34, 33, 16, 19, 18, 17, 0, 3, 2, 1, 48, 51, 50, 49};
        // int[] prunToCtrIdxUnshift = new int[]{2, 1, 0, 3, 50, 49, 48, 51, 34, 33, 32, 35, 18, 17, 16, 19};
        int[] prunToCtrIdxDoubleShift = new int[]{17, 16, 19, 18, 1, 0, 3, 2, 49, 48, 51, 50, 33, 32, 35, 34};

        // dist1Prun: Rw2 (0, 40) / Fw2 (8, 24) / Rw2 (16, 32)

        // x: (0, 40), (16, 32) + [0, 0]
        // y: (8, 24), (16, 32) + [1, 1]
        // z: (0, 40), ( 8, 24) + [1, 0]

        /*
         EdgeCube e;
         ...
         int ud = element of EdgeCube.dist1Prun.get(e.centerID(0, 40))
         int lr = element of EdgeCube.dist1Prun.get(e.centerID(8, 24))
         int fb = element of EdgeCube.dist1Prun.get(e.centerID(16, 32))

         int getCenterIndex(int axis) {
            x axis: return u + 4f + 16d + 64b
            return prunToCtrIdx[ud] + 4*prunToCtrIdx[fb]

            y axis: return l + 4f + 16r + 64b
            return prunToCtrIdxShift[lr] + 4*prunToCtrIdxShift[fb]

            z axis: return l + 4u + 16r + 64d
            return prunToCtrIdxDoubleShift[lr] + 4*prunToCtrIdxUnshift[ud]
            


         }

         */

        int udCtrDist = (e.centerDistance(0, 40)+1)/2;
        int lrCtrDist = (e.centerDistance(8, 24)+1)/2;
        int fbCtrDist = (e.centerDistance(16, 32)+1)/2;

        int targetDistSum = (udCtrDist + lrCtrDist + fbCtrDist == 2 && Math.max(Math.max(udCtrDist, lrCtrDist), fbCtrDist) == 1) ? 0 : 2;

        HashSet<Integer> fullIndices = new HashSet<Integer>();
         // try reducing by Uw or Dw
        // the nullRots are completely unnecessary and are there to show what the code would be like for fb and lr
        int udEdgeIndex = combine(combine(EdgeCube.nullRot[cycle[0]], EdgeCube.nullRot[cycle[1]]), combine(EdgeCube.nullRot[cycle[2]], EdgeCube.nullRot[cycle[3]]));
        int lrEdgeIndex = combine(combine(EdgeCube.zRot[cycle[0]], EdgeCube.zRot[cycle[1]]), combine(EdgeCube.zRot[cycle[2]], EdgeCube.zRot[cycle[3]]));
        int fbEdgeIndex = combine(combine(EdgeCube.xRot[cycle[0]], EdgeCube.xRot[cycle[1]]), combine(EdgeCube.xRot[cycle[2]], EdgeCube.xRot[cycle[3]]));
        for (int lrMoves=0; lrMoves <= 1; lrMoves++) {
            int fbMoves = targetDistSum - lrMoves - udCtrDist;
            if (fbMoves < 0 || fbMoves > 1) continue;
            for (int lr1 : EdgeCube.ctrDistPrun.get(lrMoves).get(e.centerID(8, 24))) {
                for (int fb1 : EdgeCube.ctrDistPrun.get(fbMoves).get(e.centerID(16, 32))) {
                    int centerIdx = prunToCtrIdxShift[lr1] + 4*prunToCtrIdxShift[fb1];
                    fullIndices.add(fullIndex(udEdgeIndex, centerIdx));
                }
                System.out.println("karina");
            }
        }

        for (int udMoves=0; udMoves <= 1; udMoves++) {
            int fbMoves = targetDistSum - udMoves - lrCtrDist;
            if (fbMoves < 0 || fbMoves > 1) continue;
            for (int ud1 : EdgeCube.ctrDistPrun.get(udMoves).get(e.centerID(0, 40))) {
                for (int fb1 : EdgeCube.ctrDistPrun.get(fbMoves).get(e.centerID(16, 32))) {
                    int centerIdx = prunToCtrIdx[ud1] + 4*prunToCtrIdx[fb1];
                    fullIndices.add(fullIndex(lrEdgeIndex, centerIdx));
                }
            }
        }

        for (int udMoves=0; udMoves <= 1; udMoves++) {
            int lrMoves = targetDistSum - udMoves - fbCtrDist;
            if (lrMoves < 0 || lrMoves > 1) continue;
            for (int ud1 : EdgeCube.ctrDistPrun.get(udMoves).get(e.centerID(0, 40))) {
                for (int lr1 : EdgeCube.ctrDistPrun.get(lrMoves).get(e.centerID(8, 24))) {
                    int centerIdx = prunToCtrIdxDoubleShift[lr1] + 4*prunToCtrIdxShift[ud1];
                    fullIndices.add(fullIndex(fbEdgeIndex, centerIdx));
                }
            }
        }

        int minDepth = EquatorPrun.NOTFOUND;
        for (int index : fullIndices) {
            System.out.println(EquatorPrun.fullDepthTable[index]);
            minDepth = Math.min(minDepth, EquatorPrun.fullDepthTable[index]);
        }
        return minDepth;
    }

    public static void cyclesTest() {
        EdgeCube e = new EdgeCube();
        e.executeStr("R F R B U D L F Fw2 L B2 F Rw2 U L F D");
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
        System.out.println("depth: " + e.minWingDepth());

        System.out.println(EdgeCube.dist0Prun.get(e.centerID(16, 32)));

        for (byte[] swaps : e.getPossibleSwaps(e.getWingCycles()))
            System.out.println("full depth: " + getMinDepth(e, swaps));

        System.out.println("------------");
    }

    public static void main(String[] args) {
        // webDemo();
        // edgeCubeSolve();
        // equatorTest();
        cyclesTest();

        /*
        EdgeCube e = new EdgeCube();
        e.executeStr("Rw' Lw");
        e.print();
        */
        
    }
      
}

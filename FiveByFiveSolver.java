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
        Random rand = new Random(369818240);
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

    public static void equatorTest() {
        EdgeCube e = new EdgeCube();
        e.executeStr("R F R B U D L F Dw2 R U F R'");
        e.printCycles();
        System.out.println(fullIndex(e));
        System.out.println(EquatorPrun.fullDepthTable[fullIndex(e)]);
    }

// ------------------------------- start of cycles stuff ------------------------------------------------------------

    public static ArrayList<ArrayList<Byte>> permCycles(byte[] wingCycles) {
        ArrayList<ArrayList<Byte>> cyc = new ArrayList<ArrayList<Byte>>(4);
        cyc.add(new ArrayList<Byte>(8));
        cyc.add(new ArrayList<Byte>(6));
        cyc.add(new ArrayList<Byte>(4));
        cyc.add(new ArrayList<Byte>(5));
        
        ArrayList<Byte> tempCycles = new ArrayList<Byte>(5);

        int needle = 0;
        while (needle < EdgeCube.numberOfWings) {
            if (wingCycles[needle] == needle) {
                needle++;
                continue;
            }
            byte temp = wingCycles[needle];
            wingCycles[needle] = wingCycles[temp];
            wingCycles[temp] = temp;
            tempCycles.add(temp);
            if (wingCycles[needle] == needle) {
                tempCycles.add(wingCycles[needle]);
                for (Byte b : tempCycles) cyc.get(tempCycles.size()-2).add(b);
                tempCycles.clear();
                needle++;
            }
        }



        return cyc;
    }

    public static ArrayList<byte[]> getPossibleSwaps(byte[] wingCycles) {
        ArrayList<ArrayList<Byte>> cyc = permCycles(wingCycles);
        int[][][] swapIndices;

        switch (cyc.get(0).size() + cyc.get(1).size()) {
            case 8:
                // 2 2 2 2
                swapIndices = new int[][][]{
                    {{0, 0}, {0, 1}, {0, 2}, {0, 3}}, 
                    {{0, 0}, {0, 1}, {0, 4}, {0, 5}},
                    {{0, 0}, {0, 1}, {0, 6}, {0, 7}},
                    {{0, 2}, {0, 3}, {0, 4}, {0, 5}},
                    {{0, 2}, {0, 3}, {0, 6}, {0, 7}},
                    {{0, 4}, {0, 5}, {0, 6}, {0, 7}},
                };
                break;
            case 7:
                // 3 2 2
                swapIndices = new int[][][]{
                    {{1, 0}, {1, 1}, {0, 0}, {0, 1}}, 
                    {{1, 0}, {1, 1}, {0, 2}, {0, 3}},
                    {{1, 1}, {1, 2}, {0, 0}, {0, 1}},
                    {{1, 1}, {1, 2}, {0, 2}, {0, 3}},
                    {{1, 0}, {1, 2}, {0, 0}, {0, 1}},
                    {{1, 0}, {1, 2}, {0, 2}, {0, 3}},
                };
                break;
            case 6:
                // 3 3
                swapIndices = new int[][][]{
                    {{1, 0}, {1, 1}, {1, 3}, {1, 4}},
                    {{1, 0}, {1, 1}, {1, 3}, {1, 5}},
                    {{1, 0}, {1, 1}, {1, 4}, {1, 5}},
                    {{1, 1}, {1, 2}, {1, 3}, {1, 4}},
                    {{1, 1}, {1, 2}, {1, 3}, {1, 5}},
                    {{1, 1}, {1, 2}, {1, 4}, {1, 5}},
                    {{1, 0}, {1, 2}, {1, 3}, {1, 4}},
                    {{1, 0}, {1, 2}, {1, 3}, {1, 5}},
                    {{1, 0}, {1, 2}, {1, 4}, {1, 5}},
                };
                break;
            case 4:
                // 2 2
                swapIndices = new int[][][]{
                    {{0, 0}, {0, 1}, {0, 2}, {0, 3}},
                };
                break;
            case 2:
                // 2 4
                swapIndices = new int[][][]{
                    {{0, 0}, {0, 1}, {2, 0}, {2, 2}},
                    {{0, 0}, {0, 1}, {2, 1}, {2, 3}},
                    {{2, 0}, {2, 1}, {2, 2}, {2, 3}},
                    {{2, 0}, {2, 3}, {2, 1}, {2, 2}},
                };
                break;
            case 0:
                // 5
                swapIndices = new int[][][]{
                    {{3, 0}, {3, 1}, {3, 2}, {3, 4}},
                    {{3, 1}, {3, 2}, {3, 3}, {3, 0}},
                    {{3, 2}, {3, 3}, {3, 4}, {3, 1}},
                    {{3, 3}, {3, 4}, {3, 0}, {3, 2}},
                    {{3, 4}, {3, 0}, {3, 1}, {3, 3}},
                };
                break;
            default:
                // case 3 - three cycle or more than four swaps; proceed as usual
                return new ArrayList<byte[]>();
        }

        ArrayList<byte[]> possibleSwaps = new ArrayList<byte[]>();
        for (int[][] swap : swapIndices) {
            byte[] acc = new byte[4];
            for (int i=0; i<4; i++)
                acc[i] = cyc.get(swap[i][0]).get(swap[i][1]);
            possibleSwaps.add(acc);
        }

        return possibleSwaps;
    }

    // precondition: e is exactly 2 wide moves from solved
    public static int minWingDepth(EdgeCube e) {
        byte[] wingCycles = e.getWingCycles();
        int[] zRot = {15, 14, 7, 6, 9, 8, 23, 22, 17, 16, 5, 4, 1, 0, 21, 20, 11, 10, 3, 2, 13, 12, 19, 18};
        int[] xRot = {5, 4, 10, 11, 17, 16, 8, 9, 22, 23, 18, 19, 2, 3, 6, 7, 21, 20, 12, 13, 1, 0, 14, 15};
        int[] nullRot = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        ArrayList<byte[]> possibleSwaps = getPossibleSwaps(wingCycles);
        ArrayList<int[]> possibleRots = new ArrayList<int[]>(3);
        int udCtrDist = e.centerDistance(0, 40);
        int lrCtrDist = e.centerDistance(16, 32);
        int fbCtrDist = e.centerDistance(8, 24);
        if (udCtrDist <= 2) possibleRots.add(nullRot);
        if (lrCtrDist <= 2) possibleRots.add(xRot);
        if (fbCtrDist <= 2) possibleRots.add(zRot);

        int minFound = 127;
        for (int[] rot : possibleRots) {
            for (byte[] b : possibleSwaps) {
                int edgeIndex = combine(combine(rot[b[0]], rot[b[1]]), combine(rot[b[2]], rot[b[3]]));
                int depth = EquatorPrun.depthTable[edgeIndex];
                for (byte b2 : b) {
                    System.out.print(b2);
                    System.out.print(" ");  
                }
                System.out.println(" => depth " + depth);
                minFound = Math.min(depth, minFound);
            }
        }
        return minFound;
    }

    public static void cyclesTest() {
        EdgeCube e = new EdgeCube();
        e.executeStr("R F R B U D L F Dw2 R U R' Dw2");
        e.printCycles();

        for (byte[] bar : getPossibleSwaps(e.getWingCycles())) {
            for (byte b : bar) {
                System.out.print(b);
                System.out.print(" ");  
            }
            System.out.println();
        }

        System.out.println("UD ctr: " + e.centerDistance(0, 40));
        System.out.println("LR ctr: " + e.centerDistance(8, 24));
        System.out.println("FB ctr: " + e.centerDistance(16, 32));

        System.out.println("depth: " + minWingDepth(e));
    }

    public static void main(String[] args) {
        // webDemo();
        // edgeCubeSolve();
        // equatorTest();
        cyclesTest();

        /*
        EdgeCube e = new EdgeCube();
        e.executeStr("Rw Lw'");
        e.print();
        */
    }
      
}

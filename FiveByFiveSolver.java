import java.util.*;
import java.awt.Desktop;
import java.net.URI;

public class FiveByFiveSolver {
    static int steps = 0;

    
    private static String solveCenters(Cube rootPzl) {
        final int MAXDEPTH = 1300;
        List<Stack<Cube>> openSetPzls = new ArrayList<Stack<Cube>>();
        for (int i=0; i<MAXDEPTH; i++) {
            openSetPzls.add(new Stack<Cube>());
        }
        List<Stack<Integer>> openSetMoves = new ArrayList<Stack<Integer>>();
        for (int i=0; i<MAXDEPTH; i++) {
            openSetMoves.add(new Stack<Integer>()); // g value
        }
        HashMap<Cube, Integer> closedSet = new HashMap<>();

        
        openSetPzls.get(rootPzl.h()).add(rootPzl);
        openSetMoves.get(rootPzl.h()).add(-1);
        int min_f = rootPzl.h();

        while (true) {
            while (openSetPzls.get(min_f).size() == 0) {
                min_f++;
            }
            Cube pzl = openSetPzls.get(min_f).pop();
            Integer moveIndex = openSetMoves.get(min_f).pop();
            int popped_f = min_f;

            if (closedSet.containsKey(pzl)) continue;
            closedSet.put(pzl, moveIndex);

            if (pzl.isSolved()) return getSolution(closedSet, rootPzl, pzl);

            int nextMoveIndex = 0;
            for (Cube nbr : pzl.getNeighbors()) {
                int new_f = nbr.h()+popped_f-pzl.h()+1;
                if (new_f < min_f) {
                    min_f = new_f;
                    // System.out.println(min_f);
                    // System.out.println(getSolution(closedSet, rootPzl, new CenterCube(pzl))+CenterCube.moveStr.get(i/3)+CenterCube.moveAmts.get(i%3));
                }
                if (!nbr.equals(pzl)) {
                    openSetPzls.get(new_f).push(nbr);
                    openSetMoves.get(new_f).push(nextMoveIndex); // number from 0 to 35 representing both the type and direction of the move
                }
                nextMoveIndex++;
            }
        }
    }

    private static String getSolution(HashMap<Cube, Integer> closedSet, Cube rootPzl, Cube pzl) {
        String solution = "";
        while (!pzl.equals(rootPzl)) {
            int moveData = closedSet.get(pzl);
            solution = pzl.executeInverse(moveData) + solution;
            steps++;
        }
        return solution;
    }
    
    public static void stressTest() {
        CenterCube pzl = new CenterCube();
        String[] scrambles = new String[] {"D2 Uw F R Uw U Rw' Dw' F D F' Rw2 Bw' Rw U Bw2 Dw F Rw Fw F2 R' Bw' L D R Rw' L Fw' B2 Dw Uw Rw' B' F2 U2 Bw2 Lw2 B Dw' Bw' B' Fw Dw' U2 Fw F Uw L' B2 D' B Bw D Dw R2 Bw2 F2 L2 Rw",
                              "Fw' R2 F U' Bw' D2 F2 Uw' B2 Uw2 Bw2 F' U' Bw2 Rw' B' R' Dw2 R' Dw' B2 F' Lw2 Dw D2 Fw' Bw D2 Lw2 U Uw2 R2 L' Fw R B' Lw R' D2 F B' Uw' L Dw B' Dw2 Bw' D R' L' Bw U' F2 Dw2 Fw' Dw F' Bw' B' L",
                              "R Rw2 Bw2 R' L Fw2 R D' Uw Bw' B2 D B2 L Fw2 L' Rw2 D' Dw2 U2 Bw' U' Bw2 L B' L' Dw Rw Uw2 Fw Uw' Dw L' Uw' D2 B2 Rw2 R2 Uw R' B U' Fw' U Fw2 B F R2 Bw' U2 B2 L2 F2 D2 B' U2 Bw' Dw' F L2",
                              "Lw' L' Uw2 R' L2 U' Fw2 Dw2 D' F Dw2 L Bw2 Rw' B' Lw2 Dw Fw2 L D' Dw2 L Dw R2 D2 F2 B2 Lw Uw' Fw2 Rw' F Fw2 Lw' Fw' L2 Uw' U Lw' F2 R' U L Uw' R' Fw' D Rw' Lw' L' Bw D' Bw2 L U' Uw' Bw R2 Uw' Bw'",
                              "Rw2 F' D2 L' Fw L2 U' Uw2 Lw' L Dw Bw' Rw2 R D U' Dw2 F' Lw2 F Fw' Rw D' U' Dw R' L' Bw' D' Uw R Bw' Fw' U' Bw' D Rw2 F' Lw' U' Rw' D R2 L B2 L' Fw2 U2 F Bw' L2 Fw2 B2 D2 Uw Bw R2 F R Rw'",
                              "Uw2 R' L U Bw2 Dw' Fw L U B' Dw' Uw F U' Bw' B' Dw D' B2 F2 Dw2 U F' L2 F' R2 U' B' L Lw2 Uw2 Lw' Fw2 D2 F' Dw L Uw' F' L' Lw R2 F' U' Lw' Uw Lw' Dw2 Rw' L' Fw2 B Bw' L' Uw' Fw' F' B' Rw' Lw'",
                              "F' B L' Lw2 Bw' B2 L' Rw2 U2 Uw' L2 B Fw2 U D2 L' D' Rw' Dw2 D2 Lw2 Dw D' L' B' Uw' Bw' D' Fw B R' Fw2 U F' Rw Fw' R2 Rw2 Fw2 Bw' U2 Rw2 Bw L' Fw Rw2 Lw2 Bw' B2 U Rw' R' L Lw Bw2 Uw' D U Bw' Dw2",
                              "F2 B2 L' Rw F Fw Bw2 Lw Fw2 F' U' Lw L2 D2 B Uw Rw' U F' R L' Fw' D2 Fw2 R U Rw2 R U Dw' L' Dw Rw' F2 D2 Lw2 L' R' U2 L Lw2 Rw R2 B2 Bw' Uw Rw' D' Uw' L B' L2 U' D Fw' U Fw2 Dw2 R2 Fw'",
                              "F D F2 U' B2 R2 U' Dw' Fw2 Lw2 Rw2 Uw' B U2 Rw2 Uw Fw' D Rw U2 D' F' Rw' F' L Bw D' R Fw Uw R2 Uw' Bw Fw R Bw U' Lw2 Bw' Uw' R' Rw Dw' L Rw2 Uw R2 Fw2 R' Uw Lw Dw' D U2 B' Fw' Uw Rw2 L Uw",
                              "Lw F2 Uw2 D2 Fw' Bw2 U' R Lw' Dw' Bw B2 R' Fw L' D Bw2 Rw Fw2 F Dw2 Lw' U2 Rw2 B' Bw' F Fw' Lw Rw2 Fw' L2 Rw Bw' Dw2 R F B2 Lw' R' Uw Bw F' Uw Dw U2 Rw' U' Lw2 Bw' U2 Lw' Bw B2 Fw L2 Fw2 Dw B2 Bw"};
        for (String scramble : scrambles) {
            // pzl = new CenterCube();
            pzl.executeStr(scramble);
            //System.out.println(scramble+"\n");
            System.out.println(solveCenters(pzl)+"\n");
            System.out.println(steps);
            System.out.println("\n\n-------------\n\n");
        }
    }

    public static void webDemo() {
        String setup = "Lw' Dw2 Uw R' Fw L2 U' Fw2 Dw2 D' F Dw2 L Bw2 Rw' B' Lw2 Dw Fw2 L D' Dw2 L Dw R2 D2 F2 B2 Lw Uw' Fw2 Rw' F Fw2 Lw' Fw' L2 Uw' U Lw' F2 R' U L Uw' R' Fw' D Rw' Lw' L' Bw D' Bw2 L U' Uw' Bw R2 Uw' Bw'";
        CenterCube pzl = new CenterCube();
        pzl.executeStr(setup);
        String alg = solveCenters(pzl);
        String demoURI = ("https://alg.cubing.net/?puzzle=5x5x5&alg="+alg+"&setup="+setup).replaceAll(" ","%20");;
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(demoURI);
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // stressTest();
        webDemo();
    }
}

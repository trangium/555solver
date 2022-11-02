import java.util.*;

public class FiveByFiveSolver {
    static int steps = 0;

    static final int UBL = 0;
    static final int UB = 1;
    static final int UBR = 2;
    static final int UR = 3;
    static final int UFR = 4;
    static final int UF = 5;
    static final int UFL = 6;
    static final int UL = 7;

    static final int LUB = 8;
    static final int LU = 9;
    static final int LUF = 10;
    static final int LF = 11;
    static final int LDF = 12;
    static final int LD = 13;
    static final int LDB = 14;
    static final int LB = 15;

    static final int FUL = 16;
    static final int FU = 17;
    static final int FUR = 18;
    static final int FR = 19;
    static final int FDR = 20;
    static final int FD = 21;
    static final int FDL = 22;
    static final int FL = 23;

    static final int RUF = 24;
    static final int RU = 25;
    static final int RUB = 26;
    static final int RB = 27;
    static final int RDB = 28;
    static final int RD = 29;
    static final int RDF = 30;
    static final int RF = 31;

    static final int BUR = 32;
    static final int BU = 33;
    static final int BUL = 34;
    static final int BL = 35;
    static final int BDL = 36;
    static final int BD = 37;
    static final int BDR = 38;
    static final int BR = 39;

    static final int DFL = 40;
    static final int DF = 41;
    static final int DFR = 42;
    static final int DR = 43;
    static final int DBR = 44;
    static final int DB = 45;
    static final int DBL = 46;
    static final int DL = 47;

    private static boolean pcsSolvedHTR(CenterCube pzl, int[] positions) { // reduce to HTR
        for (int pos : positions) {
            if (pzl.getPerm()[pos]%3 != CenterCube.solved[pos]%3) return false;
        }
        return true;
    }

    private static boolean anySetSolvedHTR(CenterCube pzl, int[][] positionsSet) {
        for (int[] positions : positionsSet) {
            if (pcsSolvedHTR(pzl, positions)) return true;
        }
        return false;
    }

    private static int countSolvedHTR(CenterCube pzl, int[] positions, int pseudo) {
        int acc = 0;
        for (int pos : positions) {
            if (pzl.getPerm()[pos]%3 == pseudo) acc += 1;
        }
        return acc;
    }

    private static boolean pcsSolvedPseudo(CenterCube pzl, int[] positions, int pseudo) { // reduce to HTR
        for (int pos : positions) {
            if (pzl.getPerm()[pos]%3 != pseudo) return false;
        }
        return true;
    }

    private static boolean anySetSolvedPseudo(CenterCube pzl, int[][] positionsSet, int pseudo) {
        for (int[] positions : positionsSet) {
            if (pcsSolvedPseudo(pzl, positions, pseudo)) return true;
        }
        return false;
    }

    private static double h_helper(CenterCube pzl) {
        if (!anySetSolvedHTR(pzl, new int[][] {{UB}, {UR}, {UF}, {UL}})) return 500;
        if (!anySetSolvedHTR(pzl, new int[][] {{UB, UBR, UR}, {UR, UFR, UF}, {UF, UFL, UL}, {UL, UBL, UB}})) return 499;
        if (!anySetSolvedHTR(pzl, new int[][] {{UB, UBR, UR, UFR, UF}, {UR, UFR, UF, UFL, UL}, {UF, UFL, UL, UBL, UB}, {UL, UBL, UB, UBR, UR}})) return 498;
        if (!anySetSolvedHTR(pzl, new int[][] {{DB}, {DR}, {DF}, {DL}})) return 497;
        if (!anySetSolvedHTR(pzl, new int[][] {{DB, DBL, DL}, {DF, DFL, DL}, {DB, DBR, DR}, {DF, DFR, DR}})) return 496;
        if (!anySetSolvedHTR(pzl, new int[][] {{DB, DBR, DR, DFR, DF}, {DR, DFR, DF, DFL, DL}, {DF, DFL, DL, DBL, DB}, {DL, DBL, DB, DBR, DR}})) return 495;
        if (!anySetSolvedHTR(pzl, new int[][] {{0, 1, 2, 3, 4, 5, 6, 7, 40, 41, 42, 43, 44, 45, 46, 47}})) {
            if (!anySetSolvedPseudo(pzl, new int[][] {{FUR, FR, FDR, BUR, BR, BDR}, {FUL, FL, FDL, BUL, BL, BDL}}, 0)) {
                int topHTR = countSolvedHTR(pzl, new int[] {0, 1, 2, 3, 4, 5, 6, 7}, 0);
                int bottomHTR = countSolvedHTR(pzl, new int[] {40, 41, 42, 43, 44, 45, 46, 47}, 0);
                if (topHTR != 8 && topHTR != 5) return 494;
                if (bottomHTR != 8 && bottomHTR != 5) return 493;
                if (topHTR != 8 && bottomHTR != 8) return 492;
                if (!anySetSolvedPseudo(pzl, new int[][] {{FUL, FU, FUR}, {FUR, FR, FDR}, {FDR, FD, FDL}, {FDL, FL, FUL}, {BUL, BU, BUR}, {BUR, BR, BDR}, {BDR, BD, BDL}, {BDL, BL, BUL}}, 0)) {
                    if (!anySetSolvedPseudo(pzl, new int[][] {{FUL, FU}, {FU, FUR}, {FUR, FR}, {FR, FDR}, {FDR, FD}, {FD, FDL}, {FDL, FL}, {FL, FUL}, {BUL, BU}, {BU, BUR}, {BUR, BR}, {BR, BDR}, {BDR, BD}, {BD, BDL}, {BDL, BL}, {BL, BUL}}, 0)) return 491;
                    return 490;
                }
                return 489;
            }
            return 488;
        }
        if (!anySetSolvedHTR(pzl, new int[][] {{LU}, {LF}, {LD}, {LB}})) return 487;
        if (!anySetSolvedHTR(pzl, new int[][] {{LU, LUF, LF}, {LF, LDF, LD}, {LD, LDB, LB}, {LB, LUB, LU}})) return 486;
        if (!anySetSolvedHTR(pzl, new int[][] {{LU, LUF, LF, LDF, LD}, {LF, LDF, LD, LDB, LB}, {LD, LDB, LB, LUB, LU}, {LB, LUB, LU, LUF, LF}})) return 485;
        if (!anySetSolvedHTR(pzl, new int[][] {{RU}, {RF}, {RD}, {RB}})) return 484;
        if (!anySetSolvedHTR(pzl, new int[][] {{RU, RUF, RF}, {RF, RDF, RD}, {RD, RDB, RB}, {RB, RUB, RU}})) return 483;
        if (!anySetSolvedHTR(pzl, new int[][] {{RU, RUF, RF, RDF, RD}, {RF, RDF, RD, RDB, RB}, {RD, RDB, RB, RUB, RU}, {RB, RUB, RU, RUF, RF}})) return 482;
        if (!anySetSolvedHTR(pzl, new int[][] {{8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28, 29, 30, 31}})) {
            if (!anySetSolvedPseudo(pzl, new int[][] {{FUL, FU, FUR}, {FUR, FR, FDR}, {FDR, FD, FDL}, {FDL, FL, FUL}}, 1)) return 481;
            if (!anySetSolvedPseudo(pzl, new int[][] {{BUL, BU, BUR}, {BUR, BR, BDR}, {BDR, BD, BDL}, {BDL, BL, BUL}}, 1)) return 480;
            return 479;
        }
        double acc = 478;

        double pair = 0.5;
        double line = 0.5;
        double badLine = 1;
        double veryBadLine = 5;
        double square = 4;
        double rectangle = 5;
        double badSquare = 8;
        double veryBadSquare = 7;
        double incongruTwo = 5;
        // if (anySetSolved(pzl, new int[][] {{RU, RUF, RF}, {RF, RDF, RD}, {RD, RDB, RB}, {RB, RUB, RU}})) acc -= dec;
        // if (anySetSolved(pzl, new int[][] {{LU, LUF, LF}, {LF, LDF, LD}, {LD, LDB, LB}, {LB, LUB, LU}})) acc -= dec;
        // if (anySetSolved(pzl, new int[][] {{UB, UBR, UR}, {UR, UFR, UF}, {UF, UFL, UL}, {UL, UBL, UB}})) acc -= dec;
        // if (anySetSolved(pzl, new int[][] {{DB, DBL, DL}, {DF, DFL, DL}, {DB, DBR, DR}, {DF, DFR, DR}})) acc -= dec;
        // if (anySetSolved(pzl, new int[][] {{FU, FUR, FR}, {FR, FDR, FD}, {FD, FDL, FL}, {FL, FUL, FU}})) acc -= dec;
        // if (anySetSolved(pzl, new int[][] {{BU, BUR, BR}, {BR, BDR, BD}, {BD, BDL, BL}, {BL, BUL, BU}})) acc -= dec;
        int[] faceBads = new int[6];
        for (int i=0; i<48; i+=8) {
            faceBads[i/8] = 0;
            for (int j=0; j<8; j+=2) {
                byte a = pzl.getPerm()[i+j]; // corner
                byte b = pzl.getPerm()[i+((j+1)%8)]; // edge
                byte c = pzl.getPerm()[i+((j+2)%8)]; // corner
                byte d = pzl.getPerm()[i+((j+3)%8)]; // edge
                byte e = pzl.getPerm()[i+((j+4)%8)]; // corner
                byte f = pzl.getPerm()[i+((j+5)%8)]; // edge
                byte g = pzl.getPerm()[i+((j+6)%8)]; // corner
                byte h = pzl.getPerm()[i+((j+7)%8)]; // edge
                byte m = CenterCube.solved[i+j];
                if (a==b) acc -= pair;
                if (b==c) acc -= pair;
                if (a==m && b==m && c==m) acc -= line;
                if (a==m && b!=m && c==m) acc += badLine;
                if (b==m && c==m && d==m) acc -= square;
                if (b==m && c!=m && d==m) acc += badSquare;
                if (b==m && c==m && d==m && e==m && f==m) {
                    acc -= rectangle;
                    if (a==m && h!=m && g!=m) faceBads[i/8] = 10;
                    if (g==m && h!=m && a!=m) faceBads[i/8] = 100;
                    if (a!=m && h==m && g==m) acc += veryBadSquare;
                    if (a==m && h!=m && g==m) acc += veryBadLine;
                }
            }
        }
        if (faceBads[0] + faceBads[5] == 110) acc += incongruTwo;
        if (faceBads[1] + faceBads[3] == 110) acc += incongruTwo;
        if (faceBads[2] + faceBads[4] == 110) acc += incongruTwo;
        
        if (!pzl.isSolved()) return (int)acc;
        return 0;
    }

    private static int h(CenterCube pzl) {
        return (int)(h_helper(pzl) * 2);
    }

    private static String solveCenters(CenterCube rootPzl) {
        final int MAXDEPTH = 4020;
        List<Stack<CenterCube>> openSetPzls = new ArrayList<Stack<CenterCube>>();
        for (int i=0; i<MAXDEPTH; i++) {
            openSetPzls.add(new Stack<CenterCube>());
        }
        List<Stack<Integer>> openSetMoves = new ArrayList<Stack<Integer>>();
        for (int i=0; i<MAXDEPTH; i++) {
            openSetMoves.add(new Stack<Integer>()); // g value
        }
        HashMap<CenterCube, Integer> closedSet = new HashMap<>();

        
        openSetPzls.get(h(rootPzl)).add(rootPzl);
        openSetMoves.get(h(rootPzl)).add(-1);
        int min_f = h(rootPzl);

        while (true) {
            steps++;
            while (openSetPzls.get(min_f).size() == 0) {
                min_f++;
            }
            CenterCube pzl = openSetPzls.get(min_f).pop();
            Integer moveIndex = openSetMoves.get(min_f).pop();
            int popped_f = min_f;

            if (closedSet.containsKey(pzl)) continue;
            closedSet.put(pzl, moveIndex);

            if (pzl.isSolved()) return getSolution(closedSet, rootPzl, pzl);

            for (int nextMoveIndex=0; nextMoveIndex < CenterCube.numberOfMoves; nextMoveIndex++) {
                for (int rot=0; rot<=2; rot++) {
                    CenterCube nbr = new CenterCube(pzl);
                    nbr.executeMove(nextMoveIndex, rot);
                    int new_f = h(nbr)+popped_f-h(pzl)+1;
                    if (new_f < min_f) {
                        min_f = new_f;
                        // System.out.println(min_f);
                        // System.out.println(steps);
                        // System.out.println(getSolution(closedSet, rootPzl, new CenterCube(pzl))+CenterCube.moveStr.get(nextMoveIndex)+CenterCube.moveAmts.get(rot));
                    }
                    if (!nbr.equals(pzl)) {
                        openSetPzls.get(new_f).push(nbr);
                        openSetMoves.get(new_f).push(nextMoveIndex*3+rot); // number from 1 to 36 representing both the type and direction of the move
                    }
                }
            }
        }
    }

    private static String getSolution(HashMap<CenterCube, Integer> closedSet, CenterCube rootPzl, CenterCube pzl) {
        String solution = "";
        while (!pzl.equals(rootPzl)) {
            int moveData = closedSet.get(pzl);
            int rot = moveData%3;
            int ind = moveData/3;
            solution = CenterCube.moveStr.get(ind) + CenterCube.moveAmts.get(rot) + " " + solution;
            pzl.executeStr(CenterCube.moveStr.get(ind) + CenterCube.moveAmts.get(2-rot));
        }
        return solution;
    }

    /*
     * 
    if not isSolvable(root, goal): return "X"
    openSet = [[] for i in range(160)] # create an openSet with root
    openSet[h(root)].append((root, None))
    min_f = h(root)
    closedSet = {} # create an empty closedSet

    while True:
        while not openSet[min_f]:
            min_f += 1
        pzl, direc = openSet[min_f].pop()
        if pzl in closedSet: continue
        closedSet[pzl] = direc
        if pzl == goal: return findCompactPath(root, goal, closedSet)
        
        for nbr in nbrs(pzl):
            newF = min_f - distanceTable[nbr[holePos]][posFrom] + distanceTable[nbr[holePos]][holePos] + 1
            openSet[newF].append((nbr, direc))
     */

    public static void main(String[] args) {
        CenterCube pzl;
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
            pzl = new CenterCube();
            pzl.executeStr(scramble);
            System.out.println(scramble+"\n");
            System.out.println(solveCenters(pzl));
            System.out.println("\n\n-------------\n\n");
        }
    }
}

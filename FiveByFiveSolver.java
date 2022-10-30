import java.util.*;

public class FiveByFiveSolver {
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
            if (pzl.getPerm()[pos] != CenterCube.solved[pos]) return false;
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
            if (pzl.getPerm()[pos] == pseudo) acc += 1;
        }
        return acc;
    }

    private static boolean pcsSolvedWeird(CenterCube pzl, int[] positions, int pseudo) { // reduce to HTR
        for (int pos : positions) {
            if (pzl.getPerm()[pos] != pseudo) return false;
        }
        return true;
    }

    private static boolean anySetSolvedWeird(CenterCube pzl, int[][] positionsSet, int pseudo) {
        for (int[] positions : positionsSet) {
            if (pcsSolvedWeird(pzl, positions, pseudo)) return true;
        }
        return false;
    }

    private static int h_helper(CenterCube pzl) {
        if (!anySetSolvedHTR(pzl, new int[][] {{UB}, {UR}, {UF}, {UL}})) return 4000;
        if (!anySetSolvedHTR(pzl, new int[][] {{UB, UBR, UR}, {UR, UFR, UF}, {UF, UFL, UL}, {UL, UBL, UB}})) return 3900;
        if (!anySetSolvedHTR(pzl, new int[][] {{UB, UBR, UR, UFR, UF}, {UR, UFR, UF, UFL, UL}, {UF, UFL, UL, UBL, UB}, {UL, UBL, UB, UBR, UR}})) return 3800;
        if (!anySetSolvedHTR(pzl, new int[][] {{DB}, {DR}, {DF}, {DL}})) return 3700;
        if (!anySetSolvedHTR(pzl, new int[][] {{DB, DBL, DL}, {DF, DFL, DL}, {DB, DBR, DR}, {DF, DFR, DR}})) return 3600;
        if (!anySetSolvedHTR(pzl, new int[][] {{DB, DBR, DR, DFR, DF}, {DR, DFR, DF, DFL, DL}, {DF, DFL, DL, DBL, DB}, {DL, DBL, DB, DBR, DR}})) return 3500;
        if (!anySetSolvedHTR(pzl, new int[][] {{0, 1, 2, 3, 4, 5, 6, 7, 40, 41, 42, 43, 44, 45, 46, 47}})) {
            if (!anySetSolvedWeird(pzl, new int[][] {{FUR, FR, FDR, BUR, BR, BDR}, {FUL, FL, FDL, BUL, BL, BDL}}, 0)) {
                int topHTR = countSolvedHTR(pzl, new int[] {0, 1, 2, 3, 4, 5, 6, 7}, 0);
                int bottomHTR = countSolvedHTR(pzl, new int[] {40, 41, 42, 43, 44, 45, 46, 47}, 0);
                if (topHTR != 8 && topHTR != 5) return 3400;
                if (bottomHTR != 8 && bottomHTR != 5) return 3300;
                if (topHTR != 8 && bottomHTR != 8) return 3200;
                if (!anySetSolvedWeird(pzl, new int[][] {{FUL, FU, FUR}, {FUR, FR, FDR}, {FDR, FD, FDL}, {FDL, FL, FUL}, {BUL, BU, BUR}, {BUR, BR, BDR}, {BDR, BD, BDL}, {BDL, BL, BUL}}, 0)) {
                    if (!anySetSolvedWeird(pzl, new int[][] {{FUL, FU}, {FU, FUR}, {FUR, FR}, {FR, FDR}, {FDR, FD}, {FD, FDL}, {FDL, FL}, {FL, FUL}, {BUL, BU}, {BU, BUR}, {BUR, BR}, {BR, BDR}, {BDR, BD}, {BD, BDL}, {BDL, BL}, {BL, BUL}}, 0)) return 3100;
                    return 3000;
                }
                return 2900;
            }
            return 2800;
        }
        if (!anySetSolvedHTR(pzl, new int[][] {{LU}, {LF}, {LD}, {LB}})) return 2700;
        if (!anySetSolvedHTR(pzl, new int[][] {{LU, LUF, LF}, {LF, LDF, LD}, {LD, LDB, LB}, {LB, LUB, LU}})) return 2600;
        if (!anySetSolvedHTR(pzl, new int[][] {{LU, LUF, LF, LDF, LD}, {LF, LDF, LD, LDB, LB}, {LD, LDB, LB, LUB, LU}, {LB, LUB, LU, LUF, LF}})) return 2500;
        if (!anySetSolvedHTR(pzl, new int[][] {{RU}, {RF}, {RD}, {RB}})) return 2400;
        if (!anySetSolvedHTR(pzl, new int[][] {{RU, RUF, RF}, {RF, RDF, RD}, {RD, RDB, RB}, {RB, RUB, RU}})) return 2300;
        if (!anySetSolvedHTR(pzl, new int[][] {{RU, RUF, RF, RDF, RD}, {RF, RDF, RD, RDB, RB}, {RD, RDB, RB, RUB, RU}, {RB, RUB, RU, RUF, RF}})) return 2200;
        if (!anySetSolvedHTR(pzl, new int[][] {{8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28, 29, 30, 31}})) {
            if (!anySetSolvedWeird(pzl, new int[][] {{FUL, FU, FUR}, {FUR, FR, FDR}, {FDR, FD, FDL}, {FDL, FL, FUL}}, 1)) return 2100;
            if (!anySetSolvedWeird(pzl, new int[][] {{BUL, BU, BUR}, {BUR, BR, BDR}, {BDR, BD, BDL}, {BDL, BL, BUL}}, 1)) return 2000;
            return 1900;
        }
        if (!isSolved(pzl)) return 1800;
        return 0;
    }

    private static int h(CenterCube pzl) {
        return h_helper(pzl) / 50;
    }

    private static boolean isSolved(CenterCube pzl) {
        for (int i=0; i<CenterCube.numberOfPieces; i++) {
            if (pzl.getPerm()[i] != CenterCube.solved[i]) return false;
        }
        return true;
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
            while (openSetPzls.get(min_f).size() == 0) {
                min_f++;
            }
            CenterCube pzl = openSetPzls.get(min_f).pop();
            Integer moveIndex = openSetMoves.get(min_f).pop();
            int popped_f = min_f;

            if (closedSet.containsKey(pzl)) continue;
            closedSet.put(pzl, moveIndex);

            if (isSolved(pzl)) return getSolution(closedSet, rootPzl, pzl);

            for (int nextMoveIndex=0; nextMoveIndex < CenterCube.numberOfMoves; nextMoveIndex++) {
                for (int rot=0; rot<=2; rot++) {
                    CenterCube nbr = new CenterCube(pzl);
                    nbr.executeMove(nextMoveIndex, rot);
                    int new_f = h(nbr)+popped_f-h(pzl)+1;
                    if (new_f < min_f) {
                        min_f = new_f;
                        // System.out.println(min_f);
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
        CenterCube pzl = new CenterCube();
        String scramble = "D2 Uw F R Uw U Rw' Dw' F D F' Rw2 Bw' Rw U Bw2 Dw F Rw Fw F2 R' Bw' L D R Rw' L Fw' B2 Dw Uw Rw' B' F2 U2 Bw2 Lw2 B Dw' Bw' B' Fw Dw' U2 Fw F Uw L' B2 D' B Bw D Dw R2 Bw2 F2 L2 Rw";

        pzl.executeStr(scramble);
        System.out.println(scramble+"\n");
        System.out.println(solveCenters(pzl));
    }
}

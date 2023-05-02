import java.util.*;

public class CenterCube extends Cube {
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
    private byte[] perm;
    private static final int[][][] moves = {
        {{UBL, UBR, UFR, UFL}, {UB, UR, UF, UL}}, // U
        {{RUF, RUB, RDB, RDF}, {RU, RB, RD, RF}}, // R
        {{FUL, FUR, FDR, FDL}, {FU, FR, FD, FL}}, // F
        {{DFL, DFR, DBR, DBL}, {DF, DR, DB, DL}}, // D
        {{LUB, LUF, LDF, LDB}, {LU, LF, LD, LB}}, // L
        {{BUR, BUL, BDL, BDR}, {BU, BL, BD, BR}}, // B
        {{UBL, UBR, UFR, UFL}, {UB, UR, UF, UL}, {LUB, BUR, RUF, FUL}, {LU, BU, RU, FU}, {LUF, BUL, RUB, FUR}}, // Uw
        {{RUF, RUB, RDB, RDF}, {RU, RB, RD, RF}, {BUR, DBR, FDR, UFR}, {BR, DR, FR, UR}, {BDR, DFR, FUR, UBR}}, // Rw
        {{FUL, FUR, FDR, FDL}, {FU, FR, FD, FL}, {UFR, RDF, DFL, LUF}, {UF, RF, DF, LF}, {UFL, RUF, DFR, LDF}}, // Fw
        {{DFL, DFR, DBR, DBL}, {DF, DR, DB, DL}, {BDL, LDF, FDR, RDB}, {BD, LD, FD, RD}, {BDR, LDB, FDL, RDF}}, // Dw
        {{LUB, LUF, LDF, LDB}, {LU, LF, LD, LB}, {UBL, FUL, DFL, BDL}, {UL, FL, DL, BL}, {UFL, FDL, DBL, BUL}}, // Lw
        {{BUR, BUL, BDL, BDR}, {BU, BL, BD, BR}, {UBL, LDB, DBR, RUB}, {UB, LB, DB, RB}, {UBR, LUB, DBL, RDB}}, // Bw
    };
    public static final List<String> moveStr = Arrays.asList("U", "R", "F", "D", "L", "B", "Uw", "Rw", "Fw", "Dw", "Lw", "Bw");
    public static final List<String> moveAmts = Arrays.asList("", "2", "'");
    public static final int numberOfMoves = moves.length;
    public static final byte[] solved = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int numberOfPieces = solved.length-1;
    private static final int[] blockTable = {0, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 3, 2, 2, 1, 2, 2, 1, 2, 3, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 3, 3, 2, 2, 3, 3, 1, 2, 2, 2, 2, 3, 1, 2, 1, 2, 2, 2, 2, 3, 2, 1, 1, 2, 2, 2, 2, 3, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 3, 4, 3, 3, 2, 2, 2, 2, 3, 3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 2, 2, 3, 3, 2, 2, 3, 3, 2, 3, 3, 3, 3, 4, 2, 3, 2, 2, 3, 3, 3, 3, 2, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 1, 2, 1, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 3, 3, 4, 4, 3, 3, 4, 4, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 3, 3, 3, 3, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 3, 2, 3, 3, 4, 3, 3, 1, 2, 2, 1, 2, 3, 2, 2, 2, 3, 3, 3, 2, 3, 3, 3, 3, 3, 4, 4, 3, 3, 4, 4, 2, 3, 3, 3, 3, 4, 2, 3, 2, 3, 3, 2, 2, 3, 3, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 3, 2, 3, 3, 4, 3, 3, 2, 3, 3, 2, 3, 4, 3, 3, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 3, 3, 2, 2, 3, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 3, 2, 2, 3, 3, 2, 2, 3, 3, 3, 3, 4, 3, 3, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 3, 4, 4, 5, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 2, 3, 3, 3, 2, 3, 3, 3, 2, 2, 3, 3, 2, 2, 3, 3, 3, 4, 3, 4, 3, 4, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 3, 2, 3, 3, 4, 3, 3, 2, 3, 3, 2, 3, 4, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 3, 3, 4, 4, 3, 3, 4, 4, 1, 2, 2, 2, 2, 3, 1, 2, 2, 2, 3, 3, 3, 3, 2, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 3, 2, 3, 3, 4, 3, 3, 2, 3, 3, 2, 3, 4, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 3, 3, 4, 4, 3, 3, 4, 4, 2, 3, 3, 3, 3, 4, 2, 3, 1, 2, 2, 2, 2, 3, 2, 1};

    public CenterCube() {
        perm = new byte[solved.length];
        for (int i=0; i<solved.length; i++) {
            perm[i] = solved[i];
        }
    }

    public CenterCube(CenterCube other) {
        perm = new byte[solved.length];
        for (int i=0; i<solved.length; i++) {
            perm[i] = other.perm[i];
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        else if (!(other instanceof CenterCube)) return false;
        return Arrays.equals(this.perm, ((CenterCube)(other)).perm);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(perm);
    }

    public void print() {
        String acc = "{";
        for (int i : perm) {
            acc = acc + i + ", ";
        }
        System.out.println(acc.substring(0, acc.length()-2) + "}");
    }

    public boolean isSolved() {
        for (int i=0; i<solved.length; i++) {
            if (perm[i] != solved[i]) return false;
        }
        return true;
    }

    public byte[] getPerm() {
        return perm;
    }

    private void cycle(int[] positions, int amount) {
        assert positions.length == 4;
        byte temp;
        switch (amount) {
            case 2: // inverse
                temp = perm[positions[0]];
                perm[positions[0]] = perm[positions[1]];
                perm[positions[1]] = perm[positions[2]];
                perm[positions[2]] = perm[positions[3]];
                perm[positions[3]] = temp;
                return;
            case 1: // double
                temp = perm[positions[0]];
                perm[positions[0]] = perm[positions[2]];
                perm[positions[2]] = temp;
                temp = perm[positions[1]];
                perm[positions[1]] = perm[positions[3]];
                perm[positions[3]] = temp;
                return;
            case 0: // clockwise
                temp = perm[positions[0]];
                perm[positions[0]] = perm[positions[3]];
                perm[positions[3]] = perm[positions[2]];
                perm[positions[2]] = perm[positions[1]];
                perm[positions[1]] = temp;
                return;
        }
    }

    public void swap(int p1, int p2) {
        byte temp = perm[p1];
        perm[p1] = perm[p2];
        perm[p2] = temp;
    }

    public void executeMove(int moveIndex, int amount) {
        for (int[] positions : moves[moveIndex]) {
            cycle(positions, amount);
        }
        if (moveIndex >= 6 && amount != 1) {
            perm[numberOfPieces] ^= 1;
        }
    }

    public void executeStr(String sequence) {
        for (String move : sequence.split(" ")) {
            switch (move.charAt(move.length()-1)) {
                case '\'': // counterclockwise
                    executeMove(moveStr.indexOf(move.substring(0, move.length()-1)), 2);
                    break;
                case '2':
                    executeMove(moveStr.indexOf(move.substring(0, move.length()-1)), 1);
                    break;
                default:
                    executeMove(moveStr.indexOf(move), 0);
                    break;
            }
        }
    }

    public CenterCube[] getNeighbors() {
        CenterCube[] nbrs = new CenterCube[36];
        int i = 0;
        for (int nextMoveIndex=0; nextMoveIndex < CenterCube.numberOfMoves; nextMoveIndex++) {
            for (int rot=0; rot<=2; rot++) {
                CenterCube nbr = new CenterCube(this);
                nbr.executeMove(nextMoveIndex, rot);
                nbrs[i] = nbr;
                i++;
            }
        }
        return nbrs;
    }

    // determines whether the move of getNeighbors[i1] and the move of getNeighbors[i2] commute.
    private boolean commutes(int i1, int i2) {
        if (i1 < 18 && i1 < 18) return true;
        return ((i1/3)%3 == (i2/3)%3);
    }

    // potential speed improvement: extract to lookup table
    public boolean isValidPair(int i1, int i2) {
        if (!commutes(i1, i2)) return true;
        return (i1/3 < i2/3);
    }

    // public String moveListToStr(int[] sequence) {
    //     String acc = "";
    //     for (int move : sequence) {
    //         acc += moveStr.get(move/3);
    //         if (acc%3)
    //     }
    //     return acc.substring(0, acc.length()-1); 
    // }

    public String executeInverse(int moveIndex) {
        int rot = moveIndex%3;
        int ind = moveIndex/3;
        String solutionMove = CenterCube.moveStr.get(ind) + CenterCube.moveAmts.get(rot) + " ";
        this.executeStr(CenterCube.moveStr.get(ind) + CenterCube.moveAmts.get(2-rot));
        return solutionMove;
    }

    private int pcWeight(int pos, int bitIndex, int target) {
        if (perm[pos] == target) return (1 << bitIndex);
        return 0;
    }

    public int blockCount(int target) {
        int blocks = 0;
        for (int pos=0; pos<numberOfPieces-1; pos += 8) {
            int faceIndex = pcWeight(pos, 8, target) + pcWeight(pos+1, 7, target)
                        + pcWeight(pos+2, 6, target) + pcWeight(pos+7, 5, target)
                        + pcWeight(pos+3, 3, target) + pcWeight(pos+6, 2, target)
                        + pcWeight(pos+5, 1, target) + pcWeight(pos+4, 0, target);
            if (solved[pos] == target) faceIndex += 1 << 4;
            blocks += blockTable[faceIndex];
        }
        return blocks;
    }

    public double scaleHeuristic(double x) {
        if (x <= 12) return x;
        return scaleHeuristic(12) + 0.5*(x-12);
    }

    public double h() {
        int plusDepth = CenterPrun.plusDepthTable[CenterPrun.plusCtrIndex(perm)];
        int xDepth = CenterPrun.xDepthTable[CenterPrun.xCtrIndex(perm)];
        if (plusDepth != 0 || xDepth != 0) {
            return scaleHeuristic(23) + Math.max(plusDepth, xDepth) + blockCount(0);
        }
        if (isSolved()) return 0;
        return Math.max(1, scaleHeuristic(blockCount(1) + blockCount(2)));
    }
}

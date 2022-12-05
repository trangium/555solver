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
    public static final byte[] solved = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int numberOfPieces = solved.length;

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
            case 2: // clockwise
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
            case 0: // inverse
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

    private boolean pcsSolvedHTR(int[] positions) { // reduce to HTR
        for (int pos : positions) {
            if (perm[pos]%3 != solved[pos]%3) return false;
        }
        return true;
    }

    private boolean anySetSolvedHTR(int[][] positionsSet) {
        for (int[] positions : positionsSet) {
            if (pcsSolvedHTR(positions)) return true;
        }
        return false;
    }

    private int countSolvedHTR(int[] positions, int pseudo) {
        int acc = 0;
        for (int pos : positions) {
            if (perm[pos]%3 == pseudo) acc += 1;
        }
        return acc;
    }


    private boolean pcsSolvedPseudo(int[] positions, int pseudo) { // reduce to HTR
        for (int pos : positions) {
            if (perm[pos]%3 != pseudo) return false;
        }
        return true;
    }

    private boolean anySetSolvedPseudo(int[][] positionsSet, int pseudo) {
        for (int[] positions : positionsSet) {
            if (pcsSolvedPseudo(positions, pseudo)) return true;
        }
        return false;
    }

    private double h_helper() {
        if (!anySetSolvedHTR(new int[][] {{UB}, {UR}, {UF}, {UL}})) return 500;
        if (!anySetSolvedHTR(new int[][] {{UB, UBR, UR}, {UR, UFR, UF}, {UF, UFL, UL}, {UL, UBL, UB}})) return 499;
        if (!anySetSolvedHTR(new int[][] {{UB, UBR, UR, UFR, UF}, {UR, UFR, UF, UFL, UL}, {UF, UFL, UL, UBL, UB}, {UL, UBL, UB, UBR, UR}})) return 498;
        if (!anySetSolvedHTR(new int[][] {{DB}, {DR}, {DF}, {DL}})) return 497;
        if (!anySetSolvedHTR(new int[][] {{DB, DBL, DL}, {DF, DFL, DL}, {DB, DBR, DR}, {DF, DFR, DR}})) return 496;
        if (!anySetSolvedHTR(new int[][] {{DB, DBR, DR, DFR, DF}, {DR, DFR, DF, DFL, DL}, {DF, DFL, DL, DBL, DB}, {DL, DBL, DB, DBR, DR}})) return 495;
        if (!anySetSolvedHTR(new int[][] {{0, 1, 2, 3, 4, 5, 6, 7, 40, 41, 42, 43, 44, 45, 46, 47}})) {
            if (!anySetSolvedPseudo(new int[][] {{FUR, FR, FDR, BUR, BR, BDR}, {FUL, FL, FDL, BUL, BL, BDL}}, 0)) {
                int topHTR = countSolvedHTR(new int[] {0, 1, 2, 3, 4, 5, 6, 7}, 0);
                int bottomHTR = countSolvedHTR(new int[] {40, 41, 42, 43, 44, 45, 46, 47}, 0);
                if (topHTR != 8 && topHTR != 5) return 494;
                if (bottomHTR != 8 && bottomHTR != 5) return 493;
                if (topHTR != 8 && bottomHTR != 8) return 492;
                if (!anySetSolvedPseudo(new int[][] {{FUL, FU, FUR}, {FUR, FR, FDR}, {FDR, FD, FDL}, {FDL, FL, FUL}, {BUL, BU, BUR}, {BUR, BR, BDR}, {BDR, BD, BDL}, {BDL, BL, BUL}}, 0)) {
                    if (!anySetSolvedPseudo(new int[][] {{FUL, FU}, {FU, FUR}, {FUR, FR}, {FR, FDR}, {FDR, FD}, {FD, FDL}, {FDL, FL}, {FL, FUL}, {BUL, BU}, {BU, BUR}, {BUR, BR}, {BR, BDR}, {BDR, BD}, {BD, BDL}, {BDL, BL}, {BL, BUL}}, 0)) return 491;
                    return 490;
                }
                return 489;
            }
            return 488;
        }
        if (!anySetSolvedHTR(new int[][] {{LU}, {LF}, {LD}, {LB}})) return 487;
        if (!anySetSolvedHTR(new int[][] {{LU, LUF, LF}, {LF, LDF, LD}, {LD, LDB, LB}, {LB, LUB, LU}})) return 486;
        if (!anySetSolvedHTR(new int[][] {{LU, LUF, LF, LDF, LD}, {LF, LDF, LD, LDB, LB}, {LD, LDB, LB, LUB, LU}, {LB, LUB, LU, LUF, LF}})) return 485;
        if (!anySetSolvedHTR(new int[][] {{RU}, {RF}, {RD}, {RB}})) return 484;
        if (!anySetSolvedHTR(new int[][] {{RU, RUF, RF}, {RF, RDF, RD}, {RD, RDB, RB}, {RB, RUB, RU}})) return 483;
        if (!anySetSolvedHTR(new int[][] {{RU, RUF, RF, RDF, RD}, {RF, RDF, RD, RDB, RB}, {RD, RDB, RB, RUB, RU}, {RB, RUB, RU, RUF, RF}})) return 482;
        if (!anySetSolvedHTR(new int[][] {{8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28, 29, 30, 31}})) {
            if (!anySetSolvedPseudo(new int[][] {{FUL, FU, FUR}, {FUR, FR, FDR}, {FDR, FD, FDL}, {FDL, FL, FUL}}, 1)) return 481;
            if (!anySetSolvedPseudo(new int[][] {{BUL, BU, BUR}, {BUR, BR, BDR}, {BDR, BD, BDL}, {BDL, BL, BUL}}, 1)) return 480;
            return 479;
        }
        if (!this.isSolved()) return 478;
        return 0;
    }

    public int h() {
        return (int)(h_helper() * 2);
    }
}

import java.util.*;

public class EdgeCube extends Cube { // currently a CenterCube. Change all the methods.
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


    static final int UBl = 0;
    static final int UBr = 1;
    static final int URb = 2;
    static final int URf = 3;
    static final int UFr = 4;
    static final int UFl = 5;
    static final int ULf = 6;
    static final int ULb = 7;
    static final int FLd = 8;
    static final int FLu = 9;
    static final int FRu = 10;
    static final int FRd = 11;
    static final int BRd = 12;
    static final int BRu = 13;
    static final int BLu = 14;
    static final int BLd = 15;
    static final int DFl = 16;
    static final int DFr = 17;
    static final int DRf = 18;
    static final int DRb = 19;
    static final int DBr = 20;
    static final int DBl = 21;
    static final int DLb = 22;
    static final int DLf = 23;

    static final int UBm = 0;
    static final int URs = 1;
    static final int UFm = 2;
    static final int ULs = 3;
    static final int FLe = 4;
    static final int FRe = 5;
    static final int BRe = 6;
    static final int BLe = 7;
    static final int DFm = 8;
    static final int DRs = 9;
    static final int DBm = 10;
    static final int DLs = 11; 

    private byte[] centerPerm;
    private byte[] wingPerm; 
    private byte[] midgePerm; // if a midge is flipped, toggle its last bit

    private static final int[][][] centerMoves = {
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

    private static final int[][][] wingMoves = {
        {{UBl, URb, UFr, ULf}, {UBr, URf, UFl, ULb}}, // U
        {{URf, BRu, DRb, FRd}, {URb, BRd, DRf, FRu}}, // R
        {{UFl, FRu, DFr, FLd}, {UFr, FRd, DFl, FLu}}, // F
        {{DFl, DRf, DBr, DLb}, {DFr, DRb, DBl, DLf}}, // D
        {{ULf, FLd, DLb, BLu}, {ULb, FLu, DLf, BLd}}, // L
        {{UBl, BLd, DBr, BRu}, {UBr, BLu, DBl, BRd}}, // B
        {{UBl, URb, UFr, ULf}, {UBr, URf, UFl, ULb}, {FRu, FLu, BLu, BRu}}, // Uw
        {{URf, BRu, DRb, FRd}, {URb, BRd, DRf, FRu}, {UFr, UBr, DBr, DFr}}, // Rw
        {{UFl, FRu, DFr, FLd}, {UFr, FRd, DFl, FLu}, {URf, DRf, DLf, ULf}}, // Fw
        {{DFl, DRf, DBr, DLb}, {DFr, DRb, DBl, DLf}, {FRd, BRd, BLd, FLd}}, // Dw
        {{ULf, FLd, DLb, BLu}, {ULb, FLu, DLf, BLd}, {UFl, DFl, DBl, UBl}}, // Lw
        {{UBl, BLd, DBr, BRu}, {UBr, BLu, DBl, BRd}, {URb, ULb, DLb, DRb}}, // Bw
    };

    private static final int[][][] midgeMoves = {
        {{UBm, URs, UFm, ULs}}, // U
        {{URs, BRe, DRs, FRe}}, // R
        {{UFm, FRe, DFm, FLe}}, // F
        {{DFm, DRs, DBm, DLs}}, // D
        {{ULs, FLe, DLs, BLe}}, // L
        {{UBm, BLe, DBm, BRe}}, // B
        {{UBm, URs, UFm, ULs}}, // Uw
        {{URs, BRe, DRs, FRe}}, // Rw
        {{UFm, FRe, DFm, FLe}}, // Fw
        {{DFm, DRs, DBm, DLs}}, // Dw
        {{ULs, FLe, DLs, BLe}}, // Lw
        {{UBm, BLe, DBm, BRe}}, // Bw
    };

    public static final List<String> moveStr = Arrays.asList("U", "R", "F", "D", "L", "B", "Uw", "Rw", "Fw", "Dw", "Lw", "Bw");
    public static final List<String> moveAmts = Arrays.asList("", "2", "'");
    public static final int numberOfMoves = centerMoves.length;
    public static final byte[] centerSolved = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3};
    public static final byte[] wingSolved = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    public static final byte[] midgeSolved = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22};
    public static final int numberOfCenters = centerSolved.length;
    public static final int numberOfWings = wingSolved.length;
    public static final int numberOfMidges = midgeSolved.length;

    public EdgeCube() {
        centerPerm = new byte[centerSolved.length];
        for (int i=0; i<centerSolved.length; i++) centerPerm[i] = centerSolved[i];
        wingPerm = new byte[wingSolved.length];
        for (int i=0; i<wingSolved.length; i++) wingPerm[i] = wingSolved[i];
        midgePerm = new byte[midgeSolved.length];
        for (int i=0; i<midgeSolved.length; i++) midgePerm[i] = midgeSolved[i];
    }

    public EdgeCube(EdgeCube other) {
        centerPerm = new byte[centerSolved.length];
        for (int i=0; i<centerSolved.length; i++) centerPerm[i] = other.centerPerm[i];
        wingPerm = new byte[wingSolved.length];
        for (int i=0; i<wingSolved.length; i++) wingPerm[i] = other.wingPerm[i];
        midgePerm = new byte[midgeSolved.length];
        for (int i=0; i<midgeSolved.length; i++) midgePerm[i] = other.midgePerm[i];
    }
    
    public byte[] getCenterPerm() {
        return centerPerm;
    }

    public byte[] getWingPerm() {
        return wingPerm;
    }

    public byte[] getMidgePerm() {
        return midgePerm;
    }

    public byte[] getWingCycles() {
        byte[] invMidges = new byte[numberOfWings];
        for (byte i=0; i<numberOfWings; i++) {
            int val = midgePerm[i >> 1] ^ (i & 1);
            invMidges[val] = i;
        }
        byte[] wingCycles = new byte[numberOfWings];
        int idx = 0;
        for (byte val : wingPerm) {
            wingCycles[idx] = invMidges[val];
            idx++;
        }
        return wingCycles;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        else if (!(other instanceof EdgeCube)) return false;
        return Arrays.equals(this.centerPerm, ((EdgeCube)(other)).centerPerm) && Arrays.equals(this.wingPerm, ((EdgeCube)(other)).wingPerm) && Arrays.equals(this.midgePerm, ((EdgeCube)(other)).midgePerm);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.centerPerm) ^ Arrays.hashCode(this.wingPerm) ^ Arrays.hashCode(this.midgePerm);
    }

    public void print() {
        String acc = "{";
        for (int i : centerPerm) acc = acc + i + ", ";
        acc = acc.substring(0, acc.length()-2) + "}, {";
        for (int i : wingPerm) acc = acc + i + ", ";
        acc = acc.substring(0, acc.length()-2) + "}, {";
        for (int i : midgePerm) acc = acc + i + ", ";
        System.out.println(acc.substring(0, acc.length()-2) + "}");
    }

    public void printCycles() {
        String acc = "{";
        for (int i : getWingCycles()) acc = acc + i + ", ";
        System.out.println(acc.substring(0, acc.length()-2) + "}");
    }

    public boolean isSolved() { 
        for (int i=0; i<centerSolved.length; i++) {
            if (centerPerm[i] != centerSolved[i]) return false;
        }
        byte i=0;
        for (byte val : this.getWingCycles()) {
            if (val != i) return false;
            i++;
        }
        return true;
    }

    private void cycle(byte[] pieceArr, int[] positions, int amount, boolean flip) { 
        byte temp;
        if (positions.length == 4) {
            switch (amount) {
                case 2: // clockwise
                    temp = pieceArr[positions[0]];
                    pieceArr[positions[0]] = pieceArr[positions[1]];
                    pieceArr[positions[1]] = pieceArr[positions[2]];
                    pieceArr[positions[2]] = pieceArr[positions[3]];
                    pieceArr[positions[3]] = temp;
                    break;
                case 1: // double
                    temp = pieceArr[positions[0]];
                    pieceArr[positions[0]] = pieceArr[positions[2]];
                    pieceArr[positions[2]] = temp;
                    temp = pieceArr[positions[1]];
                    pieceArr[positions[1]] = pieceArr[positions[3]];
                    pieceArr[positions[3]] = temp;
                    break;
                case 0: // inverse
                    temp = pieceArr[positions[0]];
                    pieceArr[positions[0]] = pieceArr[positions[3]];
                    pieceArr[positions[3]] = pieceArr[positions[2]];
                    pieceArr[positions[2]] = pieceArr[positions[1]];
                    pieceArr[positions[1]] = temp;
                    break;
            }
            if (flip && (amount != 1)) {
                for (int i=0; i<4; i++) {
                    pieceArr[positions[i]] ^= 1;
                }
            }
        } else { // assert positions.length == 2;
            temp = pieceArr[positions[0]];
            pieceArr[positions[0]] = pieceArr[positions[1]];
            pieceArr[positions[1]] = temp;
        }
    }

    public void executeMove(int moveIndex, int amount) { 
        for (int[] positions : centerMoves[moveIndex]) cycle(centerPerm, positions, amount, false);
        for (int[] positions : wingMoves[moveIndex]) cycle(wingPerm, positions, amount, false);
        for (int[] positions : midgeMoves[moveIndex]) cycle(midgePerm, positions, amount, (moveIndex == 2 || moveIndex == 5) ? true : false);
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

    public EdgeCube[] getNeighbors() {
        EdgeCube[] nbrs = new EdgeCube[24];
        int i = 0;
        for (int nextMoveIndex=0; nextMoveIndex < EdgeCube.numberOfMoves; nextMoveIndex++) {
            for (int rot=((nextMoveIndex < 6) ? 0 : 1); rot < ((nextMoveIndex < 6) ? 3 : 2); rot++) {
                EdgeCube nbr = new EdgeCube(this);
                nbr.executeMove(nextMoveIndex, rot);
                nbrs[i] = nbr;
                i++;
            }
        }
        return nbrs;
    }

    public String executeInverse(int moveIndex) {
        int rot;
        int ind;
        if (moveIndex < 18) {
            rot = moveIndex%3;
            ind = moveIndex/3;
        } else {
            rot = 1;
            ind = moveIndex-12;
        }
        String solutionMove = EdgeCube.moveStr.get(ind) + EdgeCube.moveAmts.get(rot) + " ";
        this.executeStr(EdgeCube.moveStr.get(ind) + EdgeCube.moveAmts.get(2-rot));
        return solutionMove;
    }

    public int swapCount() {
        int swaps = 0;
        byte[] wingCycles = getWingCycles();
        int needle = 0;
        while (needle < numberOfWings) {
            if (wingCycles[needle] == needle) needle++;
            else {
                byte temp = wingCycles[needle];
                wingCycles[needle] = wingCycles[temp];
                wingCycles[temp] = temp;
                swaps++;
            }
        }
        return swaps;
    }

    public int h() {
        return swapCount(); // (int)(h_helper() * 2);
    }
}

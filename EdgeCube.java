import java.util.*;

public class EdgeCube extends Cube { 
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

    static final int RDB = 24;
    static final int RD = 25;
    static final int RDF = 26;
    static final int RF = 27;
    static final int RUF = 28;
    static final int RU = 29;
    static final int RUB = 30;
    static final int RB = 31;

    static final int BDL = 32;
    static final int BD = 33;
    static final int BDR = 34;
    static final int BR = 35;
    static final int BUR = 36;
    static final int BU = 37;
    static final int BUL = 38;
    static final int BL = 39;

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
    private static final int[] axis = {0, 0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2, 0, 1, 2, 0, 1, 2};
    private static final int[] moveType = {0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, -6, -5, -4, -3, -2, -1};
    public static final int numberOfMoves = centerMoves.length;
    public static final byte[] centerSolved = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3};
    public static final byte[] wingSolved = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    public static final byte[] midgeSolved = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22};
    public static final int numberOfCenters = centerSolved.length;
    public static final int numberOfWings = wingSolved.length;
    public static final int numberOfMidges = midgeSolved.length;
    public static final int[] zRot = {15, 14, 7, 6, 9, 8, 23, 22, 17, 16, 5, 4, 1, 0, 21, 20, 11, 10, 3, 2, 13, 12, 19, 18};
    public static final int[] xRot = {5, 4, 10, 11, 17, 16, 8, 9, 22, 23, 18, 19, 2, 3, 6, 7, 21, 20, 12, 13, 1, 0, 14, 15};
    public static final int[] nullRot = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    public static final HashMap<Integer, Integer> htrCenterPrun;
    public static final HashMap<Integer, HashSet<Integer>> dist0Prun;
    public static final HashMap<Integer, HashSet<Integer>> dist1Prun;
    public static final ArrayList<HashMap<Integer, HashSet<Integer>>> ctrDistPrun;

    static {
        CenterCube pzl = new CenterCube();
        HashMap<CenterCube, Integer> depths = new HashMap<CenterCube, Integer>();
        depths.put(pzl, 0);
        for (int depth=1; depth<=6; depth++) {
            HashSet<CenterCube> prevNodes = new HashSet<CenterCube>(depths.keySet());
            for (CenterCube node : prevNodes) {
                for (int u=0; u<4; u++) {
                    for (int l=0; l<4; l++) {
                        CenterCube nbr = new CenterCube(node);
                        nbr.swap(UBR, LUF);
                        nbr.swap(UR, LF);
                        nbr.swap(UFR, LDF);
                        if (u!=3) nbr.executeMove(0, u);
                        if (l!=3) nbr.executeMove(4, l);
                        boolean align = (u==3 && l==3) || (u==2-l);
                        int newDepth = align ? 2*depth-1 : 2*depth;
                        if (!depths.containsKey(nbr) || depths.get(nbr) > newDepth) depths.put(nbr, newDepth);
                    }
                }
            }
        }
        HashMap<Integer, Integer> centerPrun = new HashMap<Integer, Integer>();
        HashMap<Integer, HashSet<Integer>> ctrDist0Prun = new HashMap<Integer, HashSet<Integer>>();
        HashMap<Integer, HashSet<Integer>> ctrDist1Prun = new HashMap<Integer, HashSet<Integer>>();
        for (CenterCube node : depths.keySet()) {
            int ctrId = 0;
            for (int i=0; i<16; i++) {
                ctrId *= 2;
                ctrId += node.getPerm()[i];
            }
            centerPrun.put(ctrId, depths.get(node));
            if (depths.get(node) <= 4) {
                HashSet<Integer> possibleAUF0 = new HashSet<Integer>();
                HashSet<Integer> possibleAUF1 = new HashSet<Integer>();
                for (int u=0; u<4; u++) {
                    for (int l=0; l<4; l++) {
                        CenterCube nbr = new CenterCube(node);
                        if (u != 3) nbr.executeMove(0, u);
                        if (l != 3) nbr.executeMove(4, l);
                        nbr.swap(UBR, LUF);
                        nbr.swap(UR, LF);
                        nbr.swap(UFR, LDF);
                        if (depths.get(nbr) == 0) possibleAUF0.add(u + 4*l);
                        if (depths.get(nbr) == 1 || depths.get(nbr) == 2) possibleAUF1.add(u + 4*l);
                    }
                }
                ctrDist0Prun.put(ctrId, possibleAUF0);
                ctrDist1Prun.put(ctrId, possibleAUF1);
            }
        }
        htrCenterPrun = new HashMap<Integer, Integer>(centerPrun);
        dist0Prun = new HashMap<Integer, HashSet<Integer>>(ctrDist0Prun);
        dist1Prun = new HashMap<Integer, HashSet<Integer>>(ctrDist1Prun);
        ctrDistPrun = new ArrayList<HashMap<Integer, HashSet<Integer>>>(2);
        ctrDistPrun.add(dist0Prun);
        ctrDistPrun.add(dist1Prun);
    }

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
        for (int[] positions : midgeMoves[moveIndex]) cycle(midgePerm, positions, amount, (moveIndex%3 == 2) ? true : false);
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

    public boolean isValidPair(int i1, int i2) {
        if (axis[i1] != axis[i2]) return true;
        return (moveType[i1] < moveType[i2]);
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

    public int swapCount(byte[] wingCycles) {
        int swaps = 0;
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

    public int flipCount(byte[] wingCycles) {
        int flips = 0;
        for (int i=0; i<numberOfWings; i++) {
            if (wingCycles[i^1] == i) flips++;
        }
        return flips;
    }

    public int centerDistance(int index1, int index2) {
        int ctrId = centerID(index1, index2);
        return htrCenterPrun.get(ctrId);
    }

    public int centerID(int index1, int index2) {
        int ctrId = 0;
        for (int i=index1; i<index1+8; i++) {
            ctrId *= 2;
            ctrId += (centerPerm[i] == centerSolved[index1]) ? 0 : 1;
        }
        for (int i=index2; i<index2+8; i++) {
            ctrId *= 2;
            ctrId += (centerPerm[i] == centerSolved[index1]) ? 0 : 1;
        }
        return ctrId;
    }

    // start of new stuff -----------------------------------------------------------------------------------

    public ArrayList<ArrayList<Byte>> permCycles(byte[] wingCycles) {
        ArrayList<ArrayList<Byte>> cyc = new ArrayList<ArrayList<Byte>>(4);
        cyc.add(new ArrayList<Byte>(8));
        cyc.add(new ArrayList<Byte>(6));
        cyc.add(new ArrayList<Byte>(4));
        cyc.add(new ArrayList<Byte>(5));
        
        ArrayList<Byte> tempCycles = new ArrayList<Byte>(5);

        int needle = 0;
        while (needle < numberOfWings) {
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

    // precondition: wings are not solved
    public ArrayList<byte[]> getPossibleSwaps(byte[] wingCycles) {
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
            for (int i=0; i<4; i++) {
                acc[i] = cyc.get(swap[i][0]).get(swap[i][1]);
            }
            possibleSwaps.add(acc);
        }

        return possibleSwaps;
    }

    private static int combine(int a, int b) {
        int x = Math.max(a, b);
        return x*(x-3)/2 + a + b;
    }

    private static int fullIndex(int wingIdx, int centerIdx) {
        int symData = EquatorPrun.symReference[wingIdx];
        if (((symData >> 5) << 8) + EquatorPrun.centerTransformTable[centerIdx*32+(symData & 31)] == 299325) {

            System.out.println(wingIdx);
            System.out.println(EquatorPrun.symReference[0]);
            System.out.println(centerIdx);
        }
        return ((symData >> 5) << 8) + EquatorPrun.centerTransformTable[centerIdx*32+(symData & 31)];
    }

    public int getMinDepthCyc(byte[] cycle, int targetDistSum) {
        int[] prunToCtrIdx = new int[]{51, 50, 49, 48, 35, 34, 33, 32, 19, 18, 17, 16, 3, 2, 1, 0};
        int[] prunToCtrIdxShift = new int[]{32, 35, 34, 33, 16, 19, 18, 17, 0, 3, 2, 1, 48, 51, 50, 49};
        int[] prunToCtrIdxDoubleShift = new int[]{17, 16, 19, 18, 1, 0, 3, 2, 49, 48, 51, 50, 33, 32, 35, 34};

        int udCtrDist = (centerDistance(0, 40)+1)/2;
        int lrCtrDist = (centerDistance(8, 24)+1)/2;
        int fbCtrDist = (centerDistance(16, 32)+1)/2;

        int minDepth = EquatorPrun.NOTFOUND;
        // the nullRots are completely unnecessary and are there to show what the code would be like for fb and lr
        int udEdgeIndex = combine(combine(EdgeCube.nullRot[cycle[0]], EdgeCube.nullRot[cycle[1]]), combine(EdgeCube.nullRot[cycle[2]], EdgeCube.nullRot[cycle[3]]));
        int lrEdgeIndex = combine(combine(EdgeCube.zRot[cycle[0]], EdgeCube.zRot[cycle[1]]), combine(EdgeCube.zRot[cycle[2]], EdgeCube.zRot[cycle[3]]));
        int fbEdgeIndex = combine(combine(EdgeCube.xRot[cycle[0]], EdgeCube.xRot[cycle[1]]), combine(EdgeCube.xRot[cycle[2]], EdgeCube.xRot[cycle[3]]));
        
        for (int lrMoves=0; lrMoves <= 1; lrMoves++) {
            int fbMoves = targetDistSum - lrMoves - udCtrDist;
            if (fbMoves < 0 || fbMoves > 1 || udCtrDist > 1) continue;
            HashSet<Integer> lrAdjust = EdgeCube.ctrDistPrun.get(lrMoves).get(centerID(8, 24));
            HashSet<Integer> fbAdjust = EdgeCube.ctrDistPrun.get(fbMoves).get(centerID(16, 32));
            if (lrAdjust.size() * fbAdjust.size() >= 256) {
                minDepth = Math.min(minDepth, EquatorPrun.depthTable[udEdgeIndex]);
                continue;
            }
            for (int lr1 : lrAdjust) {
                for (int fb1 : fbAdjust) {
                    int centerIdx = prunToCtrIdxShift[lr1] + 4*prunToCtrIdxShift[fb1];
                    minDepth = Math.min(minDepth, EquatorPrun.fullDepthTable[fullIndex(udEdgeIndex, centerIdx)]);
                }
            }
        }

        for (int udMoves=0; udMoves <= 1; udMoves++) {
            int fbMoves = targetDistSum - udMoves - lrCtrDist;
            if (fbMoves < 0 || fbMoves > 1 || lrCtrDist > 1) continue;
            HashSet<Integer> udAdjust = EdgeCube.ctrDistPrun.get(udMoves).get(centerID(0, 40));
            HashSet<Integer> fbAdjust = EdgeCube.ctrDistPrun.get(fbMoves).get(centerID(16, 32));
            if (udAdjust.size() * fbAdjust.size() >= 256) {
                minDepth = Math.min(minDepth, EquatorPrun.depthTable[lrEdgeIndex]);
                continue;
            }
            for (int ud1 : udAdjust) {
                for (int fb1 : fbAdjust) {
                    int centerIdx = prunToCtrIdx[ud1] + 4*prunToCtrIdx[fb1];
                    minDepth = Math.min(minDepth, EquatorPrun.fullDepthTable[fullIndex(lrEdgeIndex, centerIdx)]);
                }
            }
        }

        for (int udMoves=0; udMoves <= 1; udMoves++) {
            int lrMoves = targetDistSum - udMoves - fbCtrDist;
            if (lrMoves < 0 || lrMoves > 1 || fbCtrDist > 1) continue;
            HashSet<Integer> udAdjust = EdgeCube.ctrDistPrun.get(udMoves).get(centerID(0, 40));
            HashSet<Integer> lrAdjust = EdgeCube.ctrDistPrun.get(lrMoves).get(centerID(8, 24));
            if (udAdjust.size() * lrAdjust.size() >= 256) {
                minDepth = Math.min(minDepth, EquatorPrun.depthTable[fbEdgeIndex]);
                continue;
            }
            for (int ud1 : udAdjust) {
                for (int lr1 : lrAdjust) {
                    int centerIdx = prunToCtrIdxDoubleShift[lr1] + 4*prunToCtrIdxShift[ud1];
                    minDepth = Math.min(minDepth, EquatorPrun.fullDepthTable[fullIndex(fbEdgeIndex, centerIdx)]);
                }
            }
        }

        return minDepth;
    }

    public int getMinDepth(int targetDistSum) {
        int minDepth = EquatorPrun.NOTFOUND;
        for (byte[] swaps : getPossibleSwaps(getWingCycles()))
            minDepth = Math.min(minDepth, getMinDepthCyc(swaps, targetDistSum));
        return minDepth;
    }


    // end of new stuff ------------------------------------------------------------------------------------

    public double scaleHeuristic(double x) {
        if (x <= 6) return 3*x + 1.5;
        if (x <= 8) return scaleHeuristic(6) + 2.5*(x-6);
        if (x <= 10) return scaleHeuristic(8) + 2*(x-8);
        if (x <= 12) return scaleHeuristic(10) + 1.5*(x-10);
        return scaleHeuristic(12) + 1*(x-12);
    }

    public double h() {
        if (isSolved()) return 0;
        byte[] wingCycles = getWingCycles();
        int wingSwaps = swapCount(wingCycles);
        int wingFlips = flipCount(wingCycles);
        int ctr_1 = centerDistance(0, 40);
        int ctr_2 = centerDistance(8, 24);
        int ctr_3 = centerDistance(16, 32);

        if (wingSwaps >= 5 || ctr_1 >= 5 || ctr_2 >= 5 || ctr_3 >= 5 || (ctr_1+1)/2+(ctr_2+1)/2+(ctr_3+1)/2 >= 5) {
            int wing_h = wingFlips + wingSwaps;
            int ctr_h = ctr_1 + ctr_2 + ctr_3;
            double h_max = Math.max(ctr_h * 0.5, wing_h);
            return (scaleHeuristic(h_max));
        }

        if (wingSwaps == 0) return 6 + ctr_1 + ctr_2 + ctr_3;

        int targetDistSum = ((ctr_1+1)/2 + (ctr_2+1)/2 + (ctr_3+1)/2 == 2 && Math.max(Math.max((ctr_1+1)/2, (ctr_2+1)/2), (ctr_3+1)/2) == 1) && wingSwaps == 2 ? 0 : 1;
        return Math.min(11, getMinDepth(targetDistSum * 2)) + 8 * targetDistSum;
    }
}

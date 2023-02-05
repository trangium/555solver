import java.util.*;
import java.io.*;

public class EquatorPrun {
    public static final byte NOTFOUND = 127;
    public static int[] symReference = new int[37590]; // last 5 bits are the symmetry transformation. all other bits are the depthTable index.
    public static int[] centerTransformTable = new int[8192];
    public static byte[] depthTable = new byte[37590];
    public static byte[] fullDepthTable = new byte[220160];

    static {readLookup();}

    private static int combine(int a, int b) {
        int x = Math.max(a, b);
        return x*(x-3)/2 + a + b;
    }

    public static int wingIndex(EdgeCube e) {
        int a = -1;
        int b = -1;
        int c = -1;
        int d = -1;
        for (int i=0; i<EdgeCube.numberOfWings; i++) {
            if (e.getWingPerm()[i] == EdgeCube.FRu || e.getWingPerm()[i] == EdgeCube.FLu || 
                e.getWingPerm()[i] == EdgeCube.BLu || e.getWingPerm()[i] == EdgeCube.BRu) {
                if (a == -1) a = i;
                else if (Math.abs(e.getWingPerm()[i]-e.getWingPerm()[a]) == 4) b = i;
                else if (c == -1) c = i;
                else d = i;
            }
        }
        return combine(combine(a, b), combine(c, d));
    }
    // precondition: Edgecube e is [(outer turns) Uw2] away from solved.
    public static int centerIndex(EdgeCube e) {
        int factor = 1;
        int acc = 0;
        for (int i=9; i<40; i+=2) {
            if (e.getCenterPerm()[i] != EdgeCube.centerSolved[i]) {
                acc += (((i>=24 ? i+4 : i)%8)>>1)*factor;
                factor *= 4;
            }
        }
        return acc;
    }

    public static int expandedIndex(EdgeCube e) {
        return (wingIndex(e) << 8) + centerIndex(e);
    }

    public static int getFullIndex(EdgeCube e) {
        int symData = symReference[wingIndex(e)];
        return ((symData >> 5) << 8) + centerTransformTable[centerIndex(e)*32+(symData & 31)];
    }

    public static int sym(EdgeCube e, int symID) {
        // valid yAmounts: {0, 1, 2, 3}
        // valid x2Amounts, eAmounts, fAmounts: {0, 1}
        int yAmount = (symID & 24) >> 3;
        int x2Amount = (symID & 4) >> 2;
        int eAmount = (symID & 2) >> 1;
        int fAmount = (symID & 1);

        int ctrIndex = centerIndex(e);
        EdgeCube res = new EdgeCube(e);

        ctrIndex = (ctrIndex >> 2*yAmount) + ((ctrIndex & ((1 << 2*yAmount) - 1)) << (8 - 2*yAmount));
        for (int i=0; i<yAmount; i++) res.executeStr("Uw Dw'");

        if (x2Amount == 1) {
            res.executeStr("Lw2 Rw2");
            int lx = (ctrIndex & 3) ^ 2;
            int fx = (ctrIndex & 12) ^ 8;
            int rx = (ctrIndex & 48) ^ 32;
            int bx = (ctrIndex & 192) ^ 128;
            ctrIndex = lx + (fx << 4) + rx + (bx >> 4);
        }

        if (eAmount == 1) {
            res.executeStr("R L F R2 U D' F2 R' F B L2 U' L2 D2 R2 B2 L2 B2 U2 F2 U");
            int lx = (ctrIndex & 3) ^ ((~ctrIndex & 1) << 1);
            int fx = (ctrIndex & 12) ^ ((~ctrIndex & 4) << 1);
            int rx = (ctrIndex & 48) ^ ((~ctrIndex & 16) << 1);
            int bx = (ctrIndex & 192) ^ ((~ctrIndex & 64) << 1);
            ctrIndex = lx + fx + rx + bx;
        }

        if (fAmount == 1) {
            res.executeStr("R L U2 F U' D F2 R2 B2 L U2 F' B' U R2 D F2 U R2 U");
            int lx = (ctrIndex & 3) ^ 2;
            int fx = (ctrIndex & 12) ^ 8;
            int rx = (ctrIndex & 48) ^ 32;
            int bx = (ctrIndex & 192) ^ 128;
            ctrIndex = lx + fx + rx + bx;
        }

        return (wingIndex(res) << 8) + ctrIndex;
    }
    
    public static void fullbfs() {
        EdgeCube eInitial = new EdgeCube();
        eInitial.executeStr("Uw2");
        EdgeCube eFlipInitial = new EdgeCube(eInitial);
        eFlipInitial.executeStr("F2 L2 R2 B2");
        
        HashSet<EdgeCube> seenCubes = new HashSet<EdgeCube>();
        EdgeCube[] cubeTable = new EdgeCube[37590];
        for (int i=0; i<depthTable.length; i++) depthTable[i] = NOTFOUND;
        seenCubes.add(eInitial);
        seenCubes.add(eFlipInitial);
        depthTable[wingIndex(eInitial)] = 0;
        depthTable[wingIndex(eFlipInitial)] = 0;
        cubeTable[wingIndex(eInitial)] = eInitial;
        cubeTable[wingIndex(eFlipInitial)] = eFlipInitial;
        HashSet<EdgeCube> newCubes;

        int nodesReached = 2;
        for (byte i=1; i<=6; i++) {
            newCubes = new HashSet<EdgeCube>();
            for (EdgeCube e : seenCubes) {
                for (int nextI=0; nextI<6; nextI++) {
                    for (int rot=0; rot<3; rot++) {
                        EdgeCube nbr = new EdgeCube(e);
                        nbr.executeMove(nextI, rot);
                        if (depthTable[wingIndex(nbr)] == NOTFOUND) {
                            newCubes.add(nbr);
                            depthTable[wingIndex(nbr)] = i;
                            cubeTable[wingIndex(nbr)] = nbr;
                            nodesReached++;

                            // testing
                            int ySym = sym(nbr, 31) >> 8;
                            if (depthTable[ySym] != NOTFOUND) {
                                if (depthTable[wingIndex(nbr)] != depthTable[ySym]) System.out.println("Error: Symmetries not symmetrical");
                            }
                            // end testing
                        }
                    }
                }
            }
            seenCubes.clear();
            for (EdgeCube e : newCubes) {
                seenCubes.add(e);
            }
            System.out.println("Depth "+i+": "+nodesReached);
        }

        int[] symDepthTable = new int[860];
        HashSet<Integer> seenLows = new HashSet<Integer>();
        int count = 0;

        for (int i=0; i<37590; i++) {
            EdgeCube e = cubeTable[i];
            if (e == null) continue;
            int lowestSoFar = 37591;
            int lowSymId = -1;
            for (int symId=0; symId<32; symId++) {
                int newId = sym(e, symId) >> 8;
                if (newId < lowestSoFar) {
                    lowestSoFar = newId;
                    lowSymId = symId;
                }
            }
            // not seen before
            if (!seenLows.contains(lowestSoFar)) {
                symDepthTable[count] = depthTable[i];
                symReference[i] = (count << 5) + lowSymId;
                seenLows.add(lowestSoFar);
                count++;
            } else {
                symReference[i] = (symReference[lowestSoFar] & 65504) + lowSymId;
            }
        }



        // center transform table
        EdgeCube eTransform = new EdgeCube();
        eTransform.executeStr("Uw2");
        
        for (int a=0; a<4; a++) {
            for (int b=0; b<4; b++) {
                for (int c=0; c<4; c++) {
                    for (int d=0; d<4; d++) {
                        int ctrIndex = centerIndex(eTransform);
                        for (int symID=0; symID<32; symID++) {
                            centerTransformTable[ctrIndex*32+symID] = sym(eTransform, symID) & 255;
                        }
                        eTransform.executeStr("B");
                    }
                    eTransform.executeStr("R");
                }
                eTransform.executeStr("F");
            }
            eTransform.executeStr("L");
        }




        eInitial = new EdgeCube();
        eInitial.executeStr("Uw2");
        eFlipInitial = new EdgeCube(eInitial);
        eFlipInitial.executeStr("F B' L2 R2 F B'");
        
        for (int i=0; i<fullDepthTable.length; i++) fullDepthTable[i] = NOTFOUND;
        seenCubes = new HashSet<EdgeCube>();
        HashMap<Integer, Integer> seenIndexes = new HashMap<Integer, Integer>();
        seenCubes.add(eInitial);
        seenCubes.add(eFlipInitial);
        seenIndexes.put(expandedIndex(eInitial), 0);
        seenIndexes.put(expandedIndex(eFlipInitial), 0);
        

        System.out.println("\n\n\nSolved index: "+expandedIndex(eInitial));
        EdgeCube eTest = new EdgeCube(eInitial);
        eTest.executeStr("F2 L2 R2 B2");
        System.out.println("Test index: "+expandedIndex(eTest));

        fullDepthTable[getFullIndex(eInitial)] = 0;
        fullDepthTable[getFullIndex(eFlipInitial)] = 0;

        for (int i=1; i<=9; i++) {
            newCubes = new HashSet<EdgeCube>();
            for (EdgeCube e : seenCubes) {
                for (int nextI=0; nextI<6; nextI++) {
                    for (int rot=0; rot<3; rot++) {
                        EdgeCube nbr = new EdgeCube(e);
                        nbr.executeMove(nextI, rot);
                        if (!seenIndexes.keySet().contains(expandedIndex(nbr))) {
                            newCubes.add(nbr);
                            seenIndexes.put(expandedIndex(nbr), i);

                            int fullIndex = getFullIndex(nbr);
                            if (fullDepthTable[fullIndex] == NOTFOUND) fullDepthTable[fullIndex] = (byte)i;
                            // testing
                            // int ySym = sym(nbr, 31);
                            // if (seenIndexes.containsKey(ySym)) {
                            //     if (seenIndexes.get(expandedIndex(nbr)) != seenIndexes.get(ySym)) System.out.println("Error: Symmetries not symmetrical");
                            // }
                            // end testing
                        }
                    }
                }
            }
            seenCubes.clear();
            for (EdgeCube e : newCubes) {
                seenCubes.add(e);
            }
            System.out.println("Depth "+i+": "+seenIndexes.size());
        }


        /*
         * int fullIndex(wing permutation [A, B, C, D], centerOrientation)
        edgeIndex = getEdgeIndex(A, B, C, D)
        edgeBaseID, symID = symmetryTable[edgeIndex]
        centerID = centerTransformTable[centerOrientation, symID]
        return edgeBaseID * 256 + centerID
         */

        String[] sequences = new String[] {"R U R'", "U2 U2", "F2 L2 R2 B2", "F R U R' U'", "R U R' L' U' L R D R' L' D' L", "R U R' F R' F' R F B L2 F' B'", "R U2 R D R D R F B L D' R2 D R", "B2 L B2 F"};

        for (String seq : sequences) {
            eTest = new EdgeCube(eInitial);
            eTest.executeStr(seq);
            System.out.println("\n"+seq);

            int fullIndex = getFullIndex(eTest);
            System.out.println("fullIndex: " + fullIndex);
            System.out.println("depth: " + fullDepthTable[fullIndex]);
        }
    }

    public static void writeToFile() {
        try {
            FileOutputStream f = new FileOutputStream("equator.prun");
            BufferedOutputStream writer = new BufferedOutputStream(f);
            
            for (int i=0; i<37590; i++) {
                writer.write(symReference[i] >> 8);
                writer.write(symReference[i] & 255);
            }
            for (int i=0; i<8192; i++) writer.write(centerTransformTable[i]);
            for (int i=0; i<37590; i++) writer.write(depthTable[i]);
            for (int i=0; i<220160; i++) writer.write(fullDepthTable[i]);
            writer.close();
            f.close();
        } catch (Exception FileNotFoundException) {
            System.out.println("File equator.prun does not exist");
        }
    }

    public static void readLookup() {
        try {
            FileInputStream f = new FileInputStream("equator.prun");
            BufferedInputStream reader = new BufferedInputStream(f);

            for (int i=0; i<37590; i++) symReference[i] = (reader.read() << 8) + reader.read();
            for (int i=0; i<8192; i++) centerTransformTable[i] = reader.read();
            for (int i=0; i<37590; i++) depthTable[i] = (byte)(reader.read());
            for (int i=0; i<220160; i++) fullDepthTable[i] = (byte)(reader.read());

            reader.close();

        } catch (IOException e) {
            System.out.println("File equator.prun does not exist");
        }
    }

    public static void testTables() {
        String[] sequences = new String[] {"R U R'", "U2 U2", "F2 L2 R2 B2", "F R U R' U' F'", "R U R' L' U' L R D R' L' D' L", "R U R' F R' F' R F B L2 F' B'", "R U2 R D R D R F B L D' R2 D R", "L R' F2 L' R", "L R' F L'", "B2 L B2 F"};

        for (String seq : sequences) {
            EdgeCube eTest = new EdgeCube();
            eTest.executeStr("Uw2");
            eTest.executeStr(seq);
            System.out.println("\n"+seq);

            int fullIndex = getFullIndex(eTest);
            int edgeIndex = wingIndex(eTest);
            System.out.println("fullIndex: " + fullIndex);
            System.out.println("depth: " + fullDepthTable[fullIndex]);
            System.out.println("edgeIndex: " + edgeIndex);
            System.out.println("edgeDepth: " + depthTable[edgeIndex]);
        }
    }

    public static void main(String[] args) {
        
        // fullbfs();
        // writeToFile();

        // double t = System.currentTimeMillis();
        // readLookup();
        // System.out.println("\n"+(System.currentTimeMillis()-t)+" ms\n");
        testTables();
        


    }
}

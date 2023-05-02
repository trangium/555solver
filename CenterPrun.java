import java.util.*;
import java.io.*;

public class CenterPrun {
    public static byte[] plusDepthTable = new byte[735471];
    public static byte[] xDepthTable = new byte[735471];
    private static final int[][] choose = new int[25][9];
    static {
        for (int n=1; n<25; n++) {
            for (int k=0; k<9; k++) {
                if (k==0 || k==n) choose[n][k] = 1;
                else if (k>n) choose[n][k] = 0;
                else choose[n][k] = choose[n-1][k-1] + choose[n-1][k];
            }
        }
        readLookup();
    }

    public static int plusCtrIndex(byte[] perm) {
        return ctrIndex(perm, 0, 8);
    }

    public static int xCtrIndex(byte[] perm) {
        return ctrIndex(perm, 1, 8);
    }

    private static int ctrIndex(byte[] perm, int index, int remaining) {
        final int AXIS = 0; // 0, 1, or 2
        int n = (49-index)/2;
        int k = remaining;
        if (k == 0 || n == 0) return 0;
        byte head = perm[index];
        if (head == AXIS) return ctrIndex(perm, index+2, remaining-1) + choose[n-1][k];
        return ctrIndex(perm, index+2, remaining);
    }

    public static void fullbfs() {
        final byte maxDepth = 8;
        HashSet<CenterCube> seenCubesPlus = new HashSet<CenterCube>();
        HashSet<CenterCube> seenCubesX = new HashSet<CenterCube>();
        CenterCube initialCube = new CenterCube();
        seenCubesPlus.add(initialCube);
        seenCubesX.add(initialCube);
        for (int i=0; i<735471; i++) {
            plusDepthTable[i] = (i == plusCtrIndex(initialCube.getPerm())) ? (byte)0 : maxDepth+1;
            xDepthTable[i] = (i == xCtrIndex(initialCube.getPerm())) ? (byte)0 : maxDepth+1;
        }
        for (byte i=1; i<=maxDepth; i++) {
            HashSet<CenterCube> newCubesPlus = new HashSet<CenterCube>();
            HashSet<CenterCube> newCubesX = new HashSet<CenterCube>();
            for (CenterCube c : seenCubesPlus) {
                for (CenterCube nbr : c.getNeighbors()) {
                    int plusIndex = plusCtrIndex(nbr.getPerm());
                    if (plusDepthTable[plusIndex] > i) {
                        plusDepthTable[plusIndex] = i;
                        newCubesPlus.add(nbr);
                    }
                }
            }
            for (CenterCube c : seenCubesX) {
                for (CenterCube nbr : c.getNeighbors()) {
                    int xIndex = xCtrIndex(nbr.getPerm());
                    if (xDepthTable[xIndex] > i) {
                        xDepthTable[xIndex] = i;
                        newCubesX.add(nbr);
                    }
                }
            }
            seenCubesPlus = new HashSet<CenterCube>(newCubesPlus);
            seenCubesX = new HashSet<CenterCube>(newCubesX);
        }
    }

    public static void readLookup() {
        try {
            FileInputStream f = new FileInputStream("center.prun");
            BufferedInputStream reader = new BufferedInputStream(f);

            for (int i=0; i<735471; i++) plusDepthTable[i] = (byte)(reader.read());
            for (int i=0; i<735471; i++) xDepthTable[i] = (byte)(reader.read());

            reader.close();

        } catch (IOException e) {
            System.out.println("File center.prun does not exist");
        }
    }

    public static void writeToFile() {
        try {
            FileOutputStream f = new FileOutputStream("center.prun");
            BufferedOutputStream writer = new BufferedOutputStream(f);
            
            for (int i=0; i<735471; i++) writer.write(plusDepthTable[i]);
            for (int i=0; i<735471; i++) writer.write(xDepthTable[i]);
            writer.close();
            f.close();
        } catch (Exception FileNotFoundException) {
            System.out.println("File center.prun does not exist");
        }
    }

    public static void main(String[] args) {
        
        fullbfs();
        writeToFile();
        readLookup();
        
    }

}

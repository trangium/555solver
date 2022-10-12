public class FiveByFiveSolver {
    static final int PCCOUNT = 92;
    static final int POSMASK = 127;
    static final int ORIMASK = 384;
    static final int POSBITS = 7;
    static int[] CUBEORI = new int[PCCOUNT];

    private static void mult(int[] start, int[] move, int[] result) {
        for (int p=0; p<PCCOUNT; p++) {
            int temp = (start[move[p] & POSMASK] + (move[p] & ORIMASK));
            int tempOri = CUBEORI[temp & POSMASK];
            result[p] = temp % (tempOri << POSBITS);
        }
    }

    private static void cycle(int[] pzl, int[] positions) {
        int temp = pzl[positions[0]];
        for (int i=1; i<positions.length; i++) {
            pzl[positions[i-1]] = pzl[positions[i]];
        }
        pzl[positions[positions.length-1]] = temp;
    }

    private static void printPzl(int[] pzl) {
        String acc = "[";
        for (int i : pzl) {
            acc = acc + i + " ";
        }
        System.out.println(acc + "]");
    }

    /*
    mult (start, move, result) {
        for (let p = 0; p < this.pcCount; p++) {
            let temp = (start[move[p] & this.posMask] + (move[p] & this.oriMask));
            let tempOri = this.cubeOri[temp & this.posMask];
            result[p] = temp % (tempOri << this.posBits);
        }
    }
    */

    public static void main(String[] args) {
        for (int i=0; i<PCCOUNT; i++) {
            if (i < 8) CUBEORI[i] = 3;
            else if (i < 44) CUBEORI[i] = 2;
            else CUBEORI[i] = 1;
        }

        int[] SOLVED = new int[PCCOUNT];
        for (int i=0; i<PCCOUNT; i++) {
            if (i < 44) SOLVED[i] = i;
            else SOLVED[i] = (int)(i/8)+40;
        }

        int[] NULLMOVE = new int[PCCOUNT];
        for (int i=0; i<PCCOUNT; i++) {
            NULLMOVE[i] = i;
        }

        int[] UMOVE = new int[PCCOUNT];
        for (int i=0; i<PCCOUNT; i++) {
            UMOVE[i] = i;
        }
        cycle(UMOVE, new int[]{0, 1, 2, 3});
        cycle(UMOVE, new int[]{8, 11, 14, 17});
        cycle(UMOVE, new int[]{9, 12, 15, 18});
        cycle(UMOVE, new int[]{10, 13, 16, 19});
        cycle(UMOVE, new int[]{45, 47, 49, 51});
        cycle(UMOVE, new int[]{46, 48, 50, 52});

        int[] U2MOVE = new int[PCCOUNT];
        int[] U3MOVE = new int[PCCOUNT];
        mult(UMOVE, UMOVE, U2MOVE);
        mult(UMOVE, U2MOVE, U3MOVE);
        printPzl(NULLMOVE);
        printPzl(UMOVE);
        printPzl(U3MOVE);
    }
}
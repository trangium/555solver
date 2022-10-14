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

    private static int[] execute(int[] start, int[][] sequence) {
        int[] initCube = new int[PCCOUNT];
        for (int i=0; i<PCCOUNT; i++) initCube[i] = start[i];
        
        int[] tempCube = new int[PCCOUNT];
        for (int j=0; j<sequence.length; j++) {
            mult(initCube, sequence[j], tempCube);
            for (int i=0; i<PCCOUNT; i++) initCube[i] = tempCube[i];
        }
        System.out.println("$$\n$$");
        return initCube;
    }

    /*
    private static void cycle(int[] pzl, int[] positions) {
        int temp = pzl[positions[0]];
        for (int i=1; i<positions.length; i++) {
            pzl[positions[i-1]] = pzl[positions[i]];
        }
        pzl[positions[positions.length-1]] = temp;
    }
    */

    private static void printPzl(int[] pzl) {
        String acc = "[";
        for (int i : pzl) {
            acc = acc + i + " ";
        }
        System.out.println(acc + "]\n");
    }

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

        /*
        Order: 
            (UBL UBR UFR UFL DFL DFR DBR DBL) // corners
            (UB UR UF UL BL BR FR FL DF DR DB DL) // midges
            (ubL ubR urB urF ufR ufL ulF ulB blU brU frU flU blD brD frD flD dfL dfR drF drB dbR dbL dlB dlF) // wings
            (Ua Uc Ue Ug Ra Rc Re Rg Fa Fc Fe Fg Da Dc De Dg La Lc Le Lg Ba Bc Be Bg) // X centers
            (Ub Ud Uf Uh Rb Rd Rf Rh Fb Fd Ff Fh Db Dd Df Dh Lb Ld Lf Lh Bb Bd Bf Bh) // + centers
        Primitive moves:
            U: (UBL UBR UFR UFL) (UB UR UF UL) (ubL urB ufR ulF) (ubR urF ufL ulB) (Ua Uc Ue Ug) (Ub Ud Uf Uh)
            2U: (frU flU blU brU) (Fa La Ba Ra) (Fb Lb Bb Rb) (Fc Lc Bc Rc)
            R: (UFR-1 UBR+1 DBR-1 DFR+1) (UR BR DR FR) (urF brU drB frD) (urB brD drF frU) (Ra Rc Re Rg) (Rb Rd Rf Rh)
            L: (UFL+1 DFL-1 DBL+1 UBL-1) (UL FL DL BL) (ulB flU dlF blD) (ulF flD dlB blU) (La Lc Le Lg) (Lb Ld Lf Lh)
            M: (UF+1 DF+1 DB+1 UB+1) (Ub Fb Db Bf) (Uf Ff Df Bb)
            2R: (ufR ubR dbR dfR) (Fc Uc Bg Dc) (Fd Ud Bh Dd) (Fe Ue Ba De)
            2L: (ufL dfL dbL ubL) (Fa Da Be Ua) (Fg Dg Bc Ug) (Fh Dh Bd Uh)
        */

        int[] MOVE_U = new int[]{3, 0, 1, 2, 4, 5, 6, 7, 11, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18, 19, 26, 27, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 47, 44, 45, 46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 71, 68, 69, 70, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91};
        int[] MOVE_R = new int[]{0, 258, 133, 3, 4, 262, 129, 7, 8, 14, 10, 11, 12, 9, 17, 15, 16, 13, 18, 19, 20, 21, 30, 34, 24, 25, 26, 27, 28, 23, 38, 31, 32, 22, 39, 35, 36, 37, 33, 29, 40, 41, 42, 43, 44, 45, 46, 47, 51, 48, 49, 50, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 75, 72, 73, 74, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91};
        int[] MOVE_2R = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 22, 23, 37, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 40, 38, 39, 21, 41, 42, 43, 44, 53, 54, 47, 48, 49, 50, 51, 52, 57, 58, 55, 56, 67, 64, 59, 60, 61, 62, 63, 46, 65, 66, 45, 68, 77, 70, 71, 72, 73, 74, 75, 76, 81, 78, 79, 80, 91, 82, 83, 84, 85, 86, 87, 88, 89, 90, 69};
        int[] MOVE_M = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 146, 9, 136, 11, 12, 13, 14, 15, 138, 17, 144, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 90, 69, 88, 71, 72, 73, 74, 75, 68, 77, 70, 79, 76, 81, 78, 83, 84, 85, 86, 87, 82, 89, 80, 91};
        int[] MOVE_2L = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 41, 21, 22, 23, 24, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 25, 37, 38, 39, 40, 36, 42, 43, 66, 45, 46, 65, 48, 49, 50, 51, 44, 53, 54, 47, 52, 57, 58, 55, 60, 61, 62, 63, 64, 59, 56, 67, 68, 69, 70, 89, 72, 73, 74, 75, 76, 77, 78, 71, 80, 81, 82, 79, 84, 85, 86, 87, 88, 83, 90, 91};
        int[] MOVE_L = new int[]{135, 1, 2, 256, 131, 5, 6, 260, 8, 9, 10, 12, 19, 13, 14, 11, 16, 17, 18, 15, 20, 21, 22, 23, 24, 25, 28, 32, 42, 29, 30, 27, 43, 33, 34, 26, 36, 37, 38, 39, 40, 41, 35, 31, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 63, 60, 61, 62, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 87, 84, 85, 86, 88, 89, 90, 91};

        // Demonstrates one way of composing moves
        int[][] temp = {MOVE_2R, MOVE_R, MOVE_M, MOVE_M, MOVE_M, MOVE_2L, MOVE_2L, MOVE_2L, MOVE_L, MOVE_L, MOVE_L};
        int[] MOVE_X = execute(NULLMOVE, temp);
        int[][] temp3 = {MOVE_X, MOVE_U, MOVE_X, MOVE_X, MOVE_X};
        int[] MOVE_F = execute(NULLMOVE, temp3);
        int[][] temp4 = {MOVE_X, MOVE_X, MOVE_X, MOVE_U, MOVE_X};
        int[] MOVE_B = execute(NULLMOVE, temp4);
        int[][] temp5 = {MOVE_X, MOVE_X, MOVE_U, MOVE_X, MOVE_X};
        int[] MOVE_D = execute(NULLMOVE, temp5);

        // Demonstrates another way of composing moves
        int[] MOVE_U2 = new int[PCCOUNT];
        mult(MOVE_U, MOVE_U, MOVE_U2);

        int[] MOVE_U3 = new int[PCCOUNT];
        mult(MOVE_U, MOVE_U2, MOVE_U3);

        printPzl(NULLMOVE);
        printPzl(MOVE_F);
        printPzl(MOVE_B);
        printPzl(MOVE_D);
        printPzl(MOVE_U);
        printPzl(MOVE_U3);
    }
}
import java.util.Arrays;

public class CenterCube {
    private byte[] perm;
    private static final int[][][] moves = {
        {{0, 2, 4, 6}, {1, 3, 5, 7}}, // U
        {{24, 26, 28, 30}, {25, 27, 29, 31}}, // R
        {{0, 2, 4, 6}, {1, 3, 5, 7}, {8, 16, 24, 32}, {9, 17, 25, 33}, {10, 18, 26, 34}}, // Uw
        {{24, 26, 28, 30}, {25, 27, 29, 31}, {32, 44, 20, 4}, {33, 45, 21, 5}, {34, 36, 22, 6}}, // Rw
    };
    public static final int numberOfMoves = moves.length;

    public CenterCube() {
        perm = new byte[48];
        for (int i=0; i<48; i++) {
            perm[i] = (byte)(i/8);
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

    public void printPzl() {
        String acc = "{";
        for (int i : perm) {
            acc = acc + i + ", ";
        }
        System.out.println(acc.substring(0, acc.length()-2) + "}");
    }

    public byte[] getPerm() {
        return perm;
    }

    private void cycle(int[] positions, int amount) {
        assert positions.length == 4;
        byte temp;
        switch (amount) {
            case 1: // clockwise
                temp = perm[positions[0]];
                perm[positions[0]] = perm[positions[1]];
                perm[positions[1]] = perm[positions[2]];
                perm[positions[2]] = perm[positions[3]];
                perm[positions[3]] = temp;
                return;
            case 2: // double
                temp = perm[positions[0]];
                perm[positions[0]] = perm[positions[2]];
                perm[positions[2]] = temp;
                temp = perm[positions[1]];
                perm[positions[1]] = perm[positions[3]];
                perm[positions[3]] = temp;
                return;
            case 3: // inverse
                temp = perm[positions[0]];
                perm[positions[0]] = perm[positions[3]];
                perm[positions[3]] = perm[positions[2]];
                perm[positions[2]] = perm[positions[1]];
                perm[positions[1]] = temp;
                return;
        }
    }

    public void executeMove(int moveIndex, int amount) {
        for (int[] positions : moves[moveIndex]) {
            cycle(positions, amount);
        }
    }
}

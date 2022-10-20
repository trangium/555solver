import java.util.*;

public class CenterCube {
    private byte[] perm;
    private static final int[][][] moves = {
        {{0, 2, 4, 6}, {1, 3, 5, 7}}, // U
        {{24, 26, 28, 30}, {25, 27, 29, 31}}, // R
        {{16, 18, 20, 22}, {17, 19, 21, 23}}, // F
        {{40, 42, 44, 46}, {41, 43, 45, 47}}, // D
        {{8, 10, 12, 14}, {9, 11, 13, 15}}, // L
        {{32, 34, 36, 38}, {33, 35, 37, 39}}, // B
        {{0, 2, 4, 6}, {1, 3, 5, 7}, {8, 16, 24, 32}, {9, 17, 25, 33}, {10, 18, 26, 34}}, // Uw
        {{24, 26, 28, 30}, {25, 27, 29, 31}, {32, 44, 20, 4}, {39, 43, 19, 3}, {38, 42, 18, 2}}, // Rw
        {{16, 18, 20, 22}, {17, 19, 21, 23}, {4, 30, 40, 10}, {5, 31, 41, 11}, {6, 24, 42, 12}}, // Fw
        {{40, 42, 44, 46}, {41, 43, 45, 47}, {36, 28, 20, 12}, {37, 29, 21, 13}, {38, 30, 22, 14}}, // Dw
        {{8, 10, 12, 14}, {9, 11, 13, 15}, {0, 16, 40, 36}, {7, 23, 47, 35}, {6, 22, 46, 34}}, // Lw
        {{32, 34, 36, 38}, {33, 35, 37, 39}, {0, 14, 44, 26}, {1, 15, 45, 27}, {2, 8, 46, 28}}, // Bw
    };

    private static final List<String> moveStr = Arrays.asList("U", "R", "F", "D", "L", "B", "Uw", "Rw", "Fw", "Dw", "Lw", "Bw");
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

    public void print() {
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

    public void executeStr(String sequence) {
        for (String move : sequence.split(" ")) {
            switch (move.charAt(move.length()-1)) {
                case '\'': // counterclockwise
                    executeMove(moveStr.indexOf(move.substring(0, move.length()-1)), 3);
                    break;
                case '2':
                    executeMove(moveStr.indexOf(move.substring(0, move.length()-1)), 2);
                    break;
                default:
                    executeMove(moveStr.indexOf(move), 1);
                    break;
            }
        }
    }
}

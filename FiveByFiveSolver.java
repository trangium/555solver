public class FiveByFiveSolver {
    public static void main(String[] args) {
        CenterCube pzl = new CenterCube();
        pzl.executeStr("R U Rw Uw Rw' Uw' Rw Uw Rw' Uw' Rw Uw Rw' Uw' Rw Uw Rw' Uw' Rw Uw Rw' Uw' Rw Uw Rw'");
        pzl.executeMove(6, 3);
        pzl.print();
    }
}

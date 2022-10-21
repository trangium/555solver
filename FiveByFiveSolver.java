import java.util.*;

public class FiveByFiveSolver {

    private static int h(CenterCube pzl) { // 1, 13, 19
        int acc = 0;
        if (pzl.getPerm()[1] != 0) acc++;
        if (pzl.getPerm()[13] != 1) acc++;
        if (pzl.getPerm()[19] != 2) acc++;
        return acc;
    }

    private static boolean isSolved(CenterCube pzl) {
        for (int i=0; i<CenterCube.numberOfPieces; i++) {
            if (pzl.getPerm()[i] != CenterCube.solved[i]) return false;
        }
        return true;
    }

    private static int solve(CenterCube rootPzl) {
        final int MAXDEPTH = 20;
        List<Stack<CenterCube>> openSetPzls = new ArrayList<Stack<CenterCube>>();
        for (int i=0; i<MAXDEPTH; i++) {
            openSetPzls.add(new Stack<CenterCube>());
        }
        List<Stack<Integer>> openSetMoves = new ArrayList<Stack<Integer>>();
        for (int i=0; i<MAXDEPTH; i++) {
            openSetMoves.add(new Stack<Integer>()); // g value
        }
        HashMap<CenterCube, Integer> closedSet = new HashMap<>();

        
        openSetPzls.get(h(rootPzl)).add(rootPzl);
        openSetMoves.get(h(rootPzl)).add(-1);
        int min_f = h(rootPzl);

        while (true) {
            while (openSetPzls.get(min_f).size() == 0) {
                min_f++;
            }
            CenterCube pzl = openSetPzls.get(min_f).pop();
            Integer moveIndex = openSetMoves.get(min_f).pop();

            if (closedSet.containsKey(pzl)) continue;
            closedSet.put(pzl, moveIndex);

            if (isSolved(pzl)) return min_f;

            for (int nextMoveIndex=0; nextMoveIndex < CenterCube.numberOfMoves; nextMoveIndex++) {
                for (int rot=1; rot<=3; rot++) {
                    CenterCube nbr = new CenterCube(pzl);
                    nbr.executeMove(nextMoveIndex, rot);
                    int new_f = h(nbr)+min_f-h(pzl)+1;
                    openSetPzls.get(new_f).push(nbr);
                    openSetMoves.get(new_f).push(nextMoveIndex*3+rot); // number from 1 to 36 representing both the type and direction of the move
                }
            }
        }
    }

    /*
     * 
    if not isSolvable(root, goal): return "X"
    openSet = [[] for i in range(160)] # create an openSet with root
    openSet[h(root)].append((root, None))
    min_f = h(root)
    closedSet = {} # create an empty closedSet

    while True:
        while not openSet[min_f]:
            min_f += 1
        pzl, direc = openSet[min_f].pop()
        if pzl in closedSet: continue
        closedSet[pzl] = direc
        if pzl == goal: return findCompactPath(root, goal, closedSet)
        
        for nbr in nbrs(pzl):
            newF = min_f - distanceTable[nbr[holePos]][posFrom] + distanceTable[nbr[holePos]][holePos] + 1
            openSet[newF].append((nbr, direc))
     */

    public static void main(String[] args) {
        CenterCube pzl = new CenterCube();
        pzl.executeStr("Rw F2 Rw Uw L");
        System.out.println(solve(pzl));
    }
}

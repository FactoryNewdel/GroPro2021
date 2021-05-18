package Utils;

import Data.Cube;
import Data.CubeType;
import Data.Storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle {
    private Storage storage;
    private String[][][] solution;

    public Puzzle(Storage storage) {
        this.storage = storage;
        solution = new String[storage.getDimX()][storage.getDimY()][storage.getDimZ()];
    }

    public void solve() {
        if (findFit(0, 0, 0, storage.getOrderedCubes())) {
            storage.setSolution(solution);
        }
    }

    private boolean findFit(int x, int y, int z, HashMap<CubeType, List<String>> map) {
        int connections = 0;
        if (x - 1 > 0) connections++;
        if (y - 1 > 0) connections++;
        if (z - 1 > 0) connections++;
        if (x + 1 < storage.getDimX()) connections++;
        if (y + 1 < storage.getDimY()) connections++;
        if (z + 1 < storage.getDimZ()) connections++;
        CubeType curType;
        switch (connections) {
            case 3: curType = CubeType.ECKE;  break;
            case 4: curType = CubeType.KANTE break;
            case 5: curType = CubeType.SEITE; break;
            case 6: curType = CubeType.MITTE; break;
            default: throw new RuntimeException("RIP");
        }
        for (String cubeName : map.get(curType)) {
            Cube cube = storage.getCube(cubeName);
            do {
                if (!isFitting(cube)) {
                    cube.nextRotation();
                    continue;
                }
                if (map.get(CubeType.ECKE).isEmpty() && map.get(CubeType.KANTE).isEmpty() && map.get(CubeType.SEITE).isEmpty() && map.get(CubeType.MITTE).isEmpty()) return true;
                HashMap<CubeType, List<String>> temp = (HashMap<CubeType, List<String>>) map.clone();
                temp.get(curType).remove(cubeName);
                x++;
                if (x > storage.getDimX()) {
                    x -= storage.getDimX();
                    y++;
                }
                if (y > storage.getDimY()) {
                    y -= storage.getDimY();
                    z++;
                }
                if (findFit(x, y, z, temp)) return true;
            } while (cube.hasNextRotation(x, y, z));
        }
    }

    private boolean isFitting(Cube cube) {

    }
}

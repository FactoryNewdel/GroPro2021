package Utils;

import Data.*;

import static Data.Direction.*;

import java.util.*;


public class Puzzle {
    private Storage storage;
    private Vektor dimVektor;
    private String[][][] solution;

    public Puzzle(Storage storage) {
        this.storage = storage;
        dimVektor = storage.getDimVector();
        solution = new String[dimVektor.x][dimVektor.y][dimVektor.z];
    }

        HashMap<CubeType, ArrayList<String>> map;
    public void solve() {
        map = storage.getOrderedCubes();
        boolean b = dimVektor.x > 1 && dimVektor.y > 1 && dimVektor.z > 1;
        if (b) {
            Cube startCube = storage.getCube(map.get(CubeType.ECKE).get(0));
            map.get(CubeType.ECKE).remove(startCube.getName());
            while (!startCube.checkBounds(0, 0, 0, dimVektor)) startCube.nextRotation(0, 0, 0, dimVektor);
            solution[0][0][0] = startCube.getName();
        }
        if (findFitOrdered(b ? 1 : 0, 0, 0/*, map*/)) {
            storage.setSolution(solution);
        }
    }

    private boolean findFitOrdered(int x, int y, int z/*, HashMap<CubeType, ArrayList<String>> map*/) {
        //System.out.println(Arrays.deepToString(solution));
        //System.out.println(map);
        int connections = 0;
        if (x > 0) connections++;
        if (y > 0) connections++;
        if (z > 0) connections++;
        if (x < dimVektor.x - 1) connections++;
        if (y < dimVektor.y - 1) connections++;
        if (z < dimVektor.z - 1) connections++;

        CubeType curType = Cube.getCubeType(dimVektor, connections);


        //for (String cubeName : map.get(curType)) {
        ArrayList<String> list = map.get(curType);
        for (int i = 0; i < list.size(); i++) {
            String cubeName = list.get(i);
            Cube cube = storage.getCube(cubeName);

            while (true) {
                if (!isFitting(x, y, z, cube)) {
                    if (!cube.hasNextRotation(true)) break;
                    cube.nextRotation(x, y, z, dimVektor);
                    continue;
                }

                //if (y > 2) System.out.println("y > 2: " + Arrays.deepToString(solution));
                if (z > 8 && y > 8 && x > -1) System.out.println("z > 2: " + Arrays.deepToString(solution));
                //System.out.println("Solution1 = " + Arrays.deepToString(solution));
                solution[x][y][z] = cubeName;
                //System.out.println("Solution2 = " + Arrays.deepToString(solution));
                if (x == dimVektor.x - 1 && y == dimVektor.y - 1 && z == dimVektor.z - 1) {
                    System.out.println(Arrays.deepToString(solution));
                    return true;
                }

                //ArrayList<String> temp = (ArrayList<String>) map.get(curType).clone();
                //temp.put(curType, new ArrayList<>());
                /*for (String cloneName : map.get(curType)) {
                    temp.get(curType).add(cloneName);
                }*/
                //temp.get(curType).remove(cubeName);
                //System.out.println(x + "," + y + "," + z + ": " + Arrays.deepToString(solution));
                //temp.remove(cubeName);
                //map.put(curType, temp);
                //map.get(curType).add("-" + cubeName);
                //System.out.println("Removing");
                list.remove(i);
                int nextX = x + 1;
                int nextY = y;
                int nextZ = z;
                if (nextX > dimVektor.x - 1) {
                    nextX -= dimVektor.x;
                    nextY++;
                }
                if (nextY > dimVektor.y - 1) {
                    nextY -= dimVektor.y;
                    nextZ++;
                }
                if (findFitOrdered(nextX, nextY, nextZ)) return true;
                //System.out.println("Solution3 = " + Arrays.deepToString(solution));
                solution[x][y][z] = null;
                list.add(i, cubeName);
                if (!cube.hasNextRotation(true)) break;
                cube.nextRotation(x, y, z, dimVektor);
                //System.out.println("Solution4 = " + Arrays.deepToString(solution));
            }
        }
        return false;
    }

    private boolean isFitting(int x, int y, int z, Cube cube) {
        //if (cube.getName().equals("Teil 14")) System.out.println(cube);
        Cube cube1;
        if (x > 0 && (cube1 = storage.getCube(solution[x - 1][y][z])) != null) {
            if (!trianglesFit(cube.getTriangle(BACK), cube1.getTriangle(FRONT), false)) return false;
        }
        if (x < dimVektor.x - 1 && (cube1 = storage.getCube(solution[x + 1][y][z])) != null) {
            if (!trianglesFit(cube.getTriangle(FRONT), cube1.getTriangle(BACK), false)) return false;
        }
        if (y > 0 && (cube1 = storage.getCube(solution[x][y - 1][z])) != null) {
            if (!trianglesFit(cube.getTriangle(LEFT), cube1.getTriangle(RIGHT), false)) return false;
        }
        if (y < dimVektor.y - 1 && (cube1 = storage.getCube(solution[x][y + 1][z])) != null) {
            if (!trianglesFit(cube.getTriangle(RIGHT), cube1.getTriangle(LEFT), false)) return false;
        }
        if (z > 0 && (cube1 = storage.getCube(solution[x][y][z - 1])) != null) {
            if (!trianglesFit(cube.getTriangle(DOWN), cube1.getTriangle(UP), true)) return false;
        }
        if (z < dimVektor.z - 1 && (cube1 = storage.getCube(solution[x][y][z + 1])) != null) {
            if (!trianglesFit(cube.getTriangle(UP), cube1.getTriangle(DOWN), true)) return false;
        }
        return true;
    }

    private boolean trianglesFit(int triangle, int triangle1, boolean topBottom) {
        if (triangle == 0 || triangle1 == 0) return false;

        if (topBottom) {
            if (triangle == 1 && triangle1 != 4) return false;
            if (triangle == 4 && triangle1 != 1) return false;
            if (triangle == 2 && triangle1 != 3) return false;
            if (triangle == 3 && triangle1 != 2) return false;
        } else {
            if (triangle == 1 && triangle1 != 2) return false;
            if (triangle == 2 && triangle1 != 1) return false;
            if (triangle == 3 && triangle1 != 4) return false;
            if (triangle == 4 && triangle1 != 3) return false;
        }

        return true;
    }
}

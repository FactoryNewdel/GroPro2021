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

    public void solve() {
        storage.orderGroups();
        HashMap<CubeType, ArrayList<String>> map = storage.getOrderedCubes();
        System.out.println(map);
        Cube startCube = storage.getCube(map.get(CubeType.ECKE).get(0));
        map.get(CubeType.ECKE).remove(startCube.getName());
        while (!startCube.checkBounds(0, 0, 0, dimVektor)) startCube.nextRotation(0, 0, 0, dimVektor);
        solution[0][0][0] = startCube.getName();

        /*Cube cube100 = storage.getCube("Teil 14"/*map.get(CubeType.ECKE).get(0));
        while (cube100.hasNextRotation(true)) {
            cube100.nextRotation(1,0,0, dimVektor);
            System.out.println(cube100);
        }
        map.get(CubeType.KANTE).remove(cube100.getName());
        while (!cube100.checkBounds(1, 0, 0, dimVektor)) cube100.nextRotation(1,0,0, dimVektor);
        cube100.nextRotation(1,0,0, dimVektor);
        while (!cube100.checkBounds(1, 0, 0, dimVektor)) cube100.nextRotation(1,0,0, dimVektor);
        cube100.nextRotation(1,0,0, dimVektor);
        solution[1][0][0] = cube100.getName();

        Cube cube200 = storage.getCube("Teil 22"/*map.get(CubeType.ECKE).get(0));
        map.get(CubeType.ECKE).remove(cube200.getName());
        while (!cube200.checkBounds(2, 0, 0, dimVektor)) cube200.nextRotation(2,0,0, dimVektor);
        solution[2][0][0] = cube200.getName();*/

        if (findFitOrdered(1, 0, 0, map)) {
            storage.setSolution(solution);
        }
        System.out.println("should = " + should);
        System.out.println("did = " + did);
    }

    int should = 0;
    int did = 0;

    private boolean findFitOrdered(int x, int y, int z, HashMap<CubeType, ArrayList<String>> map) {
        //System.out.println(Arrays.deepToString(solution));
        //System.out.println(map);
        int connections = 0;
        if (x > 0) connections++;
        if (y > 0) connections++;
        if (z > 0) connections++;
        if (x < dimVektor.x - 1) connections++;
        if (y < dimVektor.y - 1) connections++;
        if (z < dimVektor.z - 1) connections++;
        CubeType curType;
        switch (connections) {
            case 3:
                curType = CubeType.ECKE;
                break;
            case 4:
                curType = CubeType.KANTE;
                break;
            case 5:
                curType = CubeType.SEITE;
                break;
            case 6:
                curType = CubeType.MITTE;
                break;
            default:
                throw new RuntimeException("RIP: " + connections);
        }
        should += map.get(curType).size();
        for (String cubeName : map.get(curType)) {
            did++;
            Cube cube = storage.getCube(cubeName);
            /*if (x == 2 && y == 2 && z == 0) {
                System.out.println("Remaining = " + map.get(CubeType.KANTE));
                System.out.println("name = " + cubeName);
                System.out.println(storage.getCube("Teil 27"));
                System.out.println("lol: " + Arrays.deepToString(solution));
            }*/
            //if (z > 1) System.out.println(Arrays.deepToString(solution));
            if (solution[0][0][0].equals("Teil 27") && solution[1][0][0] != null && solution[1][0][0].equals("Teil 14") && solution[2][0][0] != null && solution[2][0][0].equals("Teil 22")
                    && solution[0][1][0] != null && solution[0][1][0].equals("Teil 6") && solution[1][1][0] != null && solution[1][1][0].equals("Teil 3") && solution[2][1][0] != null && solution[2][1][0].equals("Teil 3")) {
                //System.out.println("Solution = " + Arrays.deepToString(solution));
                /*System.out.println(storage.getCube(solution[0][0][0]));
                System.out.println(storage.getCube(solution[1][0][0]));
                System.out.println(storage.getCube(solution[2][0][0]));
                System.out.println(storage.getCube(solution[0][1][0]));
                System.out.println(storage.getCube(solution[1][1][0]));
                System.out.println(storage.getCube(solution[2][1][0]));*/
            }

            while (true) {
                /*if (cube.getName().equals("Teil 14") && x == 1 && y == 0) {
                    System.out.println(x + "," + y + "," + z);
                    System.out.println(cube.hasNextRotation(true));
                    while (cube.hasNextRotation(true)) {
                        cube.nextRotation(1, 0, 0, new Vektor(3, 3, 3));
                        System.out.println(cube);
                    }
                    return false;
                }*/
                if (!isFitting(x, y, z, cube)) {
                    if (!cube.hasNextRotation(true)) break;
                    cube.nextRotation(x, y, z, dimVektor);
                    continue;
                }
                //System.out.println("Solution1 = " + Arrays.deepToString(solution));
                solution[x][y][z] = cubeName;
                //System.out.println("Solution2 = " + Arrays.deepToString(solution));
                if (x == dimVektor.x - 1 && y == dimVektor.y - 1 && z == dimVektor.z - 1) {
                    System.out.println(Arrays.deepToString(solution));
                    return true;
                }

                HashMap<CubeType, ArrayList<String>> temp = (HashMap<CubeType, ArrayList<String>>) map.clone();
                temp.put(curType, new ArrayList<>());
                for (String cloneName : map.get(curType)) {
                    temp.get(curType).add(cloneName);
                }
                temp.get(curType).remove(cubeName);
                //System.out.println(x + "," + y + "," + z + ": " + Arrays.deepToString(solution));
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
                if (findFitOrdered(nextX, nextY, nextZ, temp)) return true;
                //System.out.println("Solution3 = " + Arrays.deepToString(solution));
                solution[x][y][z] = null;
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

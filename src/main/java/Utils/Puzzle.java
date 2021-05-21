package Utils;

import Data.*;

import static Data.Direction.*;

import java.util.*;


public class Puzzle {
    private Storage storage;
    private Vektor dimVektor;
    private String[][][] solution;

    private HashMap<CubeType, ArrayList<String>> map;

    /**
     * Berechnet Dimensionsvektor aus Storage-Objekt und
     * legt leeres solution-Objekt (3-dim-Array) an
     *
     * @param storage Objekt, das alle Daten aus der Eingabedatei enthält
     */
    public Puzzle(Storage storage) {
        this.storage = storage;
        dimVektor = storage.getDimVector();
        solution = new String[dimVektor.x][dimVektor.y][dimVektor.z];
    }

    /**
     * Sucht eine Lösung für diese Datei/dieses Storage-Objekt
     */
    public void solve() {
        map = storage.getOrderedCubes();
        Cube startCube = storage.getCube(map.get(CubeType.ECKE).get(0));
        map.get(CubeType.ECKE).remove(startCube.getName());
        while (!startCube.checkBounds(0, 0, 0, dimVektor)) startCube.nextRotation(0, 0, 0, dimVektor);
        solution[0][0][0] = startCube.getName();
        Vektor v = new Vektor(0,0,0);
        if (dimVektor.x > 1) v.x += 1;
        else if (dimVektor.y > 1) v.y += 1;
        else if (dimVektor.z > 1) v.z += 1;
        if (findFitOrdered(v.x, v.y, v.z)) {
            storage.setSolution(solution);
        }
    }

    /**
     * Sucht passendes Teil und ruft sich selbst auf, wenn gefunden
     *
     * @param x x-Koordinate im Quader
     * @param y y-Koordinate im Quader
     * @param z z-Koordinate im Quader
     * @return ob eine Lösung gefunden wurde
     */
    private boolean findFitOrdered(int x, int y, int z/*, HashMap<CubeType, ArrayList<String>> map*/) {
        int connections = 0;
        if (x > 0) connections++;
        if (y > 0) connections++;
        if (z > 0) connections++;
        if (x < dimVektor.x - 1) connections++;
        if (y < dimVektor.y - 1) connections++;
        if (z < dimVektor.z - 1) connections++;

        CubeType curType = Cube.getCubeType(dimVektor, connections);

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
                solution[x][y][z] = cubeName;
                if (x == dimVektor.x - 1 && y == dimVektor.y - 1 && z == dimVektor.z - 1) {
                    System.out.println(Arrays.deepToString(solution));
                    return true;
                }
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

                solution[x][y][z] = null;
                list.add(i, cubeName);
                if (!cube.hasNextRotation(true)) break;
                cube.nextRotation(x, y, z, dimVektor);
            }
        }
        return false;
    }

    /**
     * Schaut, ob Würfel neben die anderen schon besetzen Positionen passt
     *
     * @param x    x-Koordinate an der gesucht wird
     * @param y    y-Koordinate an der gesucht wird
     * @param z    z-Koordinate an der gesucht wird
     * @param cube Würfel, der an diese Stelle gesetzt werden soll
     * @return ob der Würfel passt
     */
    private boolean isFitting(int x, int y, int z, Cube cube) {
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

    /**
     * Prüft, ob 2 Dreiecke beim zusammen legen passen
     *
     * @param triangle  Erstes Dreieck zur Überprüfung
     * @param triangle1 Zweites Dreieck zur Überprüfung
     * @param topBottom ob an Stelle 1 oder 6 (Oben oder Unten) geschaut wird
     * @return ob Dreiecke passen
     */
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

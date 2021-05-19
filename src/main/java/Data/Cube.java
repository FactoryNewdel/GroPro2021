package Data;

import java.util.Arrays;
import java.util.HashMap;

import static Data.Direction.*;

public class Cube {
    private String name;
    private int[] triangle;
    private CubeType cubeType = null;
    private int rotations = 0;


    public Cube(String name, int[] triangle) {
        this.name = name;
        this.triangle = triangle;
        lastState = triangle;
    }

    public Cube(Cube other) {
        this.name = other.name;
        this.triangle = other.triangle;
        this.cubeType = other.cubeType;
    }

    public Cube clone() {
        return new Cube(this);
    }

    public String getName() {
        return name;
    }

    public CubeType getType(Vektor dim) {

        if (cubeType != null) return cubeType;
        int triangles = 0;
        for (int i : triangle) {
            if (i != 0) triangles++;
        }
        cubeType = getCubeType(dim, triangles);
        return cubeType;
    }

    public static CubeType getCubeType(Vektor dim, int connections) {
        int oneDimension = (dim.x == 1 ? 1 : 0) + (dim.y == 1 ? 1 : 0) + (dim.z == 1 ? 1 : 0);
        if (oneDimension == 0) {
            switch (connections) {
                case 3: return CubeType.ECKE;
                case 4: return CubeType.KANTE;
                case 5: return CubeType.SEITE;
                case 6: return CubeType.MITTE;
                default: return null;
            }
        } else if (oneDimension == 1) {
            switch (connections) {
                case 2: return CubeType.ECKE;
                case 3: return CubeType.KANTE;
                case 4: return CubeType.MITTE;
                default: return null;
            }
        }
        else if (oneDimension == 2) {
            switch (connections) {
                case 1: return CubeType.ECKE;
                case 2: return CubeType.MITTE;
                default: return null;
            }
        } else throw new RuntimeException("RIP4");
    }

    public int getTriangle(int index) {
        return triangle[index];
    }

    public int getTriangle(Direction direction) {
        return triangle[direction.i];
    }

    public int[] getTriangles() {
        return triangle;
    }

    public boolean hasNextRotation(boolean reset) {
        if (rotations != 24) return true;
        if (reset) rotations = 0;
        return false;
    }

    private int[] lastState;
    int i = 0;
    public void nextRotation(int x, int y, int z, Vektor dim) {
        //lastState = triangle;
        do {
            rotations++;
            rotateX(true);
            switch (rotations) {
                case 4:
                case 8:
                case 12:
                    rotateZ(true);
                    break;
                case 16:
                    rotateZ(true);
                    rotateY(true);
                    break;
                case 20:
                    rotateY(true);
                    rotateY(true);
                    break;
                case 24:
                    rotateY(true);
                    break;
            }
        } while (!checkBounds(x, y, z, dim) && hasNextRotation(false));
    }

    public boolean checkBounds(int x, int y, int z, Vektor dim) {
        if (x == 0 && triangle[BACK.i] != 0) return false;
        if (x == dim.x - 1 && triangle[FRONT.i] != 0) return false;
        if (y == 0 && triangle[LEFT.i] != 0) return false;
        if (y == dim.y - 1 && triangle[RIGHT.i] != 0) return false;
        if (z == 0 && triangle[DOWN.i] != 0) return false;
        if (z == dim.z - 1 && triangle[UP.i] != 0) return false;
        return true;
    }

    public void rotateX(boolean clockwise) {
        int[] rotated = new int[6];
        if (clockwise) {
            rotated[UP.i] = rotateTriangle(triangle[LEFT.i], true);
            rotated[LEFT.i] = rotateTriangle(triangle[DOWN.i], true);
            rotated[DOWN.i] = rotateTriangle(triangle[RIGHT.i], true);
            rotated[RIGHT.i] = rotateTriangle(triangle[UP.i], true);
        } else {
            rotated[UP.i] = rotateTriangle(triangle[RIGHT.i], false);
            rotated[LEFT.i] = rotateTriangle(triangle[UP.i], false);
            rotated[DOWN.i] = rotateTriangle(triangle[LEFT.i], false);
            rotated[RIGHT.i] = rotateTriangle(triangle[DOWN.i], false);
        }
        rotated[FRONT.i] = rotateTriangle(triangle[FRONT.i], clockwise);
        rotated[BACK.i] = rotateTriangle(triangle[BACK.i], !clockwise);
        triangle = rotated;
    }

    public void rotateY(boolean clockwise) {
        int[] rotated = new int[6];
        if (clockwise) {
            rotated[UP.i] = triangle[FRONT.i];
            rotated[FRONT.i] = triangle[DOWN.i];
            rotated[DOWN.i] = rotateTriangle(rotateTriangle(triangle[BACK.i], true), true);
            rotated[BACK.i] = rotateTriangle(rotateTriangle(triangle[UP.i], true), true);
        } else {
            rotated[UP.i] = rotateTriangle(rotateTriangle(triangle[BACK.i], true), true);
            rotated[FRONT.i] = triangle[UP.i];
            rotated[DOWN.i] = triangle[FRONT.i];
            rotated[BACK.i] = rotateTriangle(rotateTriangle(triangle[DOWN.i], true), true);
        }
        rotated[LEFT.i] = rotateTriangle(triangle[LEFT.i], !clockwise);
        rotated[RIGHT.i] = rotateTriangle(triangle[RIGHT.i], clockwise);
        triangle = rotated;
    }

    public void rotateZ(boolean clockwise) {
        int[] rotated = new int[6];
        if (clockwise) {
            rotated[LEFT.i] = triangle[FRONT.i];
            rotated[FRONT.i] = triangle[RIGHT.i];
            rotated[RIGHT.i] = triangle[BACK.i];
            rotated[BACK.i] = triangle[LEFT.i];
        } else {
            rotated[LEFT.i] = triangle[BACK.i];
            rotated[FRONT.i] = triangle[LEFT.i];
            rotated[RIGHT.i] = triangle[FRONT.i];
            rotated[BACK.i] = triangle[RIGHT.i];
        }
        rotated[UP.i] = rotateTriangle(triangle[UP.i], clockwise);
        rotated[DOWN.i] = rotateTriangle(triangle[DOWN.i], !clockwise);
        triangle = rotated;
    }

    private int rotateTriangle(int value, boolean clockwise) {
        if (value == 0) return 0;
        if (clockwise) {
            if (value == 4) return 1;
            else return value + 1;
        } else {
            if (value == 1) return 4;
            else return value - 1;
        }
    }

    @Override
    public String toString() {
        return name + ": " + Arrays.toString(triangle);
    }
}

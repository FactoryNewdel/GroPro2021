package Data;

public enum Direction {
    UP(1, 0, 0, 1),
    LEFT(2, 0, 1, 0),
    FRONT(3, 1, 0, 0),
    RIGHT(4, 0, -1, 0),
    BACK(5, -1, 0, 0),
    DOWN(6, 0, 0, -1);

    Direction(int i, int i1, int i2, int i3) {
    }
}

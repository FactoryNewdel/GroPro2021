package Data;

public enum Direction {
    UP(0),
    LEFT(1),
    FRONT(2),
    RIGHT(3),
    BACK(4),
    DOWN(5);

    public int i;

    Direction(int i) {
        this.i = i;
    }
}

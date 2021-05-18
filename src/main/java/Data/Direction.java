package Data;

public enum Direction {
    UP(1),
    LEFT(2),
    FRONT(3),
    RIGHT(4),
    BACK(5),
    DOWN(6);

    private int i;

    Direction(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }
}

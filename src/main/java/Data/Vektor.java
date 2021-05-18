package Data;

public class Vektor {
    public int x;
    public int y;
    public int z;

    public Vektor(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" + x + y + z + ')';
    }

    public void setValues(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

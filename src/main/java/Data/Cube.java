package Data;

public class Cube {
    private String name;
    private int[] triangle;
    private CubeType cubeType = null;

    public Cube(String name, int[] triangle) {
        this.name = name;
        this.triangle = triangle;
    }

    public String getName() {
        return name;
    }

    public CubeType getType() {
        if (cubeType != null) return cubeType;
        int triangles = 0;
        for (int i : triangle) {
            if (i != 0) triangles++;
        }
        switch (triangles) {
            case 3: cubeType = CubeType.ECKE; break;
            case 4: cubeType = CubeType.KANTE; break;
            case 5: cubeType = CubeType.SEITE; break;
            case 6: cubeType = CubeType.MITTE; break;
            default: throw new RuntimeException("WTF");
        }
        return cubeType;
    }

    public int getTriangle(int index) {
        return triangle[index];
    }

    //TODO toString
}

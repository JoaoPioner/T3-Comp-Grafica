public class Ponto {
    public float x;
    public float y;
    public float z;

    public Ponto(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" +
                x +
                ", " + y +
                ", " + z +
                ')';
    }
}

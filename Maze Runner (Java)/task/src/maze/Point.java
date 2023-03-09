package maze;

public class Point {
    int y;
    int x;

    public Point(int y, int x) {
        this.y = y;
        this.x = x;
    }
    @Override
    public int hashCode() {
        return 10000 * this.y + this.x;
    }
    @Override
    public boolean equals(Object obj) {
        Point objP = (Point) obj;
        if (this.x == objP.x && this.y == objP.y) {
            return true;
        }
        return false;
    }
}

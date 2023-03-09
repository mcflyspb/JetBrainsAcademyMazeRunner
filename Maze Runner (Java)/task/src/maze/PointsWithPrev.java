package maze;

public class PointsWithPrev extends Point {
    int prevX;
    int prevY;

    public PointsWithPrev(Point p, int prevY, int prevX) {
        super(p.y,p.x);
        this.prevX = prevX;
        this.prevY = prevY;
    }


    public PointsWithPrev(int y, int x) {
        super(y, x);
    }
}

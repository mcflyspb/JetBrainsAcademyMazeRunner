package maze;

import com.sun.security.auth.NTSid;

import java.awt.*;

public class Vertex {
    Point point1;
    Point point2;
    Point prevPoint;
    int weight;
    boolean mst;

    public Vertex(Point point1, Point point2, int weght) {
        this.point1 = point1;
        this.point2 = point2;
        this.prevPoint = new Point(0,0);
        this.weight = weght;
        this.mst = false;
    }

    public void setPrevPoint(Point prevPoint) {
        this.prevPoint = prevPoint;
    }

    public void setMst() {
        this.mst = true;
    }
}

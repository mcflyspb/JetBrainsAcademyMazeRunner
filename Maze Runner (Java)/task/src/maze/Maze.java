package maze;

import java.util.*;

public class Maze {
    int[][] field;
    int mstLength = 0;
    int maxX = 0;
    int maxY = 0;
    public static final int MAX_RANDOM_WEIGHT = 100;
    List<Vertex> vertexList = new ArrayList<>();

    Maze(int y, int x) {
        createField(y,x);
        createVertexList();
        findMst();
        correctField();
    }

    private void correctField() {
        boolean makeExit = true;
        for (Vertex v : vertexList) {
            if (v.mst) {
                if (v.point1.x == v.point2.x) {
                    int y = Math.min(v.point1.y,v.point2.y);
                    field[2 * y + 2][2 * v.point1.x + 1] = 1;
                    if (makeExit) {
                        field[2 * y + 2][2 * v.point1.x + 1] = 0;
                        makeExit = false;
                    }
                }
                if (v.point1.y == v.point2.y) {
                    int x = Math.min(v.point1.x,v.point2.x);
                    field[2 * v.point1.y + 1][2 * x + 2] = 1;
                }
            }
        }
        int y = field.length;
        int x = field[0].length;

        for (int i = 0; i < y; i++) {
            field[i][x - 1] = 1;
        }

        for (int i = y - 2; i > 1; i--) {
            if (field[i][x - 2] == 0) {
                field[i][x - 1] = 0;
                break;
            }
        }
    }

    private void findMst() {
        findFirstMstPath();
        List<Vertex> vertexWithMst;
        HashSet<Point> pointsInMstPath;
        while (mstLength > 1) {
            vertexWithMst = findAllVertexWithMst();
            pointsInMstPath = findAllPointsInMstPath(vertexWithMst);
            findNextEdgeAndUpdateMstList(pointsInMstPath);
            mstLength--;
        }
    }

    private HashSet<Vertex> getAvailableVertex(HashSet<Point> pointsInMstPath) {
        HashSet<Vertex> availableVertexList = new HashSet<>();
        for (Vertex v : vertexList) {
            int match = 0;
            for (Point p : pointsInMstPath) {
                match = match + findPointsInVertex(v,p);
            }
            if (match < 2) {
                availableVertexList.add(v);
            }
        }
        return availableVertexList;
    }

    private int findPointsInVertex(Vertex v, Point p) {
        if (p.equals(v.point1) || p.equals(v.point2)) {
            return 1;
        }
        return 0;
    }



    private void findNextEdgeAndUpdateMstList(HashSet<Point> pointsInMstPath) {
        int minWeight = MAX_RANDOM_WEIGHT + 1;
        Point point1 = null;
        Point point2 = null;
        HashSet<Vertex> availableVertexList = getAvailableVertex(pointsInMstPath);
        for (Point p : pointsInMstPath) {
            for (Vertex v: availableVertexList) {
                if ( (p.equals(v.point1) || p.equals(v.point2)) && !v.mst ) {
                    if (minWeight > v.weight) {
                        minWeight = v.weight;
                        point1 = v.point1;
                        point2 = v.point2;
                    }
                }
            }
        }
        for (Vertex v: vertexList) {
            Vertex vertex = new Vertex(point1,point2,0);
            if (compareVertex(v,vertex)) {
                v.mst = true;
            }
        }
    }

    private boolean compareVertex(Vertex v1, Vertex v2) {
        if (v1.point1.equals(v2.point1) && v1.point2.equals(v2.point2)) {
            return true;
        }
        return v1.point1.equals(v2.point2) && v2.point2.equals(v2.point1);
    }


    private HashSet<Point> findAllPointsInMstPath(List<Vertex> vertexWithMst) {
        HashSet<Point> pointsInMstPath = new HashSet<>();
        for (Vertex v : vertexWithMst) {
            pointsInMstPath.add(v.point1);
            pointsInMstPath.add(v.point2);
        }
        return pointsInMstPath;
    }

    private List<Vertex> findAllVertexWithMst() {
        List<Vertex> vertexWithMst = new ArrayList<>();
        for (Vertex v : vertexList) {
            if (v.mst) {
                vertexWithMst.add(v);
            }
        }
        return vertexWithMst;
    }

    private void findFirstMstPath() {
        for (int i = 0; i < MAX_RANDOM_WEIGHT; i++) {
            for (Vertex v : vertexList) {
                if (i == v.weight) {
                    v.setMst();
                    return;
                }
            }
        }
    }

    private void createField(int y, int x) {
        field = new int[y + 1][x + 1];
        maxX = (x - 1) / 2;
        maxY = (y - 1) / 2;
        if (y % 2 == 1) {
            maxY = y / 2;
        }

        for (int i = 0; i < x + 1; i++) {
            field[y][i] = 1;
        }


        mstLength = (maxY + 1)  * (maxX + 1) - 1;
        for (int i = 1; i < y; i += 2) {
            for (int j = 1; j < x; j += 2) {
                field[i][j] = 1;
            }
        }
    }

    private void createVertexList() {
        Point point1;
        Point point2;
        int weight;
        Vertex vertex;
        Random random = new Random();
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                point1 = new Point(y,x);
                point2 = new Point(y + 1,x);
                weight = random.nextInt(MAX_RANDOM_WEIGHT);
                if (x == 0) {
                    weight = 1;
                }
                vertex = new Vertex(point1,point2,weight);
                vertexList.add(vertex);
                point2 = new Point(y,x + 1);
                weight = random.nextInt(MAX_RANDOM_WEIGHT);
                if (y == 0) {
                    weight = 1;
                }
                vertex = new Vertex(point1,point2,weight);
                vertexList.add(vertex);
            }
        }
        for (int y = 0; y < maxY; y++) {
            point1 = new Point(y,maxX);
            point2 = new Point(y + 1,maxX);
            weight = random.nextInt(10);
            vertex = new Vertex(point1,point2,weight);
            vertexList.add(vertex);
        }
        for (int x = 0; x < maxX; x++) {
            point1 = new Point(maxY,x);
            point2 = new Point(maxY,x + 1);
            weight = random.nextInt(2);
            vertex = new Vertex(point1,point2,weight);
            vertexList.add(vertex);
        }
    }

    public void printField() {
        for (int y = 1; y < field.length; y++) {
            for (int x = 1; x < field[0].length; x++) {
                if (field[y][x] == 1) {
                    System.out.print("██");
                } else if (field[y][x] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print("//");
                }
            }
            System.out.println();
        }
    }

    public void findTheEscape() {
        Point startPoint = findStartPointOfMaze();
        Point finishPoint = findFinishPointOfMaze();
        HashSet<PointsWithPrev> nextPoints = new HashSet<>();
        nextPoints.add(new PointsWithPrev(startPoint, 0, 0));
        calculatePathFromStartToFinish(nextPoints, finishPoint);
        updateFieldDrawPath(nextPoints, finishPoint);
    }

    private void calculatePathFromStartToFinish(HashSet<PointsWithPrev> nextPoints, Point finishPoint) {
        boolean continueLoop = true;
        while(continueLoop) {
            HashSet<PointsWithPrev> iteratorNextPoints = new HashSet<>(nextPoints);
            for (PointsWithPrev p : iteratorNextPoints) {
                HashSet<PointsWithPrev> foundedNextPoints = findNextPoits(p);
                for (PointsWithPrev newPoint : foundedNextPoints) {
                    if (newPoint.y == finishPoint.y && newPoint.x == finishPoint.x) {
                        continueLoop = false;
                    }
                    nextPoints.add(newPoint);
                }
            }
        }
    }

    private void updateFieldDrawPath(HashSet<PointsWithPrev> nextPoints, Point finishPoint) {
        while (true) {
            for (PointsWithPrev p : nextPoints) {
                if (p.y == finishPoint.y && p.x == finishPoint.x) {
                    finishPoint = new Point(p.prevY,p.prevX);
                    field[p.y][p.x] = 2;
                } else if (finishPoint.y == 0 && finishPoint.x == 0) {
                    return;
                }
            }
        }
    }

    private HashSet<PointsWithPrev> findNextPoits(PointsWithPrev p) {
        HashSet<PointsWithPrev> foundedNextPoints = new HashSet<>();
        if (field[p.y][p.x + 1] != 1 && !(p.y == p.prevY && p.x + 1 == p.prevX)) {
            Point point = new Point(p.y,p.x + 1);
            foundedNextPoints.add(new PointsWithPrev(point, p.y,p.x));
        }

        if (field[p.y][p.x - 1] != 1 && !(p.y == p.prevY && p.x - 1 == p.prevX) && p.x > 1 ) {
            Point point = new Point(p.y,p.x - 1);
            foundedNextPoints.add(new PointsWithPrev(point, p.y,p.x));
        }

        if (field[p.y + 1][p.x] != 1 && !(p.y + 1 == p.prevY && p.x == p.prevX)) {
            Point point = new Point(p.y + 1,p.x);
            foundedNextPoints.add(new PointsWithPrev(point, p.y,p.x));
        }

        if (field[p.y - 1][p.x] != 1 && !(p.y - 1 == p.prevY && p.x == p.prevX)) {
            Point point = new Point(p.y - 1,p.x);
            foundedNextPoints.add(new PointsWithPrev(point, p.y,p.x));
        }
        return foundedNextPoints;
    }


    private Point findFinishPointOfMaze() {
        Point point = null;
        int row = field[0].length - 1;
        for (int y = 1; y < field.length - 1; y++) {
            if (field[y][row] != 1) {
                point = new Point(y,row);
            }
        }
        return point;
    }

    private Point findStartPointOfMaze() {
        Point point = null;
        int row = 1;
        for (int y = 1; y < field.length - 1; y++) {
           if (field[y][row] != 1) {
               point = new Point(y,row);
           }
        }
        return point;
    }
}

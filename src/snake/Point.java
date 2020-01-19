package snake;

public class Point {
    int x;
    int y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }
    Point add(Point other){
        return new Point(this.x + other.x, this.y + other.y);
    }
    @Override
    public String toString() {
        return "snake.Point(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }
}

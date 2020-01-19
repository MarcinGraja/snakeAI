package snake;

public enum Direction {
    UP(0, -1),
    Down(0,1),
    LEFT(-1,0),
    RIGHT(1,0);
    private Point direction;
    Direction(int x, int y) {
        direction = new Point(x,y);
    }
    public Point getVector() {
        return direction;
    }
}

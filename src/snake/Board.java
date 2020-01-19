package snake;

public class Board {
    private int[][] board;
    private int sizeX;
    private int sizeY;

    public Board(int sizeX, int sizeY) {
        this.board = new int[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    int getPoint(int x, int y){
        return board[x][y];
    }
    int getPoint(Point point){
        return board[point.x][point.y];
    }
    void changePoint(Point point, int value){
        board[point.x][point.y] = value;
    }
    public int[][] getBoard() {
        return board;
    }
    public boolean isInBounds(Point p){
        return (p.x >= 0 && p.x < sizeX && p.y >= 0 && p.y < sizeY);
    }
}

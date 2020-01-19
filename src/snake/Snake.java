package snake;

import java.util.LinkedList;

class Snake {
    private final Board board;
    private LinkedList<Point> snake = new LinkedList<>();

    public Snake(Board board) {
        this.board = board;
    }

    public boolean add(Point p){
        board.changePoint(p, -1);
        return snake.add(p);
    }
    public Point get(int index){
        return snake.get(index);
    }
    public Point getFirst(){
        return snake.peekFirst();
    }
    public Point getLast(){
        return snake.peekLast();
    }
    public Point remove(int index){
        board.changePoint(snake.get(index), 0);
        return snake.remove(index);
    }
    public int size(){
        return snake.size();
    }
}

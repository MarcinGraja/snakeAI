package snake;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    Board board;
    private Snake snake;
    private boolean inFailstate = false;
    private Point head;
    int sizeX;
    int sizeY;
    private int score = 0;
    private int startingMoves = 100;
    private int movesLeft;
    private int lifetime;
    Random randomGenerator;
    public Game(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        reset();
    }



    public void step(Direction direction){

        if (inFailstate) {
            throw new RuntimeException("in fail state; shouldn't be called");
        }

        Point newHead = new Point(snake.getLast().add(direction.getVector()));
        if (!board.isInBounds(newHead)
            || (snake.size() > 1 && newHead.equals(snake.get(snake.size()-2)))  //snek going back
            || board.getPoint(newHead) < 0){    //snek biting itself
            inFailstate = true;
        }else if(board.getPoint(newHead) == 1){
            score++;
            snake.add(newHead);
            addFood();
            movesLeft += 20;
        }else {
            snake.add(newHead);
            snake.remove(0);
            movesLeft--;
        }
        if (movesLeft <= 0){
            inFailstate = true;
        }
        this.head = newHead;
        lifetime++;
    }

    public void addStartingMoves(int toAdd){
        startingMoves += toAdd;
    }
    public boolean isInFailstate() {
        return inFailstate;
    }
    public void printState(){
        int[][] boardToPrint = board.getBoard();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < boardToPrint.length; i++){
            for (int j = 0; j < boardToPrint[0].length; j++){
                if (j == head.x && i == head.y) out.append("\u25BD");   //triangle
                else if (boardToPrint[j][i] == -1) out.append("\u25A1");// square
                else out.append(boardToPrint[j][i] > 0? "1" : "-");
                out.append("\t");
            }
            out.append("\n");
        }
        out.append("moves: ").append(movesLeft);
        System.out.println(out);
    }

    private void addFood(){
        ArrayList<Point> viablePoints = new ArrayList<>();
        for (int i = 0; i < sizeX; i++){
            for (int j = 0; j < sizeY; j++){
                if (board.getPoint(i,j) == 0){
                    viablePoints.add(new Point(i,j));
                }
            }
        }

        board.changePoint(viablePoints.get(randomGenerator.nextInt(viablePoints.size())), 1);
    }

    public double getScore() {
        return lifetime/10.0 * Math.pow(2, getLength()) + Math.pow(2, getLength());
    }
    public int getLength(){
        return snake.size();
    }
    public int getStartingMoves() {
        return startingMoves;
    }
    public int getMovesLeft(){
        return movesLeft;
    }
    public void reset(){
        randomGenerator = new Random(0);
        score = 0;
        lifetime = 0;
        board = new Board(sizeX, sizeY);
        snake = new Snake(board);
        int x = sizeX/2;
        int y = sizeY/2;
        snake.add(new Point(x,y));
        head = new Point(x,y);
        movesLeft = startingMoves;
        inFailstate = false;
        addFood();
    }
    public double[] lookAround(){
        Point [] directions = {new Point(1,0), new Point(1,1), new Point(0,1),
                new Point(-1,0), new Point(-1,-1), new Point(0,-1),
                new Point(1,-1), new Point(-1,1)};
        Point currentPosition = head;

        double[] out = new double[24];
        int counter = 0;
        for (Point direction: directions){
            currentPosition = currentPosition.add(direction);
            int distance = 1;

            boolean foodFound = false;
            boolean tailFound = false;
            while (board.isInBounds(currentPosition)){
                if(!foodFound && board.getPoint(currentPosition) == 1){
                    foodFound = true;
                    out[counter * 3] = 1;   //food is in this direction
                }
                if (!tailFound && board.getPoint(currentPosition) == -1){
                    tailFound = true;
                    out[counter * 3 + 1] = 1.0/distance;    //tail is that far away
                }
                currentPosition = currentPosition.add(direction);
                distance++;
            }
            out[counter * 3 + 2] = 1.0/distance;    //wall is that far away
            counter++;
        }
        return out;
    }
}

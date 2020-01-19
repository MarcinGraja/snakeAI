package AI;

import snake.Direction;
import snake.Game;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Trainer {
    static class Touple{
        NeuralNetwork snakeBrain;
        double score;
        int moves;
        int length;

        public Touple(NeuralNetwork snakeBrain, double score, int moves) {
            this.snakeBrain = snakeBrain;
            this.score = score;
            this.moves = moves;
        }
    }

    Random random = new Random();
    int snakeCount;
    int[] gameDimensions;
    ArrayList<Touple> snakes;
    Game game;
    public void createSnakes(int[] networkSize){
        for (int i = 0; i < snakeCount; i++){
            snakes.add(new Touple(new NeuralNetwork(networkSize), 0, 0));
        }
    }
    public Trainer(int[] gameDimensions, int[] networkSize, int snakeCount) {
        this.snakeCount = snakeCount;
        this.gameDimensions = gameDimensions;
        snakes = new ArrayList<>((int)(snakeCount*1.1));
        createSnakes(networkSize);
        this.game = new Game(gameDimensions[0], gameDimensions[1]);
    }
    public void train(){
        int generation = 0;
        Direction[] cachedEnum = Direction.values();
        PriorityQueue<Touple> topSnakes = new PriorityQueue<>((o1, o2) -> {
            if (o1.score < o2.score){
                return 1;
            }else if (o1.score > o2.score){
                return -1;
            }else return 0;
        });
        while(true) {
            if (generation % 1000 == 0 && generation != 0){
                game.addStartingMoves(1);
            }
            double topScore = 0;
            int topMoves = 0;
            int topLength = 0;
            double averageMoves = 0;
            double averageLength = 0;
            double averageScore = 0;
            NeuralNetwork bestSnake = snakes.get(0).snakeBrain;
            for (Touple snake : snakes) {
                this.game.reset();
                int moves = 0;
                while (!game.isInFailstate()) {
                    //game.printState();
                    moves++;
                    Matrix m = snake.snakeBrain.run(game.lookAround());
                    int direction = m.getMaxIndex()[1];
                    game.step(cachedEnum[direction]);
                }
                snake.score = game.getScore();
                snake.moves = moves;
                snake.length = game.getLength();
                if (topScore < snake.score){
                    bestSnake = snake.snakeBrain;
                    topScore = snake.score;
                }
                if (topMoves < snake.moves) {
                    topMoves = snake.moves;
                }
                if (topLength < game.getLength()) topLength = game.getLength();
                averageMoves += moves;
                averageLength += game.getLength();
                averageScore += game.getScore();
                if (topSnakes.size() < snakeCount / 10.0){
                    topSnakes.add(snake);
                }else if (topSnakes.size() > 0 && snake.score > topSnakes.peek().score){
                    topSnakes.poll();
                    topSnakes.add(snake);
                }
            }
            game.reset();
            while (!game.isInFailstate() && generation % 250 == 0) {

                Matrix m = bestSnake.run(game.lookAround());
                int direction = m.getMaxIndex()[1];
                game.step(cachedEnum[direction]);
            }
            if (generation % 250 == 0){
                game.printState();
            }
            double topSnakesTopScore = 0;
            int topSnakesTopLength = 0;
            double topSnakesAverageMoves = 0;
            double topSnakesAverageLength = 0;
            double topSnakesAverageScore = 0;
            for (Touple snake : topSnakes){
                topSnakesTopScore = Math.max(topSnakesTopScore, snake.score);
                topSnakesTopLength = Math.max(topSnakesTopLength, snake.length);
                topSnakesAverageMoves += snake.moves;
                topSnakesAverageLength += snake.length;
                topSnakesAverageScore += snake.score;

            }

            topSnakesAverageScore /= topSnakes.size();
            topSnakesAverageLength /= topSnakes.size();
            topSnakesAverageMoves /= topSnakes.size();

            //ArrayList<Touple> newSnakes = addSnakesV1();
            ArrayList<Touple> newSnakes = new ArrayList<>(snakeCount);
            for (int i = 0; i < snakeCount; i++){
                newSnakes.add(new Touple(selectParent(averageScore).snakeBrain.merge(selectParent(averageScore).snakeBrain), 0,0 ));
            }
            averageLength /= snakeCount;
            averageMoves /= snakeCount;
            averageScore /= snakeCount;



            snakes.clear();
            snakes.addAll(newSnakes);
            snakes.addAll(topSnakes);

            if(generation%250== 0) {
                System.out.println("\n\nGeneration: " + generation + " top score: " + topScore
                        //+ " top moves: " + topMoves
                        //+ " starting moves: " + game.getStartingMoves()
                        + " top length: " + topLength
                        + " average moves: " + averageMoves
                        + " average length: " + averageLength
                        + " average score: " + averageScore);

                System.out.println("Top snakes:\t" + " top score: " + topSnakesTopScore
                        + " top length: " + topSnakesTopLength
                        + " average moves: " + topSnakesAverageMoves
                        + " average length: " + topSnakesAverageLength
                        + " average score: " + topSnakesAverageScore);
            }
            generation++;
        }
    }
    private ArrayList<Touple> addSnakesV1(){
        ArrayList<Touple> snakeRoulette= new ArrayList<>(snakes.size());

        for (Touple snake : snakes){
            for (int i = 0; i < snake.score + 1; i++){
                snakeRoulette.add(snake);
            }
        }
        ArrayList<Touple> newSnakes = new ArrayList<>(snakeCount);
        for (int i = 0; i < snakeCount; i++){
            int r1 = random.nextInt(snakeRoulette.size()), r2 = random.nextInt(snakeRoulette.size());
            NeuralNetwork p1 = snakeRoulette.get(r1).snakeBrain;
            NeuralNetwork p2 = snakeRoulette.get(r2).snakeBrain;
            newSnakes.add(new Touple(p1.merge(p2), 0, 0));
        }
        return newSnakes;
    }
    private Touple selectParent(double totalScore){
        double r = ThreadLocalRandom.current().nextDouble(0, totalScore);
        double sum = 0;
        for (Touple snake : snakes) {
            sum += snake.score;
            if (sum > r) {
                return snake;
            }
        }
        return snakes.get(0);
    }
}

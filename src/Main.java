import AI.NeuralNetwork;
import AI.Trainer;
import snake.Direction;
import snake.Game;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        int[] gameDimensions = new int[]{10,10};
        //24 is the size of the array given by Game.lookAround()
        int[] networkSize = {24,18,4};
        Trainer trainer = new Trainer(gameDimensions, networkSize, 100);
        //manualPlay(gameDimensions);
        trainer.train();
    }
    public static void manualPlay(int[] gameDimensions){
        Game game = new Game(gameDimensions[0], gameDimensions[1]);
        while(true) {
            game.printState();
            Scanner scanner = new Scanner(System.in);
            String c;
            Direction direction = Direction.UP;
            do {
                c = scanner.next();
                switch (c) {
                    case "w":
                        direction = Direction.UP;
                        break;
                    case "d":
                        direction = Direction.RIGHT;
                        break;
                    case "s":
                        direction = Direction.Down;
                        break;
                    case "a":
                        direction = Direction.LEFT;
                        break;
                }
            } while (!("wsad".contains(c) && c.length() == 1));
            game.step(direction);
            if (game.isInFailstate()){
                System.out.println("new game\n");
                game = new Game(gameDimensions[0], gameDimensions[1]);
            }
        }
    }
}

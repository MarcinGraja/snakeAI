package AI;

import java.util.ArrayList;
public class NeuralNetwork {
    private ArrayList<Matrix> weights = new ArrayList<>();
    private int mutationsPer1000 = 100;

    public NeuralNetwork(int[] size){
        for (int i = 0; i < size.length-1; i++){
            Matrix weight = new Matrix(size[i]+1, size[i+1], true);
            weights.add(weight);
            //weight.printSize();
        }
    }
    private NeuralNetwork() {}
    Matrix run(double[] input){

        Matrix result = new Matrix(input);
        result = result.addBias();
        for(int i = 0; i < weights.size(); i++){
            Matrix weight = weights.get(i);
            result = result.product(weight);
            if (i != weights.size()-1){
                result = result.activate();
                result = result.addBias();
            }
            //result.printSize();
//            System.out.println(result.rowSize() + "x" + result.columnSize());
        }
        //result.printSize();
        return result;
    }
    public NeuralNetwork merge(NeuralNetwork other){
        if (this.weights.size() != other.weights.size()){
            System.err.println("neural networks have different amount of weights matrices m8");
            return null;
        }
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        for (int i = 0; i < this.weights.size(); i++){
            neuralNetwork.weights.add(this.weights.get(i).add(other.weights.get(i)).divide(2).mutate(mutationsPer1000));

        }
        return neuralNetwork;
    }
}

package AI;

import org.w3c.dom.CDATASection;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Matrix {
    static Random random = new Random();
    private double[][] data;

    public int rowSize(){
        return data.length;
    }
    public int columnSize(){
        return data[0].length;
    }

    public Matrix(double[][] data) {
        this.data = data;
    }
    public Matrix(double[] data){
        this.data = new double[1][];
        this.data[0] = data;
    }
    public Matrix(int rowSize, int columnSize, boolean randomize){
        data = new double[rowSize][columnSize];
        if (randomize){
            for(int i = 0; i < rowSize; i++) {
                for (int j = 0; j < columnSize; j++) {
                    data[i][j] = (Math.random() - 0.5) * 2;
                }
            }
            this.checkZeros();
        }

    }
    public Matrix add(Matrix other){
        if (this.rowSize() != other.rowSize() || this.columnSize() != other.columnSize()){
            System.err.println("bad matrix m8");
            return null;
        }
        Matrix matrix = new Matrix(this.rowSize(), this.columnSize(),false);
        for (int i = 0; i < this.rowSize(); i++){
            for (int j = 0; j < this.columnSize(); j++){
                try {
                    matrix.data[i][j] = this.data[i][j] + other.data[i][j];
                }catch (IndexOutOfBoundsException e){
                    matrix.printSize();
                    this.printSize();
                    other.printSize();
                    throw e;
                }
            }
        }
        this.checkZeros();
        other.checkZeros();
        matrix.checkZeros();
        return matrix;
    }
    public Matrix divide(double d){
        Matrix matrix = new Matrix(this.data);
        for (int i = 0; i < rowSize(); i++){
            for (int j = 0; j < this.columnSize(); j++){
                matrix.data[i][j] /= d;
            }
        }
        this.checkZeros();
        matrix.checkZeros();
        return matrix;
    }
    public Matrix product(Matrix other){
        if (this.columnSize() != other.rowSize()){
            System.err.println("bad matrix m8 2");
            throw new IllegalArgumentException("multiplication: \n" + this.rowSize() + "x" + this.columnSize()
                    + "\n" + other.rowSize() + "x" + other.columnSize());
        }
        int rowSize = this.rowSize();
        int columnSize = other.columnSize();
        double[][] out = new double[rowSize][columnSize];
        for (int row = 0; row < rowSize; row++){
            for (int column = 0; column < columnSize; column++){
                for (int k = 0; k < this.columnSize(); k++){
                    try{
                        out[row][column] += this.data[row][k] * other.data[k][column];
                    }catch (IndexOutOfBoundsException e){
                        String s = rowSize + "x" + columnSize + "\n" +
                                this.rowSize() + "x" + this.columnSize() + "\n" +
                                other.rowSize() + "x" + other.columnSize();
                        System.err.println(s);
                        throw e;
                    }
                }
            }
        }

        Matrix matrix = new Matrix(out);
        this.checkZeros();
        other.checkZeros();
        matrix.checkZeros();
        return matrix;
    }
    public static Matrix flatten(Matrix m){

        double[] out = new double[m.data.length * m.data[0].length];
        for (int i = 0; i < m.data.length; i++){
            System.arraycopy(m.data[i], 0, out, i * m.data.length , m.data[0].length);
        }
        return new Matrix(out);
    }
    public Matrix mutate(int mutationPer1000){
        Matrix m = new Matrix(this.data);
        for (int i = 0; i < this.data.length; i++){
            for (int j = 0; j < this.data[i].length; j++){
                try {
                    if (random.nextInt(1000) < mutationPer1000)
                    m.data[i][j] += random.nextGaussian();
                    if (m.data[i][j] < -1) m.data[i][j] = -1;
                    if (m.data[i][j] > 1) m.data[i][j] = 1;

                }catch (IndexOutOfBoundsException e){
                    m.printSize();
                    System.out.println("i:" + i + " j: " + j);
                    throw e;
                }
            }
        }
        this.checkZeros();
        m.checkZeros();
        return m;
    }
    public Matrix activate(){
        Matrix out = new Matrix(this.rowSize(), this.columnSize(), false);
        for (int i = 0; i < rowSize(); i++){
            for (int j = 0; j < columnSize(); j++){
                out.data[i][j] = this.data[i][j];
                if (out.data[i][j] < 0) out.data[i][j] = 0;
            }
        }
        this.checkZeros();
        out.checkZeros();
        return out;
    }
    public Matrix addBias(){
        Matrix out = new Matrix(this.rowSize(), this.columnSize() + 1, false);
        for (int i = 0; i < this.columnSize(); i++){
            out.data[0][i] = this.data[0][i];
        }
        out.data[0][this.columnSize()] = 1;
        this.checkZeros();
        out.checkZeros();
        return out;
    }
    public int[] getMaxIndex(){
        double max = data[0][0];
        int[] maxIndex = {0,0};
        for (int i = 0; i < this.rowSize(); i++) {
            for (int j = 0; j < this.columnSize(); j++) {
                if (max < data[i][j]){
                    max = data[i][j];
                    maxIndex = new int[]{i, j};
                }
            }

        }
        return maxIndex;
    }
    public void printSize(){
        System.out.println(this.rowSize() + "x" + this.columnSize());
    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < rowSize(); i++){
            for (int j = 0; j < columnSize(); j++){
                s.append(this.data[i][j]).append("\t");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public double[][] getData() {
        return data;
    }
    private void checkZeros(){
        if (this.columnSize() == 25 || this.columnSize() == 24) return;
        int count = 0;
        for (int i = 0; i < rowSize(); i++){
            for (int j = 0; j < columnSize(); j++){
                if (this.data[i][j] == 0) count++;
            }
        }
        if (count > rowSize() * columnSize() *  0.9 && false){
            System.err.println(this);
            printSize();
            throw new RuntimeException("some ting wong");
        }
    }
}

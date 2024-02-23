package component;

import activation.Activation;

public class Neuron {
    int position;
    public double[] weights;
    double[] weightGradients;
    double bias;
    double biasGradient;
    public double[] savedRawInput;
    public double savedActivatedInput;
    public Activation activationFunction;
    public Neuron[] inNeurons;
    public Neuron[] outNeurons;
    public double backPropValue;

    public Neuron(int inSize, int position, Activation activationFunction){
        this.position = position;
        this.weights = initRandomParams(inSize);
        this.weightGradients = new double[inSize];
        this.bias = Neuron.randomValue();
        this.biasGradient = 0;
        this.savedActivatedInput = 0;
        this.activationFunction = activationFunction;
        this.backPropValue = 0;
    }

    public double forward(double[] input){
        this.savedRawInput = input;
        double weightedInput = this.calculateWeightedInput(input);
        double activatedInput = this.calculateActivatedInput(weightedInput);
        this.savedActivatedInput = activatedInput;
        return activatedInput;
    }

    public void updateBackProp(){
        double backProp = 0;
        for(int i=0;i<this.outNeurons.length;i++){
            Neuron outNeuron = this.outNeurons[i];
            backProp += outNeuron.weights[this.position]*outNeuron.backPropValue;
        }
        backProp *= this.activationFunction.gradient(this.savedActivatedInput);
        this.backPropValue = backProp;
    }

    public void updateGradients(){
        for(int i=0;i<this.weightGradients.length;i++){
            if(this.inNeurons == null){
                this.weightGradients[i] += this.savedRawInput[i]*this.backPropValue;
                continue;
            }
            Neuron inNeuron = this.inNeurons[i];
            this.weightGradients[i] += inNeuron.savedActivatedInput*this.backPropValue;
        }
        this.biasGradient += this.backPropValue;
    }

    public void applyGradients(double learningRate){
        for(int i=0;i<this.weights.length;i++){
            this.weights[i] -= learningRate*this.weightGradients[i];
        }
        this.bias -= learningRate*this.biasGradient;
    }

    public void clearGradients(){
        for(int i=0;i<this.weightGradients.length;i++){
            this.weightGradients[i] = 0;
        }
        this.biasGradient = 0;
    }

    double calculateWeightedInput(double[] input){
        double weightedInput = this.bias;
        for(int i=0;i<this.weights.length;i++){
            weightedInput += this.weights[i]*input[i];
        }
        return weightedInput;
    }

    double calculateActivatedInput(double weightedInput){
        return this.activationFunction.activate(weightedInput);
    }

    private static double randomValue(){
        return Math.random()*2 - 1;
    }

    private static double[] initRandomParams(int size){
        double[] params = new double[size];
        for(int i=0;i<size;i++){
            params[i] = Neuron.randomValue();
        }
        return params;
    }
}

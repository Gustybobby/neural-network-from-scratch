# Neural Network from Scratch in Java

In this project, I will build a neural network from scratch using an OOP language.

## Neural Network

### Nueron

Neuron is the smallest component of the neural network. It is responsible for calculating the value and updating the weights between itself and other neurons.

#### Linear Combination

```java
double calculateWeightedInput(double[] input) {
    double weightedInput = this.bias;
    for (int i = 0; i < this.weights.length; i++) {
        weightedInput += this.weights[i] * input[i];
    }
    return weightedInput;
}
```

The neuron calculates the weighted input by linear combinations of the input.

#### Activation Function

```java
double calculateActivatedInput(double weightedInput) {
    return this.activationFunction.activate(weightedInput);
}
```

After calculating the weighted input, the activated input is calculated using an activation function.

#### Feed Forward

```java
public double forward(double[] input) {
    this.savedRawInput = input;
    double weightedInput = this.calculateWeightedInput(input);
    double activatedInput = this.calculateActivatedInput(weightedInput);
    this.savedActivatedInput = activatedInput;
    return activatedInput;
}
```

Method `forward(double[]): double` is responsible for saving the inputs (for later uses) and calculating the activated input.

#### Backpropagation

```java
public void updateBackProp() {
    double backProp = 0;
    for (int i = 0; i < this.outNeurons.length; i++) {
        Neuron outNeuron = this.outNeurons[i];
        backProp += outNeuron.weights[this.position] * outNeuron.backPropValue;
    }
    backProp *= this.activationFunction.gradient(this.savedActivatedInput);
    this.backPropValue = backProp;
}
```

Back propagates value is calculated and saved to the neuron.

```java
public void updateGradients() {
    for (int i = 0; i < this.weightGradients.length; i++) {
        if (this.inNeurons == null) {
            this.weightGradients[i] += this.savedRawInput[i] * this.backPropValue;
            continue;
        }
        Neuron inNeuron = this.inNeurons[i];
        this.weightGradients[i] += inNeuron.savedActivatedInput * this.backPropValue;
    }
    this.biasGradient += this.backPropValue;
}
```

The gradients are updated using the back propagates value

#### Gradient Descent

```java
public void applyGradients(double learningRate) {
    for (int i = 0; i < this.weights.length; i++) {
        this.weights[i] -= learningRate * this.weightGradients[i];
    }
    this.bias -= learningRate * this.biasGradient;
}

public void clearGradients() {
    for (int i = 0; i < this.weightGradients.length; i++) {
        this.weightGradients[i] = 0;
    }
    this.biasGradient = 0;
}
```

Finally, after updating the gradients, the gradients are applied (either right away or later).
The gradients are clear afterward, readying for the next backpropagation.

### Layer

A layer is basically a collection of neurons that are linked to all neurons in the neighboring layers.
It just iterates the processes of neurons in their own layers, as explained in the last section.

#### Output Layer

The output layer is special as the back propagates values are calculated based on the cost function gradient.

```java
public void updateOutputLayerBackProps(double[] expectedOutput) {
    Layer outputLayer = this.layers[this.layers.length - 1];
    for (int i = 0; i < outputLayer.size; i++) {
        Neuron neuron = outputLayer.neurons[i];
        neuron.backPropValue = this.costFunction.calculateGradient(neuron.savedActivatedInput, expectedOutput[i]);
        neuron.backPropValue *= neuron.activationFunction.gradient(neuron.savedActivatedInput);
    }
}
```

### Training

Finally, we can train the network by inputing the expected output

```java
public double[] train(double[] input, double[] expectedOutput) {
    double[] output = this.forward(input);
    this.backward(expectedOutput);
    return output;
}
```

#### Batch Training

Batch Training use many input, output pairs to train the network. The average loss is calculated for network evaluation.

```java
public double batchTrain(double[][] inputs, double[][] expectedOutputs) {
    double averageLoss = 0;
    for (int i = 0; i < inputs.length; i++) {
        double[] output = this.train(inputs[i], expectedOutputs[i]);
        averageLoss += this.loss(output, expectedOutputs[i]);
    }
    return averageLoss / (double) inputs.length;
}
```

#### Performing Gradient Descent

Gradient descent will be performed seperately. We can use different configurations of descent intervals to experiment with each cases.

## Experiments

Below is a list of use cases and environments experimented

- [Tic Tac Toe](/ai/game/tic_tac_toe/README.md)

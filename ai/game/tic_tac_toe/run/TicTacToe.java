package game.tic_tac_toe.run;

import java.util.Scanner;

import easy_math.FileIO;
import game.tic_tac_toe.TTTEnv;
import game.tic_tac_toe.TTTState;
import game.tic_tac_toe.player.TTTHuman;
import game.tic_tac_toe.player.TTTMCTS;
import game.tic_tac_toe.player.TTTNeuralNet;
import game.tic_tac_toe.player.TTTOneStep;
import game.tic_tac_toe.player.TTTPlayer;
import neural_network.NeuralNetwork;
import neural_network.activation.Tanh;
import neural_network.cost.SquaredError;
import neural_network.layer.Layer;

public class TicTacToe {

    public static void oneStepVsNeuralNetwork(int iter, int descent, int evaluate, int layerSize, double greed,
            double alpha, double discount, boolean important) {
        NeuralNetwork nn = initNeuralNetwork(layerSize, alpha);
        TTTState tttState = TTTState.initialState(3, 3, 3);
        TTTPlayer agentX = new TTTOneStep(1, null);
        TTTPlayer agentO = new TTTNeuralNet(-1, nn, greed, agentX);
        TTTEnv env = new TTTEnv(tttState, agentX, agentO);
        agentX.opponent = agentO;
        trainNeuralNetwork(env, nn, iter, descent, evaluate, discount, important);
        play(env);
    }

    public static void mctsVsNeuralNetwork(int iter, int descent, int evaluate, int layerSize, double greed,
            double alpha, double discount, int searchIter, boolean important) {
        NeuralNetwork nn = initNeuralNetwork(layerSize, alpha);
        TTTState tttState = TTTState.initialState(3, 3, 3);
        TTTPlayer agentX = new TTTMCTS(1, searchIter, null);
        TTTPlayer agentO = new TTTNeuralNet(-1, nn, greed, agentX);
        TTTEnv env = new TTTEnv(tttState, agentX, agentO);
        agentX.opponent = agentO;
        trainNeuralNetwork(env, nn, iter, descent, evaluate, discount, important);
        play(env);
    }

    public static void mctsVsHuman(int searchIter) {
        TTTState tttState = TTTState.initialState(3, 3, 3);
        TTTPlayer agentX = new TTTOneStep(1, null);// new TTTMCTS(1, 100, null);
        TTTPlayer agentO = new TTTMCTS(-1, searchIter, agentX);
        TTTEnv env = new TTTEnv(tttState, agentX, agentO);
        agentX.opponent = agentO;
        play(env);
    }

    public static void trainNeuralNetwork(TTTEnv env, NeuralNetwork nn, int iter, int descent, int evaluate,
            double discount, boolean important) {
        double[] scores = new double[iter / evaluate];
        for (int i = 1; i <= iter; i++) {
            env.process();
            nn.batchTrain(env.getTrainingStates(1, important), env.getTrainingValues(1, discount, important));
            if (i % descent == 0) {
                nn.gradientDescent();
            }
            if (i % evaluate == 0) {
                scores[i / evaluate - 1] = env.evaluateNeuralNetwork();
            }
        }
        String[] keywords = {
                "eval",
                "it" + Integer.toString(iter),
                "gd" + Integer.toString(descent),
                "df" + Double.toString(discount),
                "im" + Boolean.toString(important),
                Integer.toString(nn.getLayersLength()),
                Integer.toString(nn.getLayerSize(0)),
                "gr" + Double.toString(((TTTNeuralNet) (env.playerO)).greed),
                "lr" + Double.toString(nn.learningRate),
                Long.toString(ProcessHandle.current().pid()),
        };
        String path = "C://Users/napat/Documents/Work/siit/project/ai/game/tic_tac_toe/data";
        FileIO.recordToCSV(path, String.join("_", keywords) + ".csv", scores);
    }

    public static void play(TTTEnv env) {
        Scanner scanner = new Scanner(System.in);
        env.playerX = new TTTHuman(1, scanner, env.playerO);
        env.play();
        scanner.close();
    }

    public static NeuralNetwork initNeuralNetwork(int layerSize, double alpha) {
        Layer[] layers = new Layer[] {
                new Layer(9, layerSize, new Tanh()),
                new Layer(layerSize, layerSize, new Tanh()),
                new Layer(layerSize, layerSize, new Tanh()),
                new Layer(layerSize, layerSize, new Tanh()),
                new Layer(layerSize, 1, new Tanh())
        };
        return new NeuralNetwork(layers, new SquaredError(), alpha);
    }

    public static void main(String[] args) {
        // oneStepVsNeuralNetwork(100000, 10, 200, 32, 0, 0.001, 0.95, true);
        mctsVsNeuralNetwork(500000, 10, 1000, 32, 0.1, 0.001, 0.8, 200, false);
        // mctsVsHuman(800);
    }
}

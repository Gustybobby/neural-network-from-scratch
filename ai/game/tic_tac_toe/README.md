# Tic Tac Toe

Here, I implemented different techniques for solving the tic-tac-toe game. Tic Tac Toe is a classic board game played on
a 3x3 board where X and O alternatively take turns drawing their symbol on the cell, and the first person to connect their symbol
3 in a row (either horizontally, vertically, or diagonally) wins.\
\
What makes Tic Tac Toe interesting for reinforcement learning and neural networks is that its state and action are not very complex but still large enough to be studied. So think of it as an introduction to reinforcement learning; the optimal solution is known, and it is easy to analyze what is going on.

## Policies

Policies describe the behavior of agents navigating the environment (in this case, players playing the game). Different policies behave differently and, interestingly, lead to different effects on the neural network.

### One-Step-Look-Ahead

This policy searches for a move that immediately results in a win; otherwise, the moves are random.

### Monte Carlo Search Tree (MCTS)

Monte Carlo Search Tree is a search algorithm based on the method of the same name. It involves simulations using roll-out policies to give each possible action a score averaged by the result. It balances the exploration and exploitation of the search and navigates the tree to find the best move. An advantage of using MCTS over other search algorithms like Minimax and Alpha-Beta pruning is the perks of not needing an evaluation function as the evaluation is based on the rollout policies (often playing random moves).

### Neural Network

This is the policy we are trying to train to play the game optimally.

## Experiments

All of the experiments use neural network with 4+1 layers, hidden layers having 32 neurons each and Tanh activation function with Squared Error Loss Function.

### One Step Look Ahead vs Neural Network

One Step Look Ahead Policy behavior are probabilistic for states with no winning move and deterministic for states with one.
We expect the network to be able to exploit these behaviors and also defends against one move threats.

#### Learning Rate and Episodes per Descent

In this section, we aim to describe the effects of learning rate and episodes per descent on the performance of the network.\
I ran tests with 6 configurations listed below

1. ep/descent: 100, learning rate: 1e-4
2. ep/descent: 100, learning rate: 0.001
3. ep/descent: 100, learning rate: 0.01
4. ep/descent: 10 , learning rate: 1e-4
5. ep/descent: 10 , learning rate: 0.001
6. ep/descent: 10 , learning rate: 0.01

All the tests simulates 100,000 episodes with 0 greed (no random actions), discount factor of 0.95. The neural network weights and biases are initialized to random values between -1 and 1. I ran 5 trials for each configuration to account for instability.\

According to the results, a learning rate of 0.01 results in instability for all configurations, with configuration 2 having the worst performance as it never converges at all. On the other hand, convergences with a learning rate of 0.001 and 0.0001 are more stable and robust, with configurations with 10 ep/descent being more stable. Although the learning rate of 0.0001 is converging, it seems to be converging so slowly that even after 100,000 episodes, it still hasn't reached its optimum. Based on these results, we can conclude that lower learning rates and lower ep/descent rates result in the learning convergences being more robust and stable. This is due to the fact that a learning rate and ep/descent rate that are too high will result in the gradient descent missing the optimum. For values of learning rate that are too small, it will take too long to converge.

### MCTS vs Neural Network

MCTS performance is heavily dependent on the number of search iterations used during each round. As Tic Tac Toe is a solved game and is a draw with best play, with enough search iterations, MCTS is expected to play perfectly. Subsequently, the neural network is also expected to learn from this behavior and not lose.

#### Epsilon-Greedy and Importance Sampling

In this section, we study the effect of greed (the episilon-greedy constant) on the convergence of the neural network against MCTS. MCTS is very different from the one-step look-ahead policy since, most of the time, one-step actions are probabilistic except for the winning move. We don't really need to consider the exploration aspect since the policy takes care of that for us. MCTS, however, behaves almost exactly the same way with every move since it will try to play the best move. This takes the exploration aspect out of the environment, so we have to incorporate it into our own learning experiences. As the moves are random for some epsilon-greedy value,the neural network is expected to converge to the expected value, not the optimal value. To account for this randomness, we introduce importance sampling so that the target policy can be learned while using another policy for generating the episodes. I ran tests with the four configurations below.

1. greed: 0.05, imp_samp = true
2. greed: 0.05, imp_samp = false
3. greed: 0.1 , imp_samp = true
4. greed: 0.1 , imp_samp = false

All the tests simulate 100,000 episodes, a 0.001 learning rate, 10 episodes per child, and a discount factor of 0.8. MCTS uses search iterations of 200 per move. The neural network weights and biases are initialized to random values between -1 and 1. I ran 3 trials for each configuration to account for instability.\

package mcts.mcts_model.classical;

import mcts.Node;

public class ClassicalMCTS {
    public Node rootNode;

    public void iterate(int iter) {
        for (int i = 0; i < iter; i++) {
            this.step();
        }
    }

    public void step() {
        if (this.rootNode.isTerminal()) {
            return;
        }
        Node currentNode = this.rootNode;
        // selection
        while (currentNode.hasChildren()) {
            currentNode = currentNode.selectNode();
        }
        // expansion
        if (!currentNode.hasChildren() && !currentNode.isTerminal()) {
            currentNode.expandNode();
            currentNode = currentNode.selectNode();
        }
        double childValue = 0;
        // simulation & backpropagation
        while (currentNode != null) {
            childValue = currentNode.update(childValue);
            currentNode = currentNode.parent;
        }
    }
}

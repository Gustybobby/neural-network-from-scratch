import model.Model;
import policy.NeuralNetPolicy;
import policy.OneStepPolicy;
import policy.Policy;

public class Main {
    public static void main(String[] args){
        Policy policy1 = new NeuralNetPolicy(0.01, 0.01);
        Policy policy2 = new OneStepPolicy();
        Model model = new Model(3, 3, policy2, policy1);
        model.train(100000, 10000, 1000);
        System.out.println("Evaluation over 100000 Sample Games");
        model.compete(100000);
        System.out.println("-------------------------------------------------");
    }
}

import java.util.ArrayList;

public abstract class Solver {

	ArrayList<Bag> Bags;
	ArrayList<Gold> Golds;
	double evaluation;

	protected Solver(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		Bags = aBags;
		Golds = aGolds;
		evaluation = 0;
	}

	abstract Algorithm selectAlgorithm();

}

abstract class Algorithm {

	ArrayList<Bag> Bags;
	ArrayList<Gold> Golds;
	double evaluation;
	int[] solution;
	double[] power ={2.0,1.8,1.6,1.4,1.2};

	protected Algorithm(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		Bags = aBags;
		Golds = aGolds;
		evaluation = 1000000;
		solution = new int[Golds.size()];
	}

	abstract void run();

	protected double getEval(int[] assignment) {

		double eval = 0;
		for (int k = 0; k < assignment.length; k++) {
			eval += power[assignment[k]] * Golds.get(k).getValue();
		}
		return eval;
	}

	protected boolean CheckConstraint(int[] assignment) {
		// bagに詰め込んだ重みを記録する配列　weights
		double[] weights = new double[Bags.size()];

		for (int k = 0; k < assignment.length; k++) {
			weights[assignment[k]] += Golds.get(k).getWeight();

			// 更新したときに重みがbagのサイズを超えていたら false
			if (weights[assignment[k]] > Bags.get(assignment[k]).getSize()) {
				return false;
			}
		}
		return true;
	}

	protected void ShowUpdate() {
		System.out.println("-----Update the best solution------");
		System.out.println("Evaluation value: " + evaluation);
		for (int x = 0; x < solution.length; x++) {
			System.out.print("x[" + x + "]=" + solution[x] + "  ");
		}
		System.out.println();
		System.out.println();
	}
}

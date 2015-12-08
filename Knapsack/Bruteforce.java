import java.io.IOException;
import java.util.ArrayList;

class Bruteforce extends Solver {

	public Bruteforce(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		super(aBags, aGolds);
	}

	@Override
	Algorithm selectAlgorithm() {
		return new runBruteforce(this.Bags, this.Golds);
	}

}

class runBruteforce extends Algorithm {

	runBruteforce(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		super(aBags, aGolds);
	}

	@Override
	void run() {
		// 金塊ごとに、どの袋に入れるか[this.Bags.size()]　＋　袋に入れない[1]通りある。
		long numCandidates = (int) Math
				.pow(this.Bags.size()+1, this.Golds.size());
	
		//上記の全通りで組み合わせ assignment を作る。
		for (long i = numCandidates-1; i >= 0; i--) {
			
			// assignment[x] = y は金塊xを鞄yに詰め込む
			// ただし y=0のときは割り当てなし。
			int[] assignment = makeCombination(i);
			
			if(CheckConstraint(assignment)){
				double newEval = getEval(assignment);
				if(evaluation < newEval){
					evaluation = newEval;
					for(int x=0; x<assignment.length;x++){
						solution[x] = assignment[x];
					}
					ShowUpdate();
				}
			}
		}
	}


	private int[] makeCombination(long i) {
		int j = 0;
		int[] assignment = new int[this.Golds.size()];
		
		//10進数の値を(bag数+1)進数の値に変換して
		//assignmentを得る。
		while (i >= 0) {
			int quotient = (int) (i / (this.Bags.size()+1));
			int remainder = (int) (i % (this.Bags.size()+1));
			i = quotient;
			assignment[j] = remainder;
			j++;			
			
			//既にすべて割当済み。
			if (j == this.Golds.size()){
				break;
			}
			
			//最後の割当
			if (i<this.Bags.size()) {
				assignment[j] = (int)i;
				break;
			}
		}
		
		//上記で金塊の割当が行われなかったものは、assignment = 0にしておく
		int num0 = this.Golds.size() - j;
		for (int l = j; l < num0; l++) {
			assignment[l] = 0;
		}
	
		return assignment;
	}
}

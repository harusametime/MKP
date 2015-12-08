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
	
	abstract Algorithm selectAlgorithm ();
	
	
}

abstract class Algorithm {
	
	ArrayList<Bag> Bags;
	ArrayList<Gold> Golds;
	double evaluation;
	int[] solution;
	
	protected Algorithm(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		Bags = aBags;
		Golds = aGolds;
		evaluation = 0;
		solution = new int[Golds.size()];
	}
	abstract void run();
	
	protected double getEval(int[] assignment) {
		
		double eval = 0;
		for(int k=0; k<assignment.length;k++){
			//どの袋にも入っていない場合は無視
			if (assignment[k]==0){
				continue;
				
			//入っていたら価値を可算
			}else{
				eval += Golds.get(k).getValue();
			}
		}
		return eval;
	}
	
	protected boolean CheckConstraint(int[] assignment) {
		//bagに詰め込んだ重みを記録する配列　weights 
		double[] weights = new double[Bags.size()];
		
		for(int k=0; k<assignment.length;k++){
			//詰め込まない場合は weights を更新しない
			if(assignment[k]==0){
				continue;
			}
			//詰め込む場合は更新
			else{
				weights[assignment[k]-1] += Golds.get(k).getWeight();
				
				//更新したときに重みがbagのサイズを超えていたら false
				if(weights[assignment[k]-1] > Bags.get(assignment[k]-1).getSize()){
					return false;
				}
			}
		}
		return true;
	}
	
	protected void ShowUpdate(){
		System.out.println("-----Update the best solution------");
		System.out.println("Evaluation value: "+ evaluation);
		for (int x=0; x<solution.length;x++){
			System.out.print("x["+x+"]="+solution[x]+"  ");
		}
		System.out.println();
		System.out.println();
	}
}

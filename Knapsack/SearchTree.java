import java.util.Stack;


public class SearchTree extends Stack<Node>{

	
	
}

class Node {
	int gold;
	int bag;
	double lb;   //lower bound
	
	public Node(int i, int j, double k) {
		gold = i;
		bag = j;
		lb = k;
				
	
	}
	public Node() {
		// TODO Auto-generated constructor stub
	}
	
}

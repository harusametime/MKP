import java.io.IOException;
import java.util.ArrayList;

class BranchAndBound extends Solver {

	public BranchAndBound(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		super(aBags, aGolds);
	}

	@Override
	Algorithm selectAlgorithm() {
		return new runBranchAndBound(this.Bags, this.Golds);
	}

}

class runBranchAndBound extends Algorithm {

	protected runBranchAndBound(ArrayList<Bag> aBags, ArrayList<Gold> aGolds) {
		super(aBags, aGolds);
	}

	@Override
	void run() {

		double best_eval = 1000000000; // 暫定値
		SearchTree tree = new SearchTree();
		int max = Bags.size() - 1;
		double infeasible_lb = 1000000000;

		// 深さ優先探索
		// とりあえず金塊1から順にすべて割り当ててみる。
		for (int i = 0; i < Golds.size(); i++) {

			Node newnode = new Node(i, max, infeasible_lb);
			tree.add(newnode);

			LowerBound LB = new LowerBound();
			// 制約を満足していれば下界値を更新する。
			if (!LB.setMatrix(tree, Bags, Golds, power)) {
				double lb = LB.value();
				newnode = tree.pop();
				newnode.lb = lb;
				tree.push(newnode);
			} else {
				// 制約を満足していなければそのまま
				break;
			}
			if (newnode.lb > best_eval) {
				break;
			}
		}

		while (!tree.empty()) {

			// ノードによって分岐の処理を変える
			// 葉ノード　または　実行不可能解 または　下界値が暫定値より悪い　場合
			// これ以上探索できないため、現在のノード now を削除して次の分岐ノードを決める。
			if (tree.size() == Golds.size() || tree.peek().lb == infeasible_lb
					|| tree.peek().lb > best_eval) {

				// 現在のノード now を削除して次に分岐するノード next を決める
				Node now;
				Node next = new Node();

				// 分岐が完了していないところまでもどるbag!=0が条件)
				boolean complete = false;
				while ((now = tree.pop()).bag == 0) {

					if (tree.empty()) {
						// 戻って空になったら、全て探索済みの意味
						complete = true;
						break;
					}
				}
				if (complete) {
					break;
				} else {
					next.bag = now.bag - 1;
					next.gold = now.gold;
					next.lb = infeasible_lb;
				}

				tree.add(next);
				LowerBound LB = new LowerBound();

				// 制約を満足していれば下界値を更新する。
				if (!LB.setMatrix(tree, Bags, Golds, power)) {
					// 葉ノードなら評価値で更新
					double lb = infeasible_lb;
					if (tree.size() == Golds.size()) {
						lb = getEvaluation(tree);
						if (lb < best_eval) {
							best_eval = lb;
							evaluation = best_eval;
							solution = extractSolutionfromTree(tree);
							ShowUpdate();
						}
					} else {
						lb = LB.value();
					}
					next = tree.pop();
					next.lb = lb;
					tree.push(next);
				}
			}

			// 葉ノードでもなく実行可能である時は
			// 次の金塊を入れてみる。
			// 現在のノード now は削除せず次に分岐するノード next を決める
			else {
				Node now = tree.peek();

				// 次の分岐をつくるとき
				Node next = new Node();
				next.bag = max;
				next.gold = now.gold + 1;
				next.lb = infeasible_lb;
				tree.add(next);
				LowerBound LB = new LowerBound();

				// 制約を満足していれば下界値を更新する。
				if (!LB.setMatrix(tree, Bags, Golds, power)) {

					// 葉ノードなら評価値で更新
					double lb = infeasible_lb;
					if (tree.size() == Golds.size()) {
						lb = getEvaluation(tree);
						if (lb < best_eval) {
							best_eval = lb;
							evaluation = best_eval;
							solution = extractSolutionfromTree(tree);
							ShowUpdate();
						}
					} else {
						lb = LB.value();
					}
					next = tree.pop();
					next.lb = lb;
					tree.push(next);

				}
			}

		}
	}

	private double getEvaluation(SearchTree tree) {
		double evaluation = 0;

		for (int i = 0; i < tree.size(); i++) {
			evaluation += Golds.get(tree.get(i).gold).value
					* power[tree.get(i).bag];
		}
		return evaluation;
	}

	private int[] extractSolutionfromTree(SearchTree tree) {

		int[] sol = new int[solution.length];
		for (int i = 0; i < tree.size(); i++) {
			sol[tree.get(i).gold] = tree.get(i).bag;
		}
		return sol;
	}
}

class LowerBound {

	double[][] A;
	double[] c;
	double[] b;
	double constant;

	public boolean setMatrix(SearchTree tree, ArrayList<Bag> Bags,
			ArrayList<Gold> Golds, double[] power) {
		int vector_size = Bags.size() * Golds.size();
		A = new double[Bags.size() + Golds.size()][vector_size];
		c = new double[vector_size];
		b = new double[Bags.size() + Golds.size()];
		for (int j = 0; j < Bags.size(); j++) {
			b[j] = Bags.get(j).size;
		}
		for (int i = Bags.size(); i < (Bags.size() + Golds.size()); i++) {
			b[i] = 1;
		}

		constant = 0;
		for (int j = 0; j < Golds.size(); j++) {
			boolean flag = false;
			int x = 0;
			for (int k = 0; k < tree.size(); k++) {
				if (j == tree.get(k).gold) {
					flag = true;
					x = k;
					break;
				}
			}
			if(flag){
				b[tree.get(x).bag] -= Golds.get(tree.get(x).gold).weight;
				if (b[tree.get(x).bag ] < 0) {
					return true;
				}
				for (int i = 0; i < Bags.size(); i++) {
					if (i == tree.get(x).bag) {
							//A[i][i * Golds.size() + j] = 0;
							A[j + Bags.size()][i * Golds.size() + j] = 1;
							//c[i * Golds.size() + j] = 0;
						} else {
							//A[i][i * Golds.size() + j] = 0;
							//c[i * Golds.size() + j] = 0;
						}
					}
				constant += Golds.get(tree.get(x).gold).value*power[tree.get(x).bag];
				}else {
					for (int i = 0; i < Bags.size(); i++) {
						A[i][i * Golds.size() + j] = Golds.get(j).weight;
						c[i * Golds.size() + j] = -Golds.get(j).value * power[i];
					}
					for (int m = 0; m < Bags.size(); m++) {
						A[j + Bags.size()][m * Golds.size() + j] = 1;
					}
				}
			
		}
		return false;
	}

	public double value() {
		Simplex sm = new Simplex(A, b, c);
		return -sm.value() + constant;
	}
}

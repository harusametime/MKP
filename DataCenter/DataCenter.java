import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataCenter {

	public static void main(String[] args) {

		// 袋のサイズのリスト
		ArrayList<Bag> Bags = new ArrayList<Bag>();
		// 金塊のサイズと価値のリスト
		ArrayList<Gold> Golds = new ArrayList<Gold>();

		// 袋のデータの読み込み
		Bags = readFileBags("./bag3.txt");
		// 金塊のデータの読み込み
		Golds = readFileGolds("./gold3.txt");
		
		
		//処理開始時刻の記録
		long start = System.currentTimeMillis();
		
		//アルゴリズムの実行
		Solver sol = new Bruteforce(Bags, Golds);
		//Solver sol = new BranchAndBound(Bags, Golds);
		Algorithm alg = sol.selectAlgorithm();
		alg.run();

		//処理終了時刻の記録
		long end = System.currentTimeMillis();
		
		
		//最終結果の表示
		System.out.println();
		System.out.println();
		System.out.println("********************************************");
		System.out.println("*************** Final Report ***************");
		System.out.println("********************************************");
		System.out.println("Evaluation value: "+ alg.evaluation);
		for(int i=0; i<alg.solution.length;i++){
			System.out.print("x["+i+"]="+ alg.solution[i]+"  ");
		}
		System.out.println();
		System.out.println();
		System.out.println("Computational time: "+ (end - start)  + "ms");
	}

	private static ArrayList<Gold> readFileGolds(String filePath) {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<Gold> tempgolds = new ArrayList<Gold>();
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				if(line.indexOf('#')>=0) continue; 
				String[] spl = line.split(",");
				double size = Double.valueOf(spl[1]);
				double value = Double.valueOf(spl[0]);

				Gold aGold = new Gold(size, value);
				tempgolds.add(aGold);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tempgolds;
	}

	private static ArrayList<Bag> readFileBags(String filePath) {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<Bag> tempbags = new ArrayList<Bag>();
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				double size = Double.valueOf(line);
				Bag aBag = new Bag(size);
				tempbags.add(aBag);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return tempbags;
	}
	
}

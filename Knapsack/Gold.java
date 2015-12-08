
public class Gold {

	double weight;	//金塊の大きさ
	double value;   //金塊の価値
	
	public Gold(double size2, double value2) {
		setValue(value2);
		setWeight(size2);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double size) {
		this.weight = size;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}

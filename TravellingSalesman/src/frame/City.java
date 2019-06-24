package frame;

public class City {
	
	int _id;
	private double _x;
	private double _y;
	
	/**
	 * A city is defined by its ID and its position (x- and y-Coordinates).
	 */
	public City(int id, double x, double y) {
		_id = id;
		_x = x;
		_y = y;
	}
	
	public int id() {
		return _id;
	}
	
	public double x() {
		return _x;
	}
	
	public double y() {
		return _y;
	}

}

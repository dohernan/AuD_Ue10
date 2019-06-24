package lab;

import java.util.LinkedList;
import java.util.ListIterator;

import frame.City;

/**
 * In this class, you should implement the backtracking algorithm for solving the TSP.
 * This is an abstract class - it can't be used directly, but classes that inherit from
 * this class and implement all the abstract methods can be used. BasicTSPSolver is such
 * a class.
 */
public abstract class AbstractTSPSolver {
	
	private LinkedList<City> _cities;
	private LinkedList<City> _solution;
	private double _length;
	private double[][] _distanceMap;
	private boolean[] used;
	
	/**
	 * Create a solver on the given set of cities.
	 */
	public AbstractTSPSolver(LinkedList<City> cities) {
		_cities = cities;
		_solution = null;
		_length = -1;
		_distanceMap = new double[cities.size()][cities.size()];
	}
	
	/**
	 * Return the cities this TSP is defined on.
	 */
	public LinkedList<City> cities() {
		return _cities;
	}
	
	/**
	 * Return the optimal roundtrip as a list of cities.
	 */
	public LinkedList<City> solution() {
		return _solution;
	}
	
	/**
	 * Return the length of the optimal solution.
	 */
	public double length() {
		return _length;
	}
	
	/**
	 * Pre-calculate a map for all distances between cities.
	 */
	public void buildDistanceMap() {

		for(int i=0; i<_cities.size(); i++){
			for(int j=i+1; j<_cities.size(); j++){
				_distanceMap[i][j]=Math.sqrt(Math.pow(_cities.get(i).x()-_cities.get(j).x(),2)+Math.pow(_cities.get(i).y()-_cities.get(j).y(),2));
				_distanceMap[j][i]=_distanceMap[i][j];

			}
		}
	}
	
	/**
	 * Return the distance between City a and City b.
	 */
	public double distance(City a, City b) {
		return _distanceMap[a.id()][b.id()];
	}
	
	/**
	 * Solve the TSP.
	 */
	public void solve() {
		used = new boolean[_cities.size()];
		used[0]=true;
		LinkedList<City> startCities = new LinkedList<>();
		startCities.add(_cities.get(0));
		solveBack(0,startCities);
	}

	public void solveBack(double currentLength, LinkedList<City> currentCities){
		double distance;
		if(currentCities.size()==_cities.size()){
			currentLength += distance(currentCities.getLast(),currentCities.getFirst());
		}
		if(currentCities.size()==_cities.size()&&(currentLength<_length||_length<0)){
			notifyNewBest(currentCities,currentLength);
			_solution = new LinkedList<City>();
			for(int i=0; i<currentCities.size();i++){
			_solution.add(currentCities.get(i));
			}
			_length=currentLength;
		}
		else{
			if(!prune(currentCities,currentLength)) {
				for (int i = 1; i < _cities.size(); i++) {
					if (!used[i]) {
						City y = currentCities.getLast();
						currentCities.add(_cities.get(i));
						used[i] = true;
						distance= distance(currentCities.getLast(),y);
						currentLength +=distance; // es ware dasgleiche stattt getLast --- cities[i]
						solveBack(currentLength, currentCities);
						currentLength -=  distance;
					}
				}
			}

		}
		used[currentCities.getLast().id()]=false;
		currentCities.removeLast();
	}
	
	/**
	 * This should be called for every recursion step in the solver. DO NOT IMPLEMENT THIS
	 * METHOD HERE!
	 * @param currentList the current list of cities that should become the roundtrip 
	 * @param currentLength the distance needed to travel through all the cities in the given order
	 * @return true if this direction of the backtracking should be stopped; false, if it should continue
	 */
	protected abstract boolean prune(LinkedList<City> currentList, double currentLength);
	
	/**
	 * Always call this method if your solver found a solution that is better than all previous
	 * solutions. DO NOT IMPLEMENT THIS METHOD HERE!
	 * @param goodSolution The roundtrip (should start at ID 0 and contain every city exactly once).
	 * @param length The length of the roundtrip.
	 */
	protected abstract void notifyNewBest(LinkedList<City> goodSolution, double length);
}

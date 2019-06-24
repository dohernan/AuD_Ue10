package lab;

/**
 * Aufgabe H1d)-H1e)
 * 
 * Abgabe von: <name>, <name> und <name>
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import frame.City;

/**
 * This is an optimized TSP solver.
 */
public class OptimizedTSPSolver extends AbstractTSPSolver {

	private boolean useLengthPruining;
	private boolean useIntersectionPruning;
	/**
	 * Initialize the solver with the list of cities and decide which optimisations to use. 
	 */
	public OptimizedTSPSolver(LinkedList<City> cities, boolean useLengthPruning, boolean useIntersectionPruning) {
		super(cities);
		this.useIntersectionPruning=useIntersectionPruning;
		this.useLengthPruining=useLengthPruning;
	}

	@Override
	protected boolean prune(LinkedList<City> currentList, double currentLength) {
		if(useLengthPruining) {
			if (this.length() > 0 && currentLength + this.distance(currentList.getLast(), currentList.getFirst()) > this.length()) {
				return true;
			}
		}
		if(useIntersectionPruning){
			if(currentList.size()>3){
				double y4=currentList.getLast().y();
				double x4=currentList.getLast().x();
				double y3=currentList.get(currentList.size()-2).y();
				double x3=currentList.get(currentList.size()-2).x();
				for(int i=0; i<currentList.size()-2;i++) {
					double x1=currentList.get(i).x();
					double y1=currentList.get(i).y();
					double x2=currentList.get(i+1).x();
					double y2=currentList.get(i+1).y();
					double ta = ((y3-y4)*(x1-x3)+(x4-x3)*(y1-y3))/((x4-x3)*(y1-y2)-(x1-x2)*(y4-y3));
					double tb = ((y1-y2)*(x1-x3)+(x2-x1)*(y1-y3))/((x4-x3)*(y1-y2)-(x1-x2)*(y4-y3));
					if((0<ta&&ta<1)&&(0<tb&&tb<1)){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void notifyNewBest(LinkedList<City> goodSolution, double length) {

	}

}

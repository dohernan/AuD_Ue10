package lab;

import java.util.LinkedList;

import frame.City;

/**
 * This is a simple TSP solver with no further optimisations.
 */
public class BasicTSPSolver extends AbstractTSPSolver {

	public BasicTSPSolver(LinkedList<City> cities) {
		super(cities);
	}

	@Override
	protected boolean prune(LinkedList<City> currentList, double currentLength) {
		return false;
	}

	@Override
	protected void notifyNewBest(LinkedList<City> goodSolution, double length) {
	}

}

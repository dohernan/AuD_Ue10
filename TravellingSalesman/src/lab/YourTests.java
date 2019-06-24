package lab;

/**
 * Aufgabe H1
 * 
 * Abgabe von: <name>, <name> und <name>
 */

import static org.junit.jupiter.api.Assertions.*;

import frame.City;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Use this class to implement your own tests.
 */
class YourTests {

	@Test
	void test() {

			System.out.println("\nStarting OptimizedTSPSolver (all optimisations) test with berlin12.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
				CityParser parser = new CityParser("tests/public/berlin12.txt");
				LinkedList<City> cities = parser.readFile();

				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, true);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4056.7, solver.length(), 0.1, "OptimizedTSPSolver (all optimisations): berlin12.txt: Wrong minimal length!");
				assertEquals(12, solver.solution().size(), "OptimizedTSPSolver (all optimisations): berlin12.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (all optimisations) test with berlin12.txt successfully!");




	}

	protected void printSolution(LinkedList<City> solution, double length) {
		ListIterator<City> iter = solution.listIterator();
		while( iter.hasNext() ) {
			System.out.print(" " + iter.next().id() + " ");
			if( iter.hasNext() ) {
				System.out.print("->");
			}
		}
		System.out.print("-> " +solution.getFirst().id());
		System.out.print(": "+length+"\n");
	}

}

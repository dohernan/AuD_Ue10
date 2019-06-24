package frame;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import lab.BasicTSPSolver;
import lab.CityParser;
import lab.OptimizedTSPSolver;

/**
 * Do NOT change anything in this class!
 * 
 * The test cases defined by this class are used to test if your program
 * works correctly. This class is also responsible for outputting to the
 * console.
 * 
 */

@DisplayName("Exercise 10 - Travelling Salesman")
class PublicTests {
	
	protected int correct = 0;
	protected Duration timeout = Duration.ofSeconds(10);
	
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
	
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("A: CityParser")
	class A_CityParser {
		
		@DisplayName("Test reading file")
		@Test
		@Order(1)
		void test_reading() {
			System.out.println("Starting CityParser test...");
			assertTimeoutPreemptively(timeout, () -> {
				CityParser parser = new CityParser("tests/public/berlin11.txt");
				LinkedList<City> cities = parser.readFile();
				City firstCity = cities.getFirst();
				assertEquals(0, firstCity.id(), "ID of the first city should be 0.");
				assertEquals(565.0, firstCity.x(), "x-Coordinate of the first city is wrong!");
				assertEquals(575.0, firstCity.y(), "y-Coordinate of the first city is wrong!");
				City lastCity = cities.getLast();
				assertEquals(10, lastCity.id(), "ID of the last city should be 10.");
				assertEquals(1605.0, lastCity.x(), "x-Coordinate of the last city is wrong!");
				assertEquals(620.0, lastCity.y(), "y-Coordinate of the last city is wrong!");
			});
			System.out.println("Finished CityParser test successfully!");
		}
	}
	
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("B: BasicTSPSolver")
	class B_TSPSolver {
		
		@DisplayName("BasicTSPSolver: distance map")
		@Test
		@Order(1)
		void test_distance() {
			System.out.println("\nStarting distance test...");
			assertTimeoutPreemptively(timeout, () -> {
				City a = new City(0, 0.0, 0.0);
				City b = new City(1, 1.0, 1.0);
				City c = new City(2, 5.0, 0.0);
				City d = new City(3, 0.0, -3.0);
				LinkedList<City> cities = new LinkedList<City>( Arrays.asList(a, b, c, d) );
				
				BasicTSPSolver solver = new BasicTSPSolver(cities);
				solver.buildDistanceMap();
				
				assertEquals(1.4142135623730951, solver.distance(a, b), 0.0001);
				assertEquals(1.4142135623730951, solver.distance(b, a), 0.0001);
				assertEquals(5.0, solver.distance(a, c), 0.0001);
				assertEquals(5.0, solver.distance(c, a), 0.0001);
				assertEquals(3.0, solver.distance(a, d), 0.0001);
				assertEquals(3.0, solver.distance(d, a), 0.0001);
				assertEquals(5.830951894845301, solver.distance(c, d), 0.0001);
				assertEquals(5.830951894845301, solver.distance(d, c), 0.0001);
				assertEquals(4.123105625617661, solver.distance(b, d), 0.0001);
				assertEquals(4.123105625617661, solver.distance(d, b), 0.0001);
			});
			System.out.println("Finished distance test successfully!");
		}
		
		@DisplayName("BasicTSPSolver: Solver: berlin10.txt")
		@Test
		@Order(2)
		void test_berlin10() {
			System.out.println("\nStarting BasicTSPSolver test with berlin10.txt...");
			assertTimeoutPreemptively(timeout, () -> {
				CityParser parser = new CityParser("tests/public/berlin10.txt");
				LinkedList<City> cities = parser.readFile();
				
				BasicTSPSolver solver = new BasicTSPSolver(cities);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(2826.5, solver.length(), 0.1, "BasicTSPSolver: berlin10.txt: Wrong minimal length!");
				assertEquals(10, solver.solution().size(), "BasicTSPSolver: berlin10.txt: Tour has wrong length!");
				assertEquals(0, solver.solution().getFirst().id(), "BasicTSPSolver: berlin10.txt: Roundtrip should start at City 0!");
			});
			System.out.println("Finished BasicTSPSolver test with berlin10.txt successfully!");
		}
		
		@DisplayName("BasicTSPSolver: Solver: berlin11.txt")
		@Test
		@Order(3)
		void test_berlin11() {
			System.out.println("\nStarting BasicTSPSolver test with berlin11.txt...");
			assertTimeoutPreemptively(timeout, () -> {
				CityParser parser = new CityParser("tests/public/berlin11.txt");
				LinkedList<City> cities = parser.readFile();
				
				BasicTSPSolver solver = new BasicTSPSolver(cities);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4038.4, solver.length(), 0.1, "BasicTSPSolver: berlin11.txt: Wrong minimal length!");
				assertEquals(11, solver.solution().size(), "BasicTSPSolver: berlin11.txt: Tour has wrong length!");
				assertEquals(0, solver.solution().getFirst().id(), "BasicTSPSolver: berlin11.txt: Roundtrip should start at City 0!");
			});
			System.out.println("Finished BasicTSPSolver test with berlin11.txt successfully!");
		}
		
		@DisplayName("BasicTSPSolver: Solver: berlin12.txt")
		@Test
		@Order(4)
		void test_berlin12() {
			System.out.println("\nStarting BasicTSPSolver test with berlin12.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
				CityParser parser = new CityParser("tests/public/berlin12.txt");
				LinkedList<City> cities = parser.readFile();
				
				BasicTSPSolver solver = new BasicTSPSolver(cities);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4056.7, solver.length(), 0.1, "BasicTSPSolver: berlin11.txt: Wrong minimal length!");
				assertEquals(12, solver.solution().size(), "BasicTSPSolver: berlin12.txt: Tour has wrong length!");
				assertEquals(0, solver.solution().getFirst().id(), "BasicTSPSolver: berlin12.txt: Roundtrip should start at City 0!");
			});
			System.out.println("Finished BasicTSPSolver test with berlin12.txt successfully!");
		}
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("C: OptimizedTSPSolver with length pruning")
	class C_OptimizedTSPSolver_Length {
		
		@DisplayName("OptimizedTSPSolver (length pruning): Solver: berlin11.txt")
		@Test
		@Order(1)
		void test_berlin11() {
			System.out.println("\nStarting OptimizedTSPSolver (length pruning) test with berlin11.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
				CityParser parser = new CityParser("tests/public/berlin11.txt");
				LinkedList<City> cities = parser.readFile();
				
				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, false);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4038.4, solver.length(), 0.1, "OptimizedTSPSolver (length pruning): berlin11.txt: Wrong minimal length!");
				assertEquals(11, solver.solution().size(), "OptimizedTSPSolver (length pruning): berlin11.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (length pruning) test with berlin11.txt successfully!");
		}
		
		@DisplayName("OptimizedTSPSolver (length pruning): Solver: berlin12.txt")
		@Test
		@Order(2)
		void test_berlin12() {
			System.out.println("\nStarting OptimizedTSPSolver (length pruning) test with berlin12.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
				CityParser parser = new CityParser("tests/public/berlin12.txt");
				LinkedList<City> cities = parser.readFile();
				
				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, false);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4056.7, solver.length(), 0.1, "OptimizedTSPSolver (length pruning): berlin12.txt: Wrong minimal length!");
				assertEquals(12, solver.solution().size(), "OptimizedTSPSolver (length pruning): berlin12.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (length pruning) test with berlin12.txt successfully!");
		}
		
		@DisplayName("OptimizedTSPSolver (length pruning): Solver: berlin13.txt")
		@Test
		@Order(3)
		void test_berlin13() {
			System.out.println("\nStarting OptimizedTSPSolver (length pruning) test with berlin13.txt...");
			assertTimeoutPreemptively(timeout, () -> {
				CityParser parser = new CityParser("tests/public/berlin13.txt");
				LinkedList<City> cities = parser.readFile();
				
				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, false);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4564.5, solver.length(), 0.1, "OptimizedTSPSolver (length pruning): berlin13.txt: Wrong minimal length!");
				assertEquals(13, solver.solution().size(), "OptimizedTSPSolver (length pruning): berlin13.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (length pruning) test with berlin13.txt successfully!");
		}

	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("D: OptimizedTSPSolver with both optimisations")
	class D_OptimizedTSPSolver_All {
		
		@DisplayName("OptimizedTSPSolver (all optimisations): Solver: berlin12.txt")
		@Test
		@Order(1)
		void test_berlin12() {
			System.out.println("\nStarting OptimizedTSPSolver (all optimisations) test with berlin12.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
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
		
		@DisplayName("OptimizedTSPSolver (all optimisations): Solver: berlin13.txt")
		@Test
		@Order(2)
		void test_berlin13() {
			System.out.println("\nStarting OptimizedTSPSolver (all optimisations) test with berlin13.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
				CityParser parser = new CityParser("tests/public/berlin13.txt");
				LinkedList<City> cities = parser.readFile();
				
				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, true);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4564.5, solver.length(), 0.1, "OptimizedTSPSolver (all optimisations): berlin13.txt: Wrong minimal length!");
				assertEquals(13, solver.solution().size(), "OptimizedTSPSolver (all optimisations): berlin13.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (all optimisations) test with berlin13.txt successfully!");
		}
		
		@DisplayName("OptimizedTSPSolver (all optimisations): Solver: berlin14.txt")
		@Test
		@Order(3)
		void test_berlin14() {
			System.out.println("\nStarting OptimizedTSPSolver (all optimisations) test with berlin14.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(8), () -> {
				CityParser parser = new CityParser("tests/public/berlin14.txt");
				LinkedList<City> cities = parser.readFile();
				
				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, true);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4946.8, solver.length(), 0.1, "OptimizedTSPSolver (all optimisations): berlin14.txt: Wrong minimal length!");
				assertEquals(14, solver.solution().size(), "OptimizedTSPSolver (all optimisations): berlin14.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (all optimisations) test with berlin14.txt successfully!");
		}
		
		@DisplayName("OptimizedTSPSolver (all optimisations): Solver: berlin15.txt")
		@Test
		@Order(4)
		void test_berlin15() {
			System.out.println("\nStarting OptimizedTSPSolver (all optimisations) test with berlin15.txt...");
			assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
				CityParser parser = new CityParser("tests/public/berlin15.txt");
				LinkedList<City> cities = parser.readFile();
				
				OptimizedTSPSolver solver = new OptimizedTSPSolver(cities, true, true);
				solver.buildDistanceMap();
				solver.solve();
				printSolution(solver.solution(), solver.length());
				assertEquals(4967.3, solver.length(), 0.1, "OptimizedTSPSolver (all optimisations): berlin15.txt: Wrong minimal length!");
				assertEquals(15, solver.solution().size(), "OptimizedTSPSolver (all optimisations): berlin15.txt: Tour has wrong length!");
			});
			System.out.println("Finished OptimizedTSPSolver (all optimisations) test with berlin15.txt successfully!");
		}
	}
}
	
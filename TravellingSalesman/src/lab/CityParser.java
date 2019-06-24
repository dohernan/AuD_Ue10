package lab;

/**
 * Aufgabe H1a)
 * 
 * Abgabe von: <name>, <name> und <name>
 */

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import frame.City;

public class CityParser {
	
	private String _filename;
	
	public CityParser(String filename) {
		_filename = filename;
	}
	
	/**
	 * Read cities from the given file and enter them into a LinkedList.
	 * Each city is in its own line, written as "x;y". 
	 * Throw a RuntimeException if anything goes wrong.
	 * Return the LinkedList of the cities in the same order as they were in the file.
	 */
	public LinkedList<City> readFile() {

		LinkedList<City> list = new LinkedList<City>();

		File file = new File(_filename);
		BufferedReader br;
		String st;

		try {
			br = new BufferedReader(new FileReader(file));
			int id=0;
			while ((st = br.readLine()) != null){
				st=st.replace(" ","");
				City c = new City(id,Double.parseDouble(st.split(";")[0]),Double.parseDouble(st.split(";")[1]));
				list.add(c);
				id++;
			}
		}
		catch(FileNotFoundException e){
			//FILE NOT FOUND EXCEPTION
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}

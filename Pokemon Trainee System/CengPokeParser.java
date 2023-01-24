import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CengPokeParser {

	public static ArrayList<CengPoke> parsePokeFile(String filename)
	{
		ArrayList<CengPoke> pokeList = new ArrayList<CengPoke>();

		// You need to parse the input file in order to use GUI tables.
		// TODO: Parse the input file, and convert them into CengPokes

		return pokeList;
	}

	public static void startParsingCommandLine() throws IOException
	{

		Scanner input = new  Scanner(System.in);
		String inputcommand, name, power, type;
		Integer key;
		CengPoke tempPoke;

		while(input.hasNext()){

			inputcommand = input.next();
			if(inputcommand.equalsIgnoreCase("add")){

				inputcommand = input.next();
				key = Integer.parseInt(inputcommand);

				inputcommand = input.next();
				name = inputcommand;

				inputcommand = input.next();
				power = inputcommand;

				inputcommand = input.next();
				type = inputcommand;

				tempPoke = new CengPoke(key, name, power, type);
				CengPokeKeeper.addPoke(tempPoke);
			}
			else if(inputcommand.equalsIgnoreCase("delete")){

				inputcommand = input.next();
				key = Integer.parseInt(inputcommand);
				CengPokeKeeper.deletePoke(key);
			}

			else if(inputcommand.equalsIgnoreCase("search")){

				inputcommand = input.next();
				key = Integer.parseInt(inputcommand);
				CengPokeKeeper.searchPoke(key);
			}

			else if(inputcommand.equalsIgnoreCase("print")){

				CengPokeKeeper.printEverything();
			}
			else if(inputcommand.equalsIgnoreCase("quit")){

				System.exit(0);
			}
		}
		// 1) quit : End the app. Print nothing, call nothing.
		// 2) add : Parse and create the poke, and call CengPokeKeeper.addPoke(newlyCreatedPoke).
		// 3) search : Parse the pokeKey, and call CengPokeKeeper.searchPoke(parsedKey).
		// 4) delete: Parse the pokeKey, and call CengPokeKeeper.removePoke(parsedKey).
		// 5) print : Print the whole hash table with the corresponding buckets, call CengPokeKeeper.printEverything().

		// Commands (quit, add, search, print) are case-insensitive.
	}
}
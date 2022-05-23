// Java Program to get elements of HashSet by index using
// ArrayList

import java.util.*;

class GFG {
	public static void main(String[] args)
	{

		// Declare HashSet using Set Interface
		Set<String> GFG = new HashSet<String>();

		// Add elements into HashSet using add()
		GFG.add("Welcome");
		GFG.add("To");
		GFG.add("Geeks");
		GFG.add("For");
		GFG.add("Geek");

		// Displaying HashSet
		System.out.println("HashSet contains: " + GFG);

		// Notice the order of elements may be different
		// than insertion

		// Converting HashSet to ArrayList
		List<String> list = new ArrayList<String>(GFG);

		System.out.println("Element at index 3 is: "
						+ list.get(3));
	}
}


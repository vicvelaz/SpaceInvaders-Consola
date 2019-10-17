package tp.p1;

import tp.p1.control.Controller;
import tp.p1.logic.Game;
import tp.p1.logic.Level;

public class Main {
	
	public static void main(String[] args) {
		
		Level level = null;
		int seed = 0;
		if (args.length > 0) {
			try {
				level = Level.valueOf(args[0].toUpperCase());
				if (args.length == 2) {
					seed = Integer.parseInt(args[1]);
				}
			}
			catch (IllegalArgumentException ex) {
				System.err.println("Invalid arguments provided. Please provide a valid level and seed.");
			}
		}
		if (level == null) {
			System.out.println("Level not valid"); 
		}
		else { 
			Controller controller = new Controller(new Game(level, seed)); 
			controller.run(); 
		}
	}
}

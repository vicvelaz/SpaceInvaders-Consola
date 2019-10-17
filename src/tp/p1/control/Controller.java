package tp.p1.control;

import java.util.Scanner;
import tp.p1.logic.Game;
import tp.p1.logic.GamePrinter;
import tp.p1.logic.Move;

public class Controller {

	private Game game;
	private Scanner input;
	private GamePrinter gamePrinter;
	private boolean reset = false;
	
	public Controller(Game game) {
		this.game = game;
		input = new Scanner(System.in); 
	}
	
	//----------------------------------------------------//
	
	private void draw() {
		//---//PRINT INFO//-------------------------
		System.out.println(game.getCycleInfo());
		
		//---//PRINT GAME//-------------------------
		gamePrinter = new GamePrinter(game, Game.NUM_COLUMNS, Game.NUM_ROWS);		
		System.out.println(gamePrinter.toString());
		
	}
	
	//----------------------------------------------------//
	
	private boolean validMove(String[] args) {
		if (args.length == 3) {
			Move move;
			int numColumns;
			
			// Type Handler
			try {
				
				move = Move.valueOf(args[1].toUpperCase());
				numColumns = Integer.parseInt(args[2]);
				
			} catch (IllegalArgumentException ex) {
				System.out.println("Invalid argument types provided. Use the following structure in the correct order: [M]OVE <left|right> <1|2>");
				return false;
			}
			
			// Value Handler
			try {
				if (game.validUCMShipMove(move, numColumns)) {
					game.moveUCMShip(move, numColumns);
					
					return true;
				}
			} catch (IllegalArgumentException ex) {
				System.err.println(ex.getMessage());
			}
			
		} else { 
			System.out.println("Invalid arguments provided. Use the following structure: [M]OVE <left|right> <1|2>");
		}
		
		return false;
	}
	
	private boolean userCommand() {
		
		boolean exit = false, validCommand = false;
			
		while (!validCommand) {
			System.out.print("Command > ");
			String commandText = input.nextLine();
			String[] args = commandText.toLowerCase().trim().split("\\s+");
				
			//Command Provided
			if (args.length > 0) { 		
				switch (args[0]) {
					case "move":
					case "m":	
						validCommand = validMove(args);
						break;
							
					case "shoot":
					case "s":	
						if(game.shootLaser()) {
							validCommand = true;
						}
						break;
						
					case "wave":
					case "w":	
						if(game.useShockWave()){ 
							validCommand = true;
						}
								
						break;
						
					case "list":
					case "l":
						System.out.println(game.showShipsList());
						break;
						
					case "help":
					case "h":
						System.out.println(game.showHelp());
						break;
							
					case "none":
					case "n":
					case "":
						validCommand = true;
						break;
							
					case "reset":
					case "r":	
						validCommand = true;
						reset = true;
						game.resetGame();
						break;
								
					case "exit":
					case "e":	
						validCommand = true;
						exit = true;
						System.out.println("GAME OVER\n");
						break;
						
					default: //Unknown Command
						System.err.println("ERROR: Unknown command provided. Type 'help' to see all available commands.\n");
						break;
					}
				} else { 
					System.err.println("ERROR: No command provided. Type 'help' to see all available commands.\n");
				}	
		}
		return exit;
	}
	
	
	public void run() {
		boolean exit = false, victory = false;
		while(!game.hasFinished() && !exit) {		
			reset = false;
			draw();
			exit = userCommand();		
			if (!exit && !reset) {
				game.computerAction(); 
				victory = game.update(); //devuelve true si no quedan naves enemigas 
				if(!game.hasFinished()) {
					game.increaseCycle();
				} else {
					draw(); 
					System.out.println("Game Over: "); 
					if (victory) {
						System.out.println("Player wins!");
					} else {
						System.out.println("Computer wins!");
					}
				}
			}
			
		}
		
		input.close();
	}
}

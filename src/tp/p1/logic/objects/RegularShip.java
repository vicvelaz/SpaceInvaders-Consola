package tp.p1.logic.objects;

import tp.p1.logic.Game;
import tp.p1.logic.Move;

public class RegularShip {
	public final static int CYCLES = 1;
	private final static int POINTS = 5;
	private final static int DAMAGE = 0;
	private final static int HEALTH = 2;
	
	private int x , y;
	private int health, contCycles;
	private Game game;
	
	
	public RegularShip(int x,int y,Game game)
	{
		this.x = x;
		this.y = y;
		this.health = HEALTH;
		this.game = game;
	}
	
	//-------------------------------------------//
	
	public static int getPoints() {
		return POINTS;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHealth() {
		return health;
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	//-------------------------------------------//
	
	public boolean update(Move nextDirection) {
		
		boolean lastRow = false;
		if (contCycles == CYCLES - 1) {
			
			contCycles = 0;

			if(nextDirection == Move.LEFT) {
				y--;
			} else if (nextDirection == Move.RIGHT) {
				y++;
			} else if (nextDirection == Move.DOWN) { 
				x++;
				if (x == Game.NUM_ROWS-1)
					lastRow = true;
			}
			if(game.isLaserColliding(x,y))
			{
				this.takeDamage(game.getUCMLaserDamage());
				game.removeUCMLaser();
			}
		} else { 
			contCycles++;
		}
		
		return lastRow;
	}
	
	public void takeDamage(int damage) {
		health -= damage;
		if (health <= 0) {
			game.removeRegularShip(x,y);
			game.addPoints(POINTS);
		}
	}
	
	public String toString() {
		return "R[" + health + "]";
	}

	public static String info() {
		return "[R]egular ship: Points: " + getPoints() + " - Damage: " + DAMAGE + " - Shield: " + HEALTH;
	}
	
}

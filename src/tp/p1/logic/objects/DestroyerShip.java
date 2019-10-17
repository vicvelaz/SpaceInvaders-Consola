package tp.p1.logic.objects;

import java.util.Random;

import tp.p1.logic.Game;
import tp.p1.logic.Move;

public class DestroyerShip {
	public final static int CYCLES = 1;
	public final static int POINTS = 10;
	public final static int DAMAGE = 1;
	private final static int HEALTH = 1;
	
	private int x, y;
	private int health, contCycles;
	private Bomb bomb;
	private Game game;
	private Random number;
	private boolean willShoot;
	
	public DestroyerShip(int x, int y, Game game)
	{
		this.x = x;
		this.y = y;
		this.health = HEALTH;
		this.game = game;
		this.willShoot = false;
		this.number = this.game.getRand();
	
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
	
	public Bomb getBomb() {
		return this.bomb;
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	//-------------------------------------------//
	
	//Duda importante -> Si la nave !isAlive OR game.isLaserColliding(x,y), en cualquiera de estos casos, la nave deberia borrarse antes de que el resto del update se ejecute
	// La duda es, que es contCycles == CYCLES -1, y por que se ejecuta antes que isLaserColliding?
	public boolean update(Move nextDirection) {
		boolean lastRow = false;
		
		if (contCycles == CYCLES - 1) {
			
			contCycles = 0;
			if(nextDirection == Move.LEFT)
				y--;
			else if(nextDirection == Move.RIGHT)
				y++;
			else if (nextDirection == Move.DOWN) 
				x++;
			
			if (x == Game.NUM_ROWS-1) {
				lastRow = true;
			}
			
			if(game.isLaserColliding(x,y) ) {
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
		if (!isAlive()) {
			game.removeDestroyerShip(x,y);
			game.addPoints(POINTS);
		}
	}
	
	public String toString() {
		return "D[" + health + "]";
	}

	public static String info() {
		return "[D]estroyer ship: Points: " + getPoints() + " - Damage: " + DAMAGE + " - Shield: " + HEALTH;
	}

	public boolean canDestroyerShoot() {
		float numRandom;			
		boolean shoot = false;
		numRandom = number.nextInt(10); 
			if (numRandom/10 <= game.getLevel().getShootFrequency()) {
				shoot = true; 
			}	
		return shoot;
	}

	public boolean getWillShoot() {
		return willShoot;
	}

	public void setWillShoot(boolean willShoot) {
		this.willShoot = willShoot;
	}

	public void setBomb(Bomb bomb) {
		this.bomb = bomb;
	}
}

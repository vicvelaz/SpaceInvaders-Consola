package tp.p1.logic.objects;

import tp.p1.logic.Game;
import tp.p1.logic.Move;

public class UCMShip {
	public final static int CYCLES = 1;
	public final static int DAMAGE = 1;
	public final static int MAX_MOVES = 2;
	private final static int HEALTH = 3;
	
	private int x, y;
	private int health, contCycles;
	private Game game;
	private UCMShipLaser ucmShipLaser;
	
	public UCMShip(Game game)
	{
		this.x = 7;
		this.y = 4;
		this.health = 3;
		this.contCycles = 0;
		this.game = game;
		this.ucmShipLaser = null;
	}
	
	//-------------------------------------------//

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHealth() {
		return health;
	}
	
	public UCMShipLaser getLaser() {
		return ucmShipLaser;
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	//-------------------------------------------//
	
	public void update() {
		if (contCycles == CYCLES - 1) {
			contCycles = 0;
			if (ucmShipLaser != null && ucmShipLaser.update())
				eraseLaser();
		} else { 
			contCycles++;
		}
	}
	

	public void attack(){
		ucmShipLaser = new UCMShipLaser(x, y,  game); //Avanzara 1 hacia arriba en el primer update, donde se comprueba si hace danio a algun enemigo
	}
	
	public void takeDamage(int damage) {
		health -= damage;
	}
	
	public String toString() {
		if (health > 0)
			return "^___^";
		else
			return"!xx!";
	}

	public static String info() {
		return "^___^: Damage:" + DAMAGE + " - Shield:" + HEALTH;
	}

	public boolean findUCMShipAt(int x, int y) {
		return (this.x == x && this.y == y);
	}

	public boolean canShoot() {
		if (ucmShipLaser == null)
			return true;
		else 
			return false;
	}

	public void move(Move move, int numColumns) {
		switch (move) {
			case LEFT:
				if ((y - numColumns) >= 0)
					y -= numColumns;
				else 
					throw new IllegalArgumentException("Cannot move out of boundaries. Please provide a possible Row movement.");
				
				break;
			
			case RIGHT:
				if ((y + numColumns) <= (Game.NUM_COLUMNS - 1)) 
					y += numColumns;
				else
					throw new IllegalArgumentException("Cannot move out of boundaries. Please provide a possible Row movement.");
				
				break;
			
			default:
				break;
		}
	}

	public void eraseLaser() {
		this.ucmShipLaser = null;
	}
	
}

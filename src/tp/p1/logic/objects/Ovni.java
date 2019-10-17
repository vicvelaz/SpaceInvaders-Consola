package tp.p1.logic.objects;


import tp.p1.logic.Game;

public class Ovni {
	public final static int CYCLES = 1;
	public final static int POINTS = 25;
	private final static int DAMAGE = 0;
	private final static int HEALTH = 1;
	
	
	private int x, y;
	private int health, contCycles;
	private Game game;
	//private Random number;
	
	public Ovni(Game game)
	{
		this.x = 0;
		this.y = 9;	//Una posicion mas a la derecha, para que en el update aparezca en la posicion correcta
		this.health = 1;
		this.game = game;
		//this.number=this.game.getRand();
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
	
	public boolean update() {
		boolean erase = false;
		if (contCycles == CYCLES - 1) {
			
			contCycles = 0;
			y--;
			if (y < 0)
				erase = true;
			
			if(game.isLaserColliding(x,y))
			{
				this.takeDamage(game.getUCMLaserDamage());
				game.removeUCMLaser();
			}
				
		} else {
			contCycles++;
		}
		return erase;
	}
	
	public void takeDamage(int damage) {
		health-=damage;
		if (health <= 0) {
			game.removeOvni();
			game.addPoints(POINTS);
		}
	}
	
	public boolean findOvniAt(int x, int y) {
		return (this.x == x && this.y == y);
	}
	
	public String toString() {
		return "O[" + health + "]";
	}

	public static String info() {
		return "[O]vni: Points: " + getPoints() + " - Damage: " + DAMAGE + "- Shield: " + HEALTH;

	}
}

package tp.p1.logic;

import java.util.Random;

import tp.p1.logic.lists.*;
import tp.p1.logic.objects.Bomb;
import tp.p1.logic.objects.DestroyerShip;
import tp.p1.logic.objects.Ovni;
import tp.p1.logic.objects.RegularShip;
import tp.p1.logic.objects.UCMShip;
import tp.p1.logic.objects.UCMShipLaser;

public class Game {

	public final static int NUM_ROWS = 8;
	public final static int NUM_COLUMNS = 9;
	private final static int DAMAGE_SW = 1;
	
	//[Game Info]//
	private int points, cycles;
	private int shockWave;
	private Level level;
	private Random rand;
	private int seed;
	private boolean hasFinished;
	private Move direction;
	
	//[Board Info]//
	private DestroyerShipList destroyerShipList;
	private RegularShipList regularShipList;
	private BombList bombList;
	private UCMShip ucmShip;
	private Ovni ovni;
	
	public Game(Level level, int seed)
	{	
		//Game Vars//
		this.level = level;
		this.seed = seed;
		rand = new Random();
		rand.setSeed(seed);
		direction=Move.LEFT;
		
		//Initialized Lists//
		destroyerShipList = new DestroyerShipList(this.level.getNumDestroyerShips(), this);
		regularShipList = new RegularShipList(this.level.getNumRegularShips(), this);
		bombList = new BombList(this.level.getNumDestroyerShips(), this);
		ucmShip = new UCMShip(this);
		
		shockWave = 0;
		hasFinished = false;
		points = 0;
		cycles = 0;
		
		//Initialized EnemyShips
		placeShips();
		
	}
	
	public void placeShips() {
		int numRegulars = level.getNumRegularShips();
		int x = 1;
		x = regularShipList.placeRegularShips(numRegulars, x);
		
		int numDestroyers = level.getNumDestroyerShips();
		destroyerShipList.placeDestroyerShips(numDestroyers, x);
	}
	
	//-------------------------------------------//
	
	public Level getLevel() {
		return level;
	}
	
	public Move getDirection() {
		return direction;
	}
	
	public int getUCMLaserDamage() {
		return UCMShipLaser.DAMAGE;
	}
	
	public int getSeed() {
		return seed;
	}
	
	public Random getRand() {
		return rand;
	}
	
	public boolean hasFinished() {
		return hasFinished;
	}
	
	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}
	
	public void increaseCycle() {
		this.cycles++;
	}
	
	//Points
	 public void addPoints(int points)
	 {
		 this.points += points; 
	 }
	 
	 public String toString()
	 {
		 return Integer.toString(this.points); 
	 }
	
	//-------------------------------------------//

	//Game Methods//
	
	private void changeDirection() {
		if (direction == Move.LEFT)
			direction = Move.RIGHT;
		else
			direction = Move.LEFT;
	}
	
	//-- Determina si se puede pasar a la siguiente fila
	private boolean switchRow() {
		boolean canSwitch = false;

		canSwitch = destroyerShipList.canSwitchRow();
		
		canSwitch = regularShipList.canSwitchRow();
		
		return canSwitch;
	}
	 
	public boolean update() {
		boolean victory = false;
		Move nextDirection = getDirection();
		ucmShip.update(); //mueve el disparo de la nave y hace daño a la nave o bomba que haya en la posicion en la que se colocaria
		bombList.update(); //hace que caigan las bombas 
		
		if (switchRow()) {
			changeDirection();
			nextDirection = Move.DOWN;
		}
		
		if (regularShipList.update(nextDirection) || destroyerShipList.update(nextDirection) || !ucmShip.isAlive()) { //true si al moverse alguna naveve llega a la ultima fila o si nos quedamos sin vidas
			hasFinished = true;
		}
		
		if (destroyerShipList.getNumDestroyerShips() == 0 && regularShipList.getNumRegularShips() == 0) {
			hasFinished = true;
			victory = true;
		}
		
		if (ovni != null && ovni.update()) {
			ovni = null;
		}
		
		return victory;
	}

	//-------------------------------------------//
	
	//Commands Methods//
	
	public boolean validUCMShipMove(Move direction, int numColumns) {
		if ((numColumns > 0 && numColumns <= UCMShip.MAX_MOVES) && (direction == Move.LEFT || direction == Move.RIGHT)) {
			return true;
		} else if (((numColumns > 0 && numColumns <= UCMShip.MAX_MOVES) && !(direction == Move.LEFT || direction == Move.RIGHT))) {
			throw new IllegalArgumentException("Invalid direction provided. You can only move Left or Right.");
		} else if ((!(numColumns > 0 && numColumns <= UCMShip.MAX_MOVES) && (direction == Move.LEFT || direction == Move.RIGHT))) {
			throw new IllegalArgumentException("Invalid amount of rows provided. You can only move 1-2 Rows per turn.");
		} else if ((!(numColumns > 0 && numColumns <= UCMShip.MAX_MOVES) && !(direction == Move.LEFT || direction == Move.RIGHT))) {
			throw new IllegalArgumentException("Invalid direction and amount of rows provided. You can only move Left or Right and you can only move 1-2 Rows per turn.");
		}
		
		return false;
	}
	
	public void moveUCMShip(Move move, int numColumns) {
		try {
			ucmShip.move(move, numColumns);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
	
	public boolean shootLaser() {
		boolean canShoot = false;
		if (ucmShip.canShoot()) {
			ucmShip.attack();
			canShoot = true;
		}
		else 
			System.out.println("Not enough ammo.");
		return canShoot;
	}

	public Boolean useShockWave() {
		boolean posible = false;
		int numReg=regularShipList.getNumRegularShips();
		int numDest=destroyerShipList.getNumDestroyerShips();
		if (shockWave > 0) {
			for (int i = numReg-1; i >=0; --i) {
				regularShipList.getRegularShipFromList(i).takeDamage(DAMAGE_SW);
			}
			for (int i =numDest-1;i>=0;--i) {
				destroyerShipList.getDestroyerShipFromList(i).takeDamage(DAMAGE_SW);
			}
			shockWave--;
			posible = true;
		}
		else
			System.out.println("Not enough ShockWaves.");
		return posible;
	}
	
	public void resetGame() {
		//Initialized Lists//
		destroyerShipList = new DestroyerShipList(level.getNumDestroyerShips(), this);
		regularShipList = new RegularShipList(level.getNumRegularShips(), this);
		bombList = new BombList(level.getNumDestroyerShips(), this);
		ucmShip = new UCMShip(this);
		direction = Move.LEFT;
			
		shockWave = 0;
		hasFinished = false;
		points = 0;
		cycles = 0;
		placeShips();
	}

	public String showHelp() {
		return System.lineSeparator() +	"List of available commands:"
			 + System.lineSeparator() +	"[M]OVE <left|right> <1|2>: Moves the UCM-Ship to the indicated direction."
			 + System.lineSeparator() +	"[S]HOOT: UCM-Ship launches a missile."
		  	 + System.lineSeparator() +	"SHOCK[W]AVE: UCM-Ship releases a shock wave."
			 + System.lineSeparator() +	"[L]IST: Shows a list with all available ships."
			 + System.lineSeparator() +	"[H]ELP: Shows a list with all available commands."
			 + System.lineSeparator() +	"[R]ESET: Starts a new game."
			 + System.lineSeparator() +	"[E]XIT: Terminates the program."
			 + System.lineSeparator() +	"[N]ONE: Skips one cycle."
			 + System.lineSeparator();
	}

	public String showShipsList() {
		StringBuilder cadena = new StringBuilder();
		cadena.append(RegularShip.info()+System.lineSeparator());
		cadena.append(DestroyerShip.info()+ System.lineSeparator());
		cadena.append(Ovni.info() + System.lineSeparator());
		cadena.append(UCMShip.info()+ System.lineSeparator());
		return cadena.toString();
	}
	
	//-------------------------------------------//
	
	//Auxiliar Methods//
	
	public String getCycleInfo() {
		StringBuilder cadena = new StringBuilder();
		cadena.append("Life: " + ucmShip.getHealth() + System.lineSeparator());
		cadena.append("Number of cycles: " + cycles + System.lineSeparator());
		cadena.append("Points: " + points + System.lineSeparator());
		cadena.append("Remaining aliens: " + (regularShipList.getNumRegularShips()+destroyerShipList.getNumDestroyerShips())+ System.lineSeparator());
		cadena.append("ShockWave: " + Integer.toString(shockWave));
		return cadena.toString();
	}
	
	public String toStringObjectAt(int i, int j) {
		String position = "";
		
		RegularShip regularShip = regularShipList.findRegularShipAt(i, j);
		if (regularShip != null) {
			return regularShip.toString();
		}
		
		DestroyerShip destroyerShip = destroyerShipList.findDestroyerShipAt(i, j);
		if (destroyerShip != null) {
			return destroyerShip.toString();
		}
		
		Bomb bomb = bombList.findBombAt(i, j);
		if (bomb != null) {
			return bomb.toString();
		}
		
		if (ucmShip.findUCMShipAt(i, j)) {
			return ucmShip.toString();
		}
		
		if (ucmShip.getLaser() != null && ucmShip.getLaser().findLaserAt(i, j)) {
			return ucmShip.getLaser().toString();
		}
		
		if (ovni != null && ovni.findOvniAt(i, j)) {	
			return ovni.toString();
		}
		
		return position;
	}

	public void computerAction() {
		destroyerShipList.decideShots();
		destroyerShipShots();
		if (ovni == null && canAddOvni()) {
			ovni = new Ovni(this);
		}
	}

	private void destroyerShipShots() {
		for(int i = 0; i < destroyerShipList.getNumDestroyerShips();++i) {
			if (destroyerShipList.getDestroyerShipFromList(i).getWillShoot()) 
			{	
				Bomb bomb = new Bomb(destroyerShipList.getDestroyerShipFromList(i).getX(), destroyerShipList.getDestroyerShipFromList(i).getY(), this);
				bombList.addBomb(bomb);
				destroyerShipList.getDestroyerShipFromList(i).setBomb(bomb);
				destroyerShipList.getDestroyerShipFromList(i).setWillShoot(false);
			}		
		}	
	}

	public boolean canAddOvni() {
		float numRandom;			
		boolean added = false;
		numRandom = this.rand.nextInt(10); 
		
		if (numRandom/10 <= this.level.getOvniFrequency()) {
			added = true; 
		}	
		return added;
	}

	public void removeRegularShip(int x, int y) {
		regularShipList.eraseRegularShip(regularShipList.findRegularShipAt(x, y));
	}

	public void removeDestroyerShip(int x, int y) {
		destroyerShipList.eraseDestroyerShip(destroyerShipList.findDestroyerShipAt(x, y));	
	}
	
	
	public void removeBombFromList(Bomb bomb) {
		bombList.eraseBomb(bomb);
	}

	public void eraseBombOfDestroyer(Bomb bomb) {
		boolean find = false;
		int i=0;
		while(!find && i < destroyerShipList.getNumDestroyerShips()) {
			if(destroyerShipList.getDestroyerShipFromList(i).getBomb() == bomb) {
				destroyerShipList.getDestroyerShipFromList(i).setBomb(null);	
				find = true;
			}
			else {
				i++;
			}
		}
	}

	public void removeOvni() {
		this.ovni = null;
	}

	public boolean searchInPosition(int x, int y) {
		boolean found = false;
		
		if (ucmShip.findUCMShipAt(x, y)) {
			found = true;
		} 
		else if (ucmShip.getLaser() != null && ucmShip.getLaser().findLaserAt(x, y)) {
			found=true;
		}
		else if (regularShipList.findRegularShipAt(x, y) != null) {
			found =true;
		}
		else if(destroyerShipList.findDestroyerShipAt(x, y) != null) {
			found =true;
		}
		else if(bombList.findBombAt(x, y) != null) {
			found =true;
		}
		else if(ovni != null && ovni.findOvniAt(x, y)) {
			found=true;
		}
		
		return found;
	}

	public void damageToEnemy(int x, int y) {
		DestroyerShip destroyerShip = destroyerShipList.findDestroyerShipAt(x, y);
		RegularShip regularShip = regularShipList.findRegularShipAt(x, y);
		Bomb bomb = bombList.findBombAt(x,y);
		
		if (destroyerShip != null) {
			destroyerShip.takeDamage(UCMShipLaser.DAMAGE);
		} 
		else if (regularShip != null) {
			regularShip.takeDamage(UCMShipLaser.DAMAGE);
		} 
		else if (bomb != null) {
			removeBombFromList(bomb);
		}
		else if(ovni != null) {	
			ovni.takeDamage(UCMShipLaser.DAMAGE);
			if(shockWave==0)
				++shockWave;
		}
	}

	public void damageFromEnemy(int x, int y) {
		if (ucmShip.getLaser() != null)
			ucmShip.eraseLaser();
		else if (ucmShip.findUCMShipAt(x, y))
			ucmShip.takeDamage(DestroyerShip.DAMAGE);
	}

	public boolean isLaserColliding(int x, int y) {
		return !ucmShip.canShoot() && ucmShip.getLaser().findLaserAt(x, y);
	}

	public void removeUCMLaser() {
		ucmShip.eraseLaser();
}
	
}

package tp.p1.logic.objects;

import tp.p1.logic.Game;

public class UCMShipLaser {
	public final static int DAMAGE = 1;
	private int x, y;
	private Game game;
	
	public UCMShipLaser(int x, int y,Game game) {
		this.x = x;
		this.y = y;
		this.game = game;
	}
	
	//Reposiciona el objecto y ataca a naves o bombas
	public boolean update() { //Devuelve true si tiene que eliminar el laser
		boolean erase = false;
		if (game.searchInPosition(x-1, y)) {
			game.damageToEnemy(x-1, y);
			erase=true;
		}
		else if (move()) {
			erase = true;
		}

		return erase;
	}
	
	private boolean move() {
		boolean end = false;
		x = x-1;
		if (x < 0) {
			end = true;
		}
		return end;
		
	}
	
	public boolean findLaserAt(int x, int y) {
		return (this.x == x && this.y == y);
	}
	
	public String toString() {
		return "oo";
	}
}

package tp.p1.logic.objects;

import tp.p1.logic.Game;

public class Bomb {
	private int x, y;
	private Game game;
	
	public Bomb(int x, int y, Game game) {
		this.x = x;
		this.y = y;
		this.game = game;
	}
	//-------------------------------------------//

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	//-------------------------------------------//
	
	//Reposiciona el objecto y ataca a la UCM
	public boolean update() {
		boolean erase = false;
		if (game.searchInPosition(x+1, y)) {
			game.damageFromEnemy(x+1, y);
			erase = true;
		}
		else {
			erase = move();
		}
		return erase;
	}

	public boolean move() {
		boolean lastRow = false;	
		x++;
		if( x >= Game.NUM_ROWS) {
			System.out.println("\nBomb reached the end. Proceeding to remove Bomb.\n");
			lastRow = true;
		}
		return lastRow;
	}
	
	public String toString() {
		return ".";
	}

}

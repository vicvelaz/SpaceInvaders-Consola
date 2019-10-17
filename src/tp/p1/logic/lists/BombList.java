package tp.p1.logic.lists;

import tp.p1.logic.Game;
import tp.p1.logic.objects.Bomb;

public class BombList {
	private Bomb[] bombList;
	private int numBombs;
	private Game game;
	
	public BombList(int maxBombs,  Game game) {
		bombList = new Bomb[maxBombs];
		numBombs = 0;
		this.game = game;
	}
	
	public void addBomb(Bomb bomb){
			bombList[numBombs] = bomb;
			numBombs++;
	}
	
	public Bomb findBombAt(int x, int y) {
		for (int i = 0; i < numBombs; i++) {
			if (bombList[i] != null ) {
				if (bombList[i].getX() == x && bombList[i].getY() == y) {
					return bombList[i];
				}
			}
		}
		
		return null;
	}
	
	private void sortList(int index) {
		for(int i = index; i < numBombs; i++){
			bombList[i] = bombList[i+1];
		}
	}
	
	public boolean removeBomb(int index) {
		
		if (bombList[index] != null) {
			bombList[index] = null;
			
			sortList(index);
			numBombs--;
			return true;
		}
		
		return false;
	}
	

	private void sortBombList(int position)
	{
		for(int i = position; i < numBombs; ++i) {
			bombList[i] = bombList[i+1];
		}
	}
	
	public void eraseBomb(Bomb bomb)
	{
		int i = 0;
		boolean found = false;
		while (!found && i < numBombs)
		{
			if(bombList[i] == bomb) {
				found = true;
				game.eraseBombOfDestroyer(bombList[i]); //Elimina bomba asignada al destroyer
				bombList[i] = null;
			}
			else {
				++i;
			}
		}
		
		--numBombs;
		sortBombList(i);
		
	}

	public void update() {
		for (int i = 0; i < numBombs; i++) {
			Bomb bomb = bombList[i];
			
			if (bomb != null ) {
				if(bomb.update()) {
					eraseBomb(bomb);
				} 

			}
		}
	}
	
}

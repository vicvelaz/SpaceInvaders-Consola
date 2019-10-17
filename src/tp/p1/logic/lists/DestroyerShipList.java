package tp.p1.logic.lists;

import tp.p1.logic.Game;
import tp.p1.logic.Move;
import tp.p1.logic.objects.DestroyerShip;



public class DestroyerShipList {
	private DestroyerShip[] destroyerShipList;
	private int numDestroyerShips;
	private Game game;
	
	public DestroyerShipList(int iniDestroyerShips,  Game game)	{
		destroyerShipList = new DestroyerShip[iniDestroyerShips];
		numDestroyerShips = 0;
		this.game = game;
	}
	
	public boolean addDestroyerShip(DestroyerShip destroyerShip){
		if (numDestroyerShips < destroyerShipList.length) {
			destroyerShipList[numDestroyerShips] = destroyerShip;
			numDestroyerShips++;
			return true;
		}
		return false;
	}
	
	public DestroyerShip findDestroyerShipAt(int x, int y) {
		DestroyerShip foundDestroyerShip = null;
		for (int i = 0; i < numDestroyerShips; i++) {
			if (destroyerShipList[i] != null ) {
				if (destroyerShipList[i].getX() == x && destroyerShipList[i].getY() == y) {
					foundDestroyerShip = destroyerShipList[i];
				}
			}
		}
		
		return foundDestroyerShip;
	}
	
	private void sortList(int num) { //Da error al tirar la shockwave
		for (int i = num; i < numDestroyerShips-1; i++){
			destroyerShipList[i] = destroyerShipList[i+1];
		}
	}
	
	public void eraseDestroyerShip(DestroyerShip destroyerShip) {
		boolean found = false;
		int i = 0;
		while (!found && i < numDestroyerShips) {
			found = (destroyerShip == destroyerShipList[i]);
			if(!found)
				++i;
		}
		
		if (found) {
			sortList(i);
			--numDestroyerShips;
		}
	}

	public int getNumDestroyerShips() {
		return numDestroyerShips;
	}

	public DestroyerShip getDestroyerShipFromList(int i) {
		return destroyerShipList[i];
	}
	
	public void placeDestroyerShips(int numDestroyers, int X) {
		int x = X;
		int y;

		if (numDestroyers == 4) {
			y = 3;
		} else {
			y = 4;
		}
		
		for (int i = numDestroyers; i > 0; --i) {
			addDestroyerShip(new DestroyerShip(x, y, game));
			++y;
		}
	}

	public boolean canSwitchRow() {
		boolean canSwitch = false;
		int i = 0;
		while(!canSwitch && i < numDestroyerShips) {
			if (getDestroyerShipFromList(i).getY() == 0 && game.getDirection() == Move.LEFT)
				canSwitch = true;
			else if (getDestroyerShipFromList(i).getY() == Game.NUM_COLUMNS-1 && game.getDirection() == Move.RIGHT)
				canSwitch = true;
			else
				++i;
		}
		return canSwitch;
	}
	
	public boolean update(Move nextDirection) {
		boolean lastRow = false;
		for (int i = 0; i < numDestroyerShips; ++i) {
			if (destroyerShipList[i].update(nextDirection)) {
				lastRow = true;
			}
		}
		return lastRow;
	}

	public void decideShots() {
		for (int i = 0; i < numDestroyerShips; ++i) {
			DestroyerShip destroyerShip = destroyerShipList[i];
			if (destroyerShip.getBomb() == null && destroyerShip.canDestroyerShoot()) {
				destroyerShip.setWillShoot(true);
			}
		}
		System.out.println("");
	}
}

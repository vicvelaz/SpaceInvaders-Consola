package tp.p1.logic.lists;


import tp.p1.logic.Game;
import tp.p1.logic.Move;
import tp.p1.logic.objects.RegularShip;

public class RegularShipList {
	private RegularShip[] regularShipList;
	private int numRegularShips;
	private Game game;
	
	public RegularShipList(int maxRegularShips, Game game)
	{
		regularShipList = new RegularShip[maxRegularShips];
		numRegularShips = 0;
		this.game = game;
	}
	
	public int getNumRegularShips() {
		return this.numRegularShips;
	}
	
	public boolean addRegularShip(RegularShip regularShip){
		if (numRegularShips < regularShipList.length) {
			regularShipList[numRegularShips] = regularShip;
			numRegularShips++;
			return true;
		}
		return false;
	}
	
	public RegularShip findRegularShipAt(int x, int y) {
		for (int i = 0; i < numRegularShips; i++) {
			if (regularShipList[i] != null) {
				if (regularShipList[i].getX() == x && regularShipList[i].getY() == y) {
					return regularShipList[i];
				}
			}
		}
		return null;
	}
	
	private void sortList(int index) {
		for(int i = index; i < numRegularShips-1; i++) {
			if (regularShipList[i+1] != null) {
				regularShipList[i] = regularShipList[i+1];
			}
		}
	}
	
	public void eraseRegularShip(RegularShip regularShip) {
		boolean found = false;
		int i = 0;
		while (!found && i < numRegularShips) {
			found = (regularShip == regularShipList[i]);
			if(!found)
				++i;
		}
		
		if (found) {
			sortList(i);
			--numRegularShips;
		}
	}
	
	public RegularShip getRegularShipFromList(int i) {
		return regularShipList[i];
	}

	public int placeRegularShips(int numRegulars, int X) {
		int x = X;
		while (numRegulars > 1) {
			for (int y = 3; y < 7; ++y) {
				addRegularShip(new RegularShip(x,  y, game));
				numRegulars--;
			}
			++x;
		}
		
		return x;
	}
	
	public boolean canSwitchRow() {
		boolean canSwitch = false;
		int i = 0;
		while(!canSwitch && i < getNumRegularShips()) {
			if (getRegularShipFromList(i).getY() == 0 && game.getDirection() == Move.LEFT)
				canSwitch = true;
			else if (getRegularShipFromList(i).getY() == Game.NUM_COLUMNS-1 && game.getDirection() == Move.RIGHT)
				canSwitch = true;
			else
				++i;
		}
		
		return canSwitch;
	}
	
	public boolean update(Move nextDirection) {
		boolean lastRow = false;
		for (int i = 0; i < numRegularShips; ++i) {
			if (regularShipList[i].update(nextDirection)) {
				lastRow = true;
			}
		}
		return lastRow;
	}
}

package tp.p1.logic;

public enum Level {
	EASY(4,2,0.1,3,0.5), HARD(8,2,0.3,2,0.2), INSANE(8,4,0.5,1,0.1);
	
	private int numRegularShips, numDestroyerShips, speed;
	private double shootingFrequency, ovniFrequency;
	
	private Level(int numRegularShips, int numDestroyerShips, double shootingFrequency, int speed, double ovniFrequency)  {
		this.numRegularShips = numRegularShips;
		this.numDestroyerShips = numDestroyerShips;
		this.shootingFrequency = shootingFrequency;
		this.speed = speed;
		this.ovniFrequency = ovniFrequency;
	}
	
	public int getNumRegularShips() {
		return numRegularShips;
	}
	
	public int getNumDestroyerShips() {
		return numDestroyerShips;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public double getShootFrequency() {
		return shootingFrequency;
	}
	
	public double getOvniFrequency() {
		return ovniFrequency;
	}

}

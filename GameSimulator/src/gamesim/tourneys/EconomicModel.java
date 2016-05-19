package gamesim.tourneys;

import gamesim.GameThread;
import gamesim.GameTourney;

/**TODO
 * @author DaJay42
 *
 */
public class EconomicModel extends GameTourney {

	int generations = 50;
	GameTourney model;
	
	public EconomicModel(int generations, GameTourney model){
		this.generations = generations;
		this.model = model;
	}
	
	@Override
	public boolean isFinished() {
		return generations == 0;
	}

	@Override
	public int getRoundsPerGame() {
		return model.getRoundsPerGame();
	}

	@Override
	public String getRoundsDescriptor() {
		return "generations of " + model.getClass().getSimpleName()+ " (with " + model.getRoundsPerGame()+" "+model.getRoundsDescriptor() +" each)";
	}

	@Override
	public GameThread[] getNextMatchUp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void evaluate(GameThread[] workers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] printResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

}

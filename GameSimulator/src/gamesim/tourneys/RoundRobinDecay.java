package gamesim.tourneys;

import gamesim.tourneys.RoundRobinFixed;

/**Round Robin Tournament, but with a random number of rounds.
 * The number of rounds is exponential-decay-distributed,
 * and can have a chosen median (default 200).
 * As played in Axelrod's second tournament.
 * @author DaJay42
 *
 * @see RoundRobinFixed
 */
public class RoundRobinDecay extends RoundRobinFixed {

	double w = 0.9975;
	
	
	/**
	 * @param median The median of the distribution of the number of rounds each game.
	 */
	public RoundRobinDecay(int median) {
		super(median);
		
		w = 1 - Math.log(2)/median;
	}

	@Override
	public int getRoundsPerGame(){
		int i;
		
		for(i = 0; Math.random() < w; i++){}
		
		return i;
	}
	

	@Override
	public String getRoundsDescriptor() {
		return "rounds (median)";
	}
}

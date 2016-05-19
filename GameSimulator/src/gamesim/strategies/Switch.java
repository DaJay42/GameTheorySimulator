package gamesim.strategies;

import gamesim.GameStrategy;

/**A relatively simple strategy devised by me, 2016.
 * I'm not even sure why it does actually kind of  well.
 * @author DaJay42
 *
 */
public class Switch implements GameStrategy {
	
	Strategy last = Strategy.C;
	boolean cautious = false;

	@Override
	public Strategy first() {
		return Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D ){
			if(last == Strategy.D){
				cautious = false;
			}else{
				cautious = !cautious;
			}
		}
		last = Answer;
		
		return cautious ? Strategy.C : Strategy.D;
	}

}

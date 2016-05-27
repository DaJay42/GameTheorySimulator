package gamesim.strategies;

import gamesim.GameStrategy;

/**Same strategy as Switch, but friendly. (starts with C)
 * @author DaJay42
 * @see Switch
 */
public class CSwitch implements GameStrategy {
	
	Strategy last = Strategy.C;
	boolean cautious = true;

	@Override
	public Strategy first() {
		return Strategy.C;
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

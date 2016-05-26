package gamesim.strategies;

import gamesim.GameStrategy;

/**A modification of the Downing strategy, that plays C the first two rounds.
 * @author DaJay42
 * @see Downing
 */
public class CDowning implements GameStrategy {

	int cForC;
	int dForC;
	int cForD;
	int dForD;
	
	float p1;
	float p2;
	
	boolean second = true;
	
	Strategy last;
	Strategy prelast;
	
	@Override
	public Strategy first() {
		last = Strategy.C;
		return last;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(second){
			second = false;
			prelast = last;
			last = Strategy.C;
			return last;
		}
		if(Answer == Strategy.D){
			if(prelast == Strategy.D){
				dForD++;
			}else{
				dForC++;
			}
		}else{
			if(prelast == Strategy.D){
				cForD++;
			}else{
				cForC++;
			}
		}
		
		prelast = last;
		
		p1 = (cForC+dForC > 0) ? cForC/(float)(cForC+dForC) : 1;
		p2 = (cForD+dForD > 0) ? cForD/(float)(cForD+dForD) : 0;
		
		if(p1 < 0.5 || p2 > 0.5){
			last = Strategy.D;
		}else{
			last = Strategy.C;
		}
		
		return last;
	}

}
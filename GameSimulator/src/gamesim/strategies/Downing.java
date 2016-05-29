package gamesim.strategies;

import gamesim.GameStrategy;

/**A strategy by Leslie Downing, as seen in Axelrod's first tournament.
 *<br>A rather complex strategy that tries to estimate the behavior of the opponent
 * based on their answers. Unfriendly.
 * <p>Does surprisingly well in environments with very faulty players.
 * @author DaJay42
 *
 */
public class Downing implements GameStrategy {

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
		last = Strategy.D;
		return last;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(second){
			second = false;
			prelast = last;
			last = Strategy.D;
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

package gamesim.strategies;

import gamesim.GameStrategy;

/**An aggressively-friendly modification of the Downing strategy, by me.
 *<br>Plays C the first two rounds, and plays D only if both C and D are answered
 * predominantly with D.
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
	public Strategy next(Strategy answer) {
		if(second){
			second = false;
			prelast = last;
			last = Strategy.C;
			return last;
		}
		if(answer == Strategy.D){
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
		
		p1 = (cForC+dForC > 0) ? cForC/(float)(cForC+dForC) : 0;
		p2 = (cForD+dForD > 0) ? cForD/(float)(cForD+dForD) : 0;
		
		if(p1 > 0.5 || p2 > 0.5){
			last = Strategy.C;
		}else{
			last = Strategy.D;
		}
		
		return last;
	}

}
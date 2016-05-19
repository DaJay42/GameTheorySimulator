package gamesim.strategies;

import gamesim.GameStrategy;

/**A strategy by Leslie Downing, as seen in Axelrod's first tournament.
 * A rather complex strategy that tries to estimate the behavior of the opponent
 * based on their answers.
 * @author DaJay42
 *
 */
public class Downing implements GameStrategy {

	int round = 1;
	
	int cForC;
	int dForC;
	int cForD;
	int dForD;
	
	float p1;
	float p2;
	
	Strategy last;
	
	@Override
	public Strategy first() {
		round = 2;
		last = Strategy.D;
		return last;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D){
			if(last == Strategy.D){
				dForD++;
			}else{
				dForC++;
			}
		}else{
			if(last == Strategy.D){
				cForD++;
			}else{
				cForC++;
			}
		}
		
		switch(round){
		case 2:
			last = Strategy.D;
			break;
		case 3:
		case 4:
			last = Strategy.C;
			break;
		default:
			p1 = cForC/(float)(cForC+dForC);
			p2 = cForD/(float)(cForD+dForD);
			if(p1 < 0.5 || p2 > 0.5){
				last = Strategy.D;
			}else{
				last = Strategy.C;
			}
		}
		
		round++;
		return last;
	}

}

package gamesim;

import gamesim.GameStrategy.Strategy;

/**Has two players play out a chosen number of rounds
 * of a chosen GameType. Does actually provide multi-threading.
 * @author DaJay42
 *
 */
public class GameThread implements Runnable{
	GameRuleSet rules;
	public GamePlayer A;
	public GamePlayer B;
	int rounds;
	
	
	/**
	 * @param type The GameType to play
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @param length How many rounds?
	 */
	public GameThread(GameRuleSet type, GamePlayer p1, GamePlayer p2, int length){
		rules = type;
		A = p1;
		B = p2;
		rounds = length;
	}
	
	/**Actually executes the games.
	 * 
	 */
	public void run(){
		Strategy l, r, t1, t2;
		
		l = A.first();		
		r = B.first();
		
		for(int i = 0; i < rounds; i++){
			A.score += rules.eval(l, r);
			B.score += rules.eval(r, l);
			
			t1 = l;
			t2 = r;
			
			l = A.next(t2);
			r = B.next(t1);
		}
	}
	
}

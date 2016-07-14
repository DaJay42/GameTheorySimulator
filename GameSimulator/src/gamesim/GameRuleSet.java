package gamesim;

import gamesim.GameStrategy.Strategy;

/** Defines the pay-out matrix to be used in evaluating players' actions.
 *<br>It is suggested to use small ints to prevent overflows.
 *  @author DaJay42
 *  
 */
public abstract class GameRuleSet extends GameEntity{
	/**
	 * @return "Reward": The pay-out awarded if both players chose C.
	 */
	public abstract int R();
	/**
	 * @return "Sucker's pay-out": The pay-out awarded to a player who chose C
	 * while their opponent chose D.
	 */
	public abstract int S();
	/**
	 * @return "Temptation": The pay-out awarded to a player who chose D
	 * while their opponent chose C.
	 */
	public abstract int T();
	/**
	 * @return "Punishment": The pay-out awarded if both players chose D.
	 */
	public abstract int P();
	
	/**Awards points based on the strategies chosen,
	 * as defined by the GameType.
	 * @param me The player's Strategy
	 * @param other The opponent's Strategy.
	 * @return The pay-out.
	 */
	int eval(Strategy me, Strategy other){
		switch(me){
		case D:
			switch(other){
			case D:
				return P();
			case C:
				return T();
			}
		case C:
			switch(other){
			case D:
				return S();
			case C:
				return R();
			}
		}
		
		//failsafe. should never be reached.
		//...but then, that's what they always say.
		return Integer.MIN_VALUE;
	}
	
}

package gamesim;

/**
 * Implement this interface to create a Strategy.
 *<br>Strategies MUST be fully constructible by the zero-argument constructor,
 * as only that will be called.
 * @author DaJay42
 */
public interface GameStrategy {
	
	/**The two options possible have every round.
	 * 'C'ooperate, or 'D'efect.
	 */
	public static enum Strategy{
		/**Cooperation*/C,
		/**Defection*/D;
		/**Turns C into D and vice-versa.
		 * @return not this */
		public Strategy invert(){
			return (this == C) ? D : C;
		}
	}
	
	/**
	 * Called in the first round.
	 * Chooses a Strategy to start the game with.
	 */
	public abstract Strategy first();
	
	/**
	 * Called in subsequent rounds.
	 * Chooses a Strategy to continue the game with.
	 * @param answer The Strategy the opponent used last round.
	 */
	public abstract Strategy next(Strategy answer);
}

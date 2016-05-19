package gamesim;

/**
 * Implement this interface to create a Strategy.
 * Strategies MUST be constructed ONLY by the zero-argument constructor.
 * @author DaJay42
 */
public interface GameStrategy {
	
	/**
	 * The two options you have every round.
	 * 'C'ooperate, or 'D'efect.
	 */
	public static enum Strategy {C, D};
	
	/**
	 * Called in the first round.
	 * Choose a Strategy to start the game with.
	 */
	public abstract Strategy first();
	
	/**
	 * Called in subsequent rounds.
	 * Choose a Strategy to continue the game with.
	 * @param Answer The Strategy your opponent used last round.
	 */
	public abstract Strategy next(Strategy Answer);
}

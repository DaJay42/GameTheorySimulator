package gamesim.strategies;

import gamesim.GameStrategy;

/**The Tit-For-Tat Strategy, as by Anatol Rapoport, 1984 in Axelrod's first tournament.
 *<br>Starts with C, then answers with whatever the opponent played.
 *<br>Friendly, forgiving, non-exploitable and simple, it might just be the best know strategy.
 * @author DaJay42
 *
 */
public class TitForTat implements GameStrategy {

	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return Answer;
	}

}

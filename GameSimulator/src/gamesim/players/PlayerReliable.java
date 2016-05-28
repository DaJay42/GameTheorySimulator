package gamesim.players;

import gamesim.GamePlayer;
import gamesim.GameStrategy;

public class PlayerReliable extends GamePlayer {

	public PlayerReliable(Class<? extends GameStrategy> s, String... args)
			throws InstantiationException, IllegalAccessException {
		super(s, args);
	}

	@Override
	public String getName() {
		return PlayerReliable.class.getSimpleName();
	}

}

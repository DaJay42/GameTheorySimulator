package gamesim.players;

import gamesim.GamePlayer;
import gamesim.GameStrategy;
import gamesim.GameStrategy.Strategy;

public class PlayerFaulty extends GamePlayer {

	double faultChance = 0.05;
	
	public PlayerFaulty(Class<? extends GameStrategy> s, String... args)
			throws InstantiationException, IllegalAccessException {
		super(s, args);

		if(args != null && args.length > 0)
			faultChance = Double.parseDouble(args[0]);
		
		faultChance = Double.max(0, Double.min(1, faultChance));
	}

	@Override
	public Strategy next(Strategy answer) {
		Strategy s = myStrategy.next(answer);
		return (Math.random() < faultChance) ? s.invert() : s;
	}
	
	@Override
	public Strategy first() {
		Strategy s = myStrategy.first();
		return (Math.random() < faultChance) ? s.invert() : s;
	}

	@Override
	public String getName() {
		return PlayerFaulty.class.getSimpleName() + " ("+100*faultChance+"% fault chance)";
	}
}

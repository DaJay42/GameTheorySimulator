package gamesim;

import java.util.ArrayList;

/**GameSim execution model.
 * 
 * @author DaJay42
 */
public class GameRunner {
	
	GameTourney Tournament;
	GameThread[] workers;
	GameStrategy[] strats;
	String[] results;
	
	
	
	public GameRunner(GameTourney Tourney, GameType rules, GameStrategy[] strategies) throws InstantiationException, IllegalAccessException{		
		Tournament = Tourney;
		strats = strategies;
		Tournament.ruleset = rules;
		
		Tournament.players = new ArrayList<GamePlayer>();
		
		for(GameStrategy s : strats){
			Tournament.players.add(new GamePlayer(s));
		}
		
	}
	
	public void run() throws InstantiationException, IllegalAccessException{
		Tournament.setup();
		
		System.out.println("Tournament " + Tournament.getClass().getSimpleName()
					+ " over " + Tournament.getRoundsPerGame() + " " + Tournament.getRoundsDescriptor()
					+ " with rules "+ Tournament.ruleset.getClass().getSimpleName()
					+ " and " + strats.length + " players.");
		
		String text = "Players: ";
		for(int i = 0; i < strats.length; i++){
			text = text + i+": "+ strats[i].getClass().getSimpleName()+", ";
			if(i % 5 == 4){
				text = text + "\n";
			}
		}
		System.out.println(text);
		System.out.println("-----");
		
		
		while(!Tournament.isFinished()){
			workers = Tournament.getNextMatchUp();
			
			for(GameThread worker : workers){
				if(worker != null){
					worker.run();
				}
			}
			
			Tournament.evaluate(workers);
			Tournament.resetPlayers();
		}
		
		
		
		results = Tournament.printResults();
		
		for(String line : results){
			System.out.println(line);
		}
	}
	
	
}

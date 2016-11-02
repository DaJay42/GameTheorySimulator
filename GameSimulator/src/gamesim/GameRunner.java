package gamesim;

import java.util.*;
import java.util.concurrent.*;

/**GameSim execution model.
 *<br>Will use parallelism if DO_THREADING in GameMain is set.
 * @author DaJay42
 */
public class GameRunner{
	
	GameTourney Tournament;
	GameThread[] workers;
	ArrayList<Class<? extends GameStrategy>> strats;
	String[][] results;
	ExecutorService pool;
	
	
	public GameRunner(Class<? extends GameRuleSet> rules,
			Class<? extends GamePlayer> player, String[] playerArgs,
			ArrayList<Class<? extends GameStrategy>> strategies,
			Class<? extends GameTourney> tourney, String...tourneyArgs)
					throws InstantiationException{
		
		Tournament = (GameTourney) GameEntityReflector.createWithArgs(tourney, tourneyArgs);
		Tournament.ruleset = (GameRuleSet) GameEntityReflector.create(rules);
		Tournament.players = new ArrayList<GamePlayer>();
		
		strats = strategies;
		for(Class<? extends GameStrategy> s : strats){
			Tournament.players.add(GameEntityReflector.createPlayer(player, s, playerArgs));
		}
		
		if(GameMain.DO_THREADING){
			int processors = Runtime.getRuntime().availableProcessors();
			pool = Executors.newScheduledThreadPool(processors);
		}
	}
	
	public void run() throws InterruptedException, ExecutionException, InstantiationException{
		
		GameMain.printInfo("Tournament " + Tournament.getClass().getSimpleName()
				+ " over " + Tournament.getRoundsDescriptor()
				+ " with rules "+ Tournament.ruleset.getClass().getSimpleName()
				+ " between " + strats.size()
				+ " " + Tournament.players.get(0).getName()
				+ ".");
		
		
		String text = "Players: ";
		for(int i = 0; i < strats.size(); i++){
			text = text + i+": "+ strats.get(i).getSimpleName()+", ";
			if(i % 5 == 4){
				text = text + "\n";
			}
		}
		GameMain.printInfo(text);

		GameMain.printInfo("-----");

		Tournament.setup();
		
		while(!Tournament.isFinished()){
			workers = Tournament.getNextMatchUp();

			if(GameMain.DO_THREADING){
				ArrayList<Callable<Object>> workList = new ArrayList<Callable<Object>>();
				List<Future<Object>> futureList;
				
				for(GameThread worker : workers){
					if(worker != null){
						workList.add(Executors.callable(worker));
					}
				}
				
				futureList = pool.invokeAll(workList);
				
				for(Future<Object> f : futureList){
					f.get();
				}
			}else{
				for(GameThread worker : workers){
					if(worker != null){
						worker.run();
					}
				}
			}
			Tournament.evaluate(workers);
			Tournament.resetPlayers();
		}
		
		if(GameMain.DO_THREADING){
			pool.shutdown();
		}

		GameMain.printInfo("-----");
		
		results = Tournament.printResults();
		
		for(String[] result : results){
			for(String line : result){
				GameMain.printResult(line);
			}
		}
	}
	
	
}

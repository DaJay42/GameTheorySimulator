package gamesim;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
	
	
	public GameRunner(Class<? extends GameRuleSet> rules, Class<? extends GamePlayer> player, String[] playerArgs, ArrayList<Class<? extends GameStrategy>> strategies, Class<? extends GameTourney> tourney, String...tourneyArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
		for(Constructor<?> c : tourney.getConstructors()){
			if(c.getParameterTypes().length == 1 && c.getParameterTypes()[0] == String[].class){
				Object[] o = {tourneyArgs};
				this.Tournament = (GameTourney) c.newInstance(o);
				break;
			}
		}
		if(Tournament == null){
			throw new InstantiationException("No suitable constructor found for class "+tourney.getName()+" with args "+tourneyArgs.toString());
		}
		
		Constructor<?> playerCon = null;
		for(Constructor<?> c : player.getConstructors()){
			if(c.getParameterTypes().length == 2 && c.getParameterTypes()[1] == String[].class){
				playerCon = c;
				break;
			}
		}
		if(playerCon == null){
			throw new InstantiationException("No suitable constructor found for class "+player.getName()+" with args "+playerArgs.toString());
		}
		
		strats = strategies;
		
		Tournament.ruleset = rules.newInstance();
		
		Tournament.players = new ArrayList<GamePlayer>();
		
		for(Class<? extends GameStrategy> s : strats){
			Tournament.players.add((GamePlayer) playerCon.newInstance(new Object[]{s, playerArgs}));
		}
		
		if(GameMain.DO_THREADING)
			pool = Executors.newCachedThreadPool();
	}
	
	public void run() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ExecutionException, InterruptedException{
		
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
		

		GameMain.printInfo("-----");
		
		results = Tournament.printResults();
		
		for(String[] result : results){
			for(String line : result){
				GameMain.printResult(line);
			}
		}
	}
	
	
}

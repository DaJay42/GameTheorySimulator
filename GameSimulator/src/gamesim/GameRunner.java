package gamesim;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**GameSim execution model.
 * 
 * @author DaJay42
 */
public class GameRunner /*implements Runnable*/ {
	
	GameTourney Tournament;
	GameThread[] workers;
	ArrayList<Class<? extends GameStrategy>> strats;
	String[][] results;
	
	
	
	public GameRunner(Class<? extends GameRuleSet> rules,ArrayList<Class<? extends GameStrategy>> strategies, Class<? extends GameTourney> tourney, String...tourneyArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
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
		
		strats = strategies;
		
		Tournament.ruleset = rules.newInstance();
		
		Tournament.players = new ArrayList<GamePlayer>();
		
		for(Class<? extends GameStrategy> s : strats){
			Tournament.players.add(new GamePlayer(s));
		}
		
	}
	
	public void run() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		GameMain.out.println("Tournament " + Tournament.getClass().getSimpleName()
				+ " over " + Tournament.getRoundsDescriptor()
				+ " with rules "+ Tournament.ruleset.getClass().getSimpleName()
				+ " and " + strats.size() + " players.");
		
		
		String text = "Players: ";
		for(int i = 0; i < strats.size(); i++){
			text = text + i+": "+ strats.get(i).getSimpleName()+", ";
			if(i % 5 == 4){
				text = text + "\n";
			}
		}
		GameMain.out.println(text);

		GameMain.out.println("-----");

		Tournament.setup();
		
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
		

		GameMain.out.println("-----");
		
		results = Tournament.printResults();
		
		for(String[] result : results){
			for(String line : result){
				GameMain.out.println(line);
			}
		}
	}
	
	
}

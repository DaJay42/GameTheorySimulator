package gamesim.tourneys;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import gamesim.GameMain;
import gamesim.GamePlayer;
import gamesim.GameThread;
import gamesim.GameTourney;
import gamesim.util.ScoreTuple;

/**TODO
 * @author DaJay42
 *
 */
public class EconomicModel extends GameTourney {

	int generations = 50;
	GameTourney model;
	Class<? extends GameTourney> modelClass = RoundRobinDecay.class;
	String[] arg0 = {};
	int playercount;
	int internalPlayers = 500;
	double[][] pointHistory;
	int[][] playerHistory;
	int turn = 0;
	
	public EconomicModel(String...args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		super(args);
		if(args.length > 3)
			arg0 = new String[args.length-3];
		for(int i = 0; i < args.length; i++){
			switch(i){
			case 0:
				generations = Integer.parseInt(args[i]);
				break;
			case 1:
				internalPlayers = Integer.parseInt(args[i]);
				break;
			case 2:
				modelClass = Class.forName(args[i]).asSubclass(GameTourney.class);
				break;
			default:
				arg0[i-3] = args[i];
			}
		}
		
		for(Constructor<?> c : modelClass.getConstructors()){
			if(c.getParameterTypes().length == 1 && c.getParameterTypes()[0] == String[].class){
				Object[] o = {arg0};
				model = (GameTourney) c.newInstance(o);
				break;
			}
		}
	}
	
	@Override
	public boolean isFinished() {
		for(int i = 0; i < playerHistory.length; i++){
			if(playerHistory[i][turn] == internalPlayers){
				GameMain.out.println("EconomicModel terminated after "+ turn + " Generations due to domineering strategy "+players.get(i).name+".");
				return true;
			}
		}
		return (generations - 1 == turn) && model.isFinished();
	}

	@Override
	public int getRoundsPerGame() {
		return generations;
	}

	@Override
	public void resetPlayers() throws InstantiationException, IllegalAccessException{
		super.resetPlayers();
		model.resetPlayers();
	}
	
	@Override
	public String getRoundsDescriptor() {
		return generations + " generations of " +model.getClass().getSimpleName()+ " (with "+model.getRoundsDescriptor() +" each)";
	}

	@Override
	public GameThread[] getNextMatchUp() throws IllegalArgumentException, InvocationTargetException, InstantiationException, IllegalAccessException {
		if(model.isFinished()){
//			if(turn < 3){
//				String[][] results;
//				results = model.printResults();
//				
//				for(String[] result : results){
//					for(String line : result){
//						GameMain.out.println(line);
//					}
//				}
//			}
			double total = 0;
			for(int i = 0; i < playercount; i++){
				total += pointHistory[i][turn];
			}
			double scale = internalPlayers/total;
			
			for(int i = 0; i < playercount; i++){
				playerHistory[i][turn+1] = (int)(scale*pointHistory[i][turn]);
			}
			
			try {
				if(arg0 == null)
					model = model.getClass().newInstance();
				else{
					for(Constructor<?> c : model.getClass().getConstructors()){
						if(c.getParameterTypes().length == 1 && c.getParameterTypes()[0] == String[].class){
							Object[] o = {arg0};
							this.model = (GameTourney) c.newInstance(o);
							break;
						}
					}
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			model.players = new ArrayList<GamePlayer>();
			model.ruleset = ruleset;
			for(int i = 0; i < playercount; i++){
				for(int j = 0; j < playerHistory[i][turn+1];j++){
					model.players.add(players.get(i).duplicate());
					
				}
			}
			
			model.setup();

			turn++;
			
			GameMain.out.println("Generation "+(turn+1)+" of " + model.getClass().getSimpleName()
					+ " over " + model.getRoundsDescriptor()
					+ " with rules "+ model.ruleset.getClass().getSimpleName()
					+ " between " + model.getNumPlayers()
					+ " " + model.players.get(0).getName()
					+ ".");
		}

		return model.getNextMatchUp();
	}

	@Override
	public void evaluate(GameThread[] workers) {
		
		for(GameThread worker : workers){
			if(worker != null){
				for(GamePlayer player : players){
					if(player != null){
						if(worker.A.name.equals(player.name))
							pointHistory[players.indexOf(player)][turn] += worker.A.score;
						
						if(worker.B.name.equals(player.name))
							pointHistory[players.indexOf(player)][turn] += worker.B.score;
					}
				}
				
			}
		}
		
		model.evaluate(workers);
	}

	@Override
	public void setup() {
		playercount = players.size();
		pointHistory = new double[playercount][generations];
		playerHistory = new int[playercount][generations];
		for(int i = 0; i < playercount; i++){
			playerHistory[i][0] = 1;
		}
		model.players = players;
		model.ruleset = ruleset;
		model.setup();
		
		GameMain.out.println("Generation 1 of " + model.getClass().getSimpleName()
				+ " over " + model.getRoundsDescriptor()
				+ " with rules "+ model.ruleset.getClass().getSimpleName()
				+ " between " + model.getNumPlayers()
				+ " " + model.players.get(0).getName()
				+ ".");
		
	}

	@Override
	public String[][] printResults() {
		
		ArrayList<ScoreTuple<GamePlayer>> ranking = new ArrayList<ScoreTuple<GamePlayer>>();
		for(int i = 0; i < playercount; i++){
			if(players.get(i) != null){
				ranking.add(new ScoreTuple<GamePlayer>(playerHistory[i][turn], players.get(i)));
			}
		}
		Collections.sort(ranking);
		
		String[][] text = new String[3][];
		
		text[0] = new String[playercount];
		for(int i = 0; i < playercount; i++){
				if(players.get(i).name.length()<8)
					text[0][i] = players.get(i).name + "        \t";
				else
					text[0][i] = players.get(i).name + "\t";
				for(int j = 0; j < turn + 1; j++){
					text[0][i] = text[0][i] + playerHistory[i][j] + "\t";
				}
		}
				
		text[1] = new String[2];
		text[1][0] = "-----";
		text[1][1] = "##\t(end)\t:Name:";
		
		text[2] = new String[playercount];
		for(int i = 0; i < playercount; i++){
			ScoreTuple<GamePlayer> p = ranking.get(i);
			text[2][i] = "#"+(i+1) + ":\t(" +p.getScore()+")\t"+ p.getPayload().name;
		}
		
		return text;
	}

	@Override
	public int getNumPlayers() {
		return playercount;
	}

}

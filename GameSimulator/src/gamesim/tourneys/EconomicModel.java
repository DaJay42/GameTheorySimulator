package gamesim.tourneys;

import java.util.ArrayList;
import java.util.Collections;

import gamesim.GameEntityReflector;
import gamesim.GameMain;
import gamesim.GamePlayer;
import gamesim.GameThread;
import gamesim.GameTourney;
import gamesim.util.ScoreTuple;

/**A meta-tournament that runs several generations (default: 50) of an internal tournament model (default: gamesim.tourneys.RoundRobinDecay).
 * <p>
 * In the first generation, all players are represented once.
 * In every subsequent generation, the internal players (default: 400)
 * are distributed to strategies according to the total points achieved
 * by the players using that strategy in the previous generation.
 * 
 *<p>Takes two integer number as first and second String parameter.
 *<br>Takes one full class name as third String parameter.
 *<br>All subsequent arguments are forwarded to the internal model.
 * 
 * @param generations : int. The number of time the model is run game
 * @param internalPlayers : int. The maximum number of players each run of the model has.
 * @param modelClass : Class<? extends GameTourney>. The internal tournament model to use. 
 * @author DaJay42
 *
 */
public class EconomicModel extends GameTourney {

	int generations = 50;
	GameTourney model;
	Class<? extends GameTourney> modelClass = RoundRobinDecay.class;
	int playercount;
	int internalPlayers = 500;
	long[][] pointHistory; //beware of overflows/precision. long should be suitable enough
	int[][] playerHistory;
	int turn = 0;
	String[] arg1;
	
	public EconomicModel(String...args) throws InstantiationException, ClassNotFoundException{
		super(args);
		if(args.length > 3)
			arg1 = new String[args.length-3];
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
				arg1[i-3] = args[i];
			}
		}
		
		model = (GameTourney) GameEntityReflector.createWithArgs(modelClass, arg1);
	}
	
	@Override
	public boolean isFinished() {
		for(int i = 0; i < playerHistory.length; i++){
			if(playerHistory[i][turn] == internalPlayers){
				GameMain.printWarning("EconomicModel terminated after "+ turn + " Generations due to domineering strategy "+players.get(i).name+".");
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
	public void resetPlayers() throws InstantiationException{
		super.resetPlayers();
		model.resetPlayers();
	}
	
	@Override
	public String getRoundsDescriptor() {
		return generations + " generations of " +model.getClass().getSimpleName()+ " (with "+model.getRoundsDescriptor() +" each)";
	}

	@Override
	public GameThread[] getNextMatchUp() throws InstantiationException {
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
			model = (GameTourney) GameEntityReflector.createWithArgs(modelClass, arg1);
			
			model.players = new ArrayList<GamePlayer>();
			model.ruleset = ruleset;
			for(int i = 0; i < playercount; i++){
				for(int j = 0; j < playerHistory[i][turn+1];j++){
					model.players.add(players.get(i).duplicate());
					
				}
			}
			
			model.setup();

			turn++;
			
			GameMain.printInfo("Generation "+(turn+1)+" of " + model.getClass().getSimpleName()
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
		model.evaluate(workers);
		
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
		
	}

	@Override
	public void setup() throws InstantiationException {
		playercount = players.size();
		pointHistory = new long[playercount][generations+1];
		playerHistory = new int[playercount][generations+1];
		for(int i = 0; i < playercount; i++){
			playerHistory[i][0] = 1;
		}
		model.players = players;
		model.ruleset = ruleset;
		model.setup();
		
		GameMain.printInfo("Generation 1 of " + model.getClass().getSimpleName()
				+ " over " + model.getRoundsDescriptor()
				+ " with rules "+ model.ruleset.getClass().getSimpleName()
				+ " between " + model.getNumPlayers()
				+ " " + model.players.get(0).getName()
				+ ".");
		
	}

	@Override
	public String[][] printResults() {
		double total = 0;
		for(int i = 0; i < playercount; i++){
			total += pointHistory[i][turn];
		}
		double scale = internalPlayers/total;
		
		for(int i = 0; i < playercount; i++){
			playerHistory[i][turn+1] = (int)(scale*pointHistory[i][turn]);
		}
		turn++;
		ArrayList<ScoreTuple<GamePlayer>> ranking = getRanking();
		
		String[][] text = new String[3][];
		
		text[0] = new String[playercount];
		int max = 0;
		for(int i = 0; i < playercount; i++){
			max = Math.max(max, players.get(i).name.length());
		}
		for(int i = 0; i < playercount; i++){
				text[0][i] = String.format("%-"+max+"s\t", players.get(i).name);
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

	@Override
	public ArrayList<ScoreTuple<GamePlayer>> getRanking() {
		ArrayList<ScoreTuple<GamePlayer>> ranking = new ArrayList<ScoreTuple<GamePlayer>>();
		for(int i = 0; i < playercount; i++){
			if(players.get(i) != null){
				ranking.add(new ScoreTuple<GamePlayer>(playerHistory[i][turn], players.get(i)));
			}
		}
		Collections.sort(ranking);
		return ranking;
	}

}

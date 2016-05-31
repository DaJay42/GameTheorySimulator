package gamesim.tourneys;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import gamesim.GamePlayer;
import gamesim.GameThread;
import gamesim.GameTourney;
import gamesim.util.ScoreTuple;

/**Round Robin Tournament, with fixed, chosen number of rounds (default: 200).
 *<br>Every player plays against everyone (including itself) once.
 *<br>As played in Axelrod's first tournament.
 * 
 *<p>Takes one integer number as first String parameter.
 *<br>All subsequent arguments are ignored.
 * 
 * @param gamerounds : int. The number of rounds each game.
 * @author DaJay42
 *
 */
public class RoundRobinFixed extends GameTourney {

	int matchups;
	int round = 0;
	int playercount;
	int gamerounds = 200;
	int[][] results;
	GameThread[][] allMatchups;
	
	
	
	public RoundRobinFixed(String...args) {
		super(args);
		if(args != null && args.length > 0)
			gamerounds = Integer.parseInt(args[0]);
	}
	
	public void setup() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		playercount = players.size();
		if(playercount % 2 == 1)
			players.add(null);
		matchups = (players.size());
		int games = players.size()/2;
		results = new int[playercount][playercount];
		allMatchups = new GameThread[matchups+2][games];
		
		for (int round = 0; round < matchups; round++) {
	        for (int match = 0; match < games; match++) {
	            int home = (round + match) % (players.size() - 1);
	            int away = (players.size() - 1 - match + round) % (players.size() - 1);

	            if (match == 0) {
	                away = players.size() - 1;
	            }
	            if(players.get(home) != null && players.get(away) != null){
	            	allMatchups[round][match] = new GameThread(ruleset, players.get(home), 
						players.get(away), getRoundsPerGame());
	            }else{
	            	allMatchups[round][match] = null;
	            }
	        }
	    }
		//play against self.
		for(int i = 0; i < players.size()/2; i++){
			if(players.get(i+players.size()/2) != null)
				allMatchups[matchups][i] = new GameThread(ruleset, players.get(i), 
					players.get(i).duplicate(), getRoundsPerGame());
		}
		for(int i = 0; i < players.size()/2; i++){
			if(players.get(i+players.size()/2) != null)
				allMatchups[matchups+1][i] = new GameThread(ruleset, players.get(i+players.size()/2), 
        			players.get(i+players.size()/2).duplicate(), getRoundsPerGame());
		}
		matchups += 2;
	}

	@Override
	public boolean isFinished() {
		return round == matchups;
	}

	@Override
	public int getRoundsPerGame() {
		return gamerounds;
	}

	@Override
	public GameThread[] getNextMatchUp() {
		workers = allMatchups[round];
		round++;
		return workers;
	}

	@Override
	public String[][] printResults() {
		ArrayList<ScoreTuple<GamePlayer>> ranking = getRanking();
		
		String[][] text = new String[4][];
		
		text[0] = new String[1];
		text[0][0] = "player";
		for(int i = 0; i < playercount; i++){
			text[0][0] = text[0][0]+"\t"+i;
		}
		
		text[1]= new String[playercount];
		for(int i = 0; i < playercount; i++){
				text[1][i] = i + "\t";
				int total = 0;
				for(int j = 0; j < playercount; j++){
					text[1][i] = text[1][i] + results[i][j] + "\t";
					total += results[i][j];
				}
				text[1][i] = text[1][i] + "Total:\t" + total;
		}
		
		text[2] = new String[3];
		text[2][0] = "player";
		for(int i = 0; i < playercount; i++){
			text[2][0] = text[2][0]+"\t"+i;
		}
		text[2][1] = "-----";
		text[2][2] = "##\t(avg)\t:Name:";
		
		text[3] = new String[playercount];
		for(int i = 0; i < playercount; i++){
			ScoreTuple<GamePlayer> p = ranking.get(i);
			text[3][i] = "#"+(i+1) + "\t(" +p.getScore() +")\t"+ p.getPayload().name;
		}
		
		return text;
	}

	@Override
	public void evaluate(GameThread[] workers) {
		//correct for playing against self.
		if(round >= matchups-2){
			for(GameThread worker : workers){
				if(worker != null){
					worker.A.score /= 2;
					worker.B.score /= 2;
					results[players.indexOf(worker.A)][players.indexOf(worker.A)] = worker.A.score + worker.B.score;
				}
			}
			return;
		}
		
		for(GameThread worker : workers){
			if(worker != null){
				results[players.indexOf(worker.A)][players.indexOf(worker.B)] += worker.A.score;
				results[players.indexOf(worker.B)][players.indexOf(worker.A)] += worker.B.score;
			}
		}
	}

	@Override
	public String getRoundsDescriptor() {
		return gamerounds + " rounds";
	}

	@Override
	public int getNumPlayers() {
		return playercount;
	}

	@Override
	public ArrayList<ScoreTuple<GamePlayer>> getRanking() {
		ArrayList<ScoreTuple<GamePlayer>> ranking = new ArrayList<ScoreTuple<GamePlayer>>();
		for(GamePlayer player : players){
			if(player != null){
				int total = 0;
				for(int j = 0; j < playercount; j++){
					total += results[players.indexOf(player)][j];
				}
				ranking.add(new ScoreTuple<GamePlayer>((total/playercount), player));
			}
		}
		Collections.sort(ranking);
		return ranking;
	}

}

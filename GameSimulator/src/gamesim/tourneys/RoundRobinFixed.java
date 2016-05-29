package gamesim.tourneys;

import java.util.ArrayList;
import java.util.Collections;

import gamesim.GamePlayer;
import gamesim.GameThread;
import gamesim.GameTourney;
import gamesim.util.ScoreTuple;

/**Round Robin Tournament, with fixed, chosen number of rounds (default: 200).
 *<br>Everyone plays against everyone else once.
 *<br>As played in Axelrod's first tournament.
 * 
 *<p>Takes one integer number as first String parameter.
 *<br>All subsequent arguments are ignored.
 * 
 * @param median : int. The number of rounds each game.
 * @author DaJay42
 *
 */
public class RoundRobinFixed extends GameTourney {

	int matchups;
	int playercount;
	int gamerounds = 200;
	int[][] results;
	GameThread[][] allMatchups;
	
	
	
	public RoundRobinFixed(String...args) {
		super(args);
		if(args != null && args.length > 0)
			gamerounds = Integer.parseInt(args[0]);
	}
	
	public void setup() {
		playercount = players.size();
		if(playercount % 2 == 1)
			players.add(null);
		matchups = (players.size()-1);
		int games = players.size()/2;
		results = new int[playercount][playercount];
		allMatchups = new GameThread[matchups][games];
		
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
		
	}

	@Override
	public boolean isFinished() {
		return matchups == 0;
	}

	@Override
	public int getRoundsPerGame() {
		return gamerounds;
	}

	@Override
	public GameThread[] getNextMatchUp() {
		matchups--;
		workers = allMatchups[matchups];
		return workers;
	}

	@Override
	public String[][] printResults() {
		ArrayList<ScoreTuple<GamePlayer>> ranking = new ArrayList<ScoreTuple<GamePlayer>>();
		for(GamePlayer player : players){
			if(player != null){
				int total = 0;
				for(int j = 0; j < playercount; j++){
					total += results[players.indexOf(player)][j];
				}
				ranking.add(new ScoreTuple<GamePlayer>(total, player));
			}
		}
		Collections.sort(ranking);
		
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
			text[3][i] = "#"+(i+1) + "\t(" +(int)(p.getScore()/(float)(playercount-1)) +")\t"+ p.getPayload().name;
		}
		
		return text;
	}

	@Override
	public void evaluate(GameThread[] workers) {
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

}

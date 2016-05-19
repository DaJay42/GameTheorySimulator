package gamesim.tourneys;

import java.util.ArrayList;
import java.util.Collections;

import gamesim.GamePlayer;
import gamesim.GameThread;
import gamesim.GameTourney;
import gamesim.util.ScoreTuple;

/**Round Robin Tournament, with fixed, chosen number of rounds (default 200).
 * Everyone plays against everyone else once.
 * As played in Axelrod's first tournament.
 * @author DaJay42
 *
 */
public class RoundRobinFixed extends GameTourney {

	int matchups;
	int playercount;
	int gamerounds = 200;
	int[][] results;
	GameThread[][] allMatchups;
	
	
	public RoundRobinFixed(int rounds) {
		gamerounds = rounds;
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
	public String[] printResults() {
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
		
		String[] text = new String[playercount*2+2];
		
		for(int k = 0; k < text.length; k++){
			if(k < playercount){
				int i = k;
				text[k] = i + ": ";
				int total = 0;
				for(int j = 0; j < playercount; j++){
					text [k] = text[k] + results[i][j] + ", ";
					total += results[i][j];
				}
				text[k] = text[k] + "Total: " + total;
				
			}else if(k == playercount){
				text[k] = "-----";
			}else if(k == playercount + 1){
				text[k] = "##  (avg) :Name:";
			}else if(k >= playercount + 2){
				int i = k-playercount-2;
				ScoreTuple<GamePlayer> p = ranking.get(i);
				text[k] = "#"+(i+1) + ": (" +(int)(p.getScore()/(float)playercount) +") "+ p.getPayload().name;
			}
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
		return "rounds";
	}

	@Override
	public int getNumPlayers() {
		return playercount;
	}

}

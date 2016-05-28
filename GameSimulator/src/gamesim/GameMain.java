package gamesim;

import java.io.PrintStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import gamesim.strategies.*;
import gamesim.tourneys.*;
import gamesim.gamerules.*;
import gamesim.players.*;


/**
 * Main Object. Everything starts here.
 * All main parameters have the format '-x'.
 * All parameters are optional.
 * All parameters are order-independent,
 * however it is recommended to pass '-o' first and '-i' last.
 * Lists of arguments are separated by simple spaces,
 * and terminated by the next '-x' parameter.
 * 
 * @param t : Full class name of the GameTourney to be played,
 * followed by a list of (String) arguments to be passed to it.
 * See the respective class for supported arguments.
 * If specified multiple times, last passed wins.
 * Default: gamesim.tourneys.EconomicModel {empty list}.
 * 
 * @param r : Full class name of the GameRuleSet to follow.
 * If specified multiple times, last passed wins.
 * Default: gamesim.gamerules.AxelrodPrisoners
 * 
 * @param s : List of the full class names
 * of the GameStrategies that will participate.
 * If specified multiple times, the lists will be appended.
 * Default: (see defaultStrategies)
 * 
 * @param p : Full class name of the GamePlayer to model,
 * followed by a list of (String) arguments to be passed to it.
 * See the respective class for supported arguments.
 * If specified multiple times, last passed wins.
 * Default: gamesim.GamePlayer {empty list}.
 * 
 * @param o : Path to a file, to which results will be printed.
 * If specified multiple times, last passed wins.
 * If not specified, System.out will be used instead.
 * 
 * @param i : Path to a file from which to read more arguments.
 * All whitespace (\r, \n, \t and space) are valid separators.
 * All arguments will be appended to the current list.
 * If specified multiple times (e.g. in the file(s)), the evaluation will be chained.
 * 
 * @author DaJay42
 */
public class GameMain {
	
	
	public static PrintStream out = System.out;
	
	public static void main(String[] args){
		ArrayList<String> lArgs = new ArrayList<String>();
		for(String s : args)
			lArgs.add(s);
		
		//defaults
		Class<? extends GameRuleSet> ruleSet = AxelrodPrisoners.class;
		
		Class<? extends GameTourney> tourney = EconomicModel.class;
		String[] tourneyArgs = {};
		ArrayList<String> listTArgs = new ArrayList<String>();
		
		Class<? extends GamePlayer> player = PlayerReliable.class;
		String[] playerArgs = {};
		ArrayList<String> listPArgs = new ArrayList<String>();
		
		ArrayList<Class<? extends GameStrategy>> listStrategies = new ArrayList<Class<? extends GameStrategy>>();
		Class<?>[] defaultStrategies = {
			FiftyFifty.class,
			
			TitForTat.class,
			TitForTwoTat.class,
			Friedman.class,
			Downing.class,
			Joss.class,
			Pavlov.class,
			
			DTitForTat.class,
			
			CDowning.class,
			CSwitch.class,
			Panic.class,
			};
		
			for(int i = 0; i < lArgs.size(); i++){
				switch(lArgs.get(i)){
				case "-t":
					try{
						i++;
						tourney = Class.forName(lArgs.get(i)).asSubclass(GameTourney.class);
						out.printf("Set tournament to '%s'.\n", lArgs.get(i));
						//default args are useless now
						tourneyArgs = new String[0];

						listTArgs = new ArrayList<String>();
						while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
							i++;
							listTArgs.add(lArgs.get(i));
							out.printf("Added tArg '%s'.\n", lArgs.get(i));
						}
						
					}catch(Exception e){
						out.printf("Warning: invalid argument '%s' passed to '-t', ignoring...\n", lArgs.get(i));
					}
					break;
				case "-r":
					try{
						i++;
						ruleSet = Class.forName(lArgs.get(i)).asSubclass(GameRuleSet.class);
						out.printf("Set ruleset to '%s'.\n", lArgs.get(i));
					}catch(Exception e){
						out.printf("Warning: invalid class '%s' passed to '-r', ignoring...\n", lArgs.get(i));
					}
					break;
				case "-p":
					try{
						i++;
						player = Class.forName(lArgs.get(i)).asSubclass(GamePlayer.class);
						out.printf("Set player model to '%s'.\n", lArgs.get(i));
						
						listPArgs = new ArrayList<String>();
						while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
							i++;
							listPArgs.add(lArgs.get(i));
							out.printf("Added pArg '%s'.\n", lArgs.get(i));
						}
					}catch(Exception e){
						out.printf("Warning: invalid argument '%s' passed to '-p', ignoring...\n", lArgs.get(i));
					}
					break;
				case "-s":
					while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
						i++;
						try{
							listStrategies.add(Class.forName(lArgs.get(i)).asSubclass(GameStrategy.class));
							out.printf("Added strategy '%s'.\n", lArgs.get(i));
						}catch(Exception e){
							out.printf("Warning: invalid class '%s' passed to '-s', ignoring...\n", lArgs.get(i));
						}
					}
					break;
				case "-i":
					try{
						i++;
						List<String> text = Files.readAllLines(FileSystems.getDefault().getPath(lArgs.get(i)));
						for(String line : text){
							for(String word : line.replaceAll("\t", " ").split(" ")){
								if(word.length() > 0){
									lArgs.add(word);
								}
							}
						}
						out.printf("GameTheorySimulator: Queueing arguments from '%s'.\n", lArgs.get(i));
					}catch(Exception e){
						out.printf("Warning: invalid file '%s' passed to '-i', ignoring...\n", lArgs.get(i));
					}
				break;
				case "-o":
					try {
						i++;
						out.printf("GameTheorySimulator: redirecting output to '%s'\n",lArgs.get(i));
						if(out != System.out)
							out.close();
					
						Path p = FileSystems.getDefault().getPath(lArgs.get(i));
						out = new PrintStream(Files.newOutputStream(p),true);
						out.printf("GameTheorySimulator: redirecting output to '%s'\n",lArgs.get(i));
					} catch (Exception e) {
						out.printf("Warning: could not create file '%s' passed to '-o', ignoring...\n", lArgs.get(i));
						
					}
					break;
				default:
					out.printf("Info: unexpected argument '%s', ignoring...\n", lArgs.get(i));
					break;
				}
			}
			out.println("-----");

		try{
			if(listStrategies.size() < 2){
				for(Class<?> c : defaultStrategies){
					if(GameStrategy.class.isAssignableFrom(c))
						listStrategies.add(c.asSubclass(GameStrategy.class));
				}
			}
			
			if(listTArgs.size() > 0){
				tourneyArgs = listTArgs.toArray(tourneyArgs);
			}
			if(listPArgs.size() > 0){
				playerArgs = listPArgs.toArray(playerArgs);
			}
			
			GameRunner runner = new GameRunner(ruleSet, player, playerArgs, listStrategies, tourney, tourneyArgs);
			runner.run();
			
			out.println("-----\nGameTheorySimulator: finished");
			if(out != System.out)
				System.out.println("-----\nGameTheorySimulator: finished");
			
		}catch(Exception e){
			e.printStackTrace();
			if(out != System.out)
				e.printStackTrace(out);
		}finally{
			if(out != System.out)
				out.close();
		}
	}
	
}

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
 * <p>
 * All main parameters have the format '-x'.
 *<br>All parameters are optional.
 *<br>All parameters are order-independent,
 * however it is recommended to pass '-o' first and '-i' last.
 *<br>Lists of arguments are separated by simple spaces,
 * and terminated by the next '-x' parameter.
 * 
 * @param t : Full class name of the GameTourney to be played,
 * <br>followed by a list of (String) arguments to be passed to it.
 * <br>See the respective class for supported arguments.
 * <br>If specified multiple times, last passed wins.
 * <br>Default: gamesim.tourneys.EconomicModel {empty list}.
 * <p>
 * @param r : Full class name of the GameRuleSet to follow.
 * <br>If specified multiple times, last passed wins.
 * <br>Default: gamesim.gamerules.AxelrodPrisoners
 * <p>
 * @param s : List of the full class names
 * <br>of the GameStrategies that will participate.
 * <br>If specified multiple times, the lists will be appended.
 * <br>Default: (see defaultStrategies)
 * <p>
 * @param p : Full class name of the GamePlayer to model,
 * <br>followed by a list of (String) arguments to be passed to it.
 * <br>See the respective class for supported arguments.
 * <br>If specified multiple times, last passed wins.
 * <br>Default: gamesim.GamePlayer {empty list}.
 * <p>
 * @param o : Path to a file, to which results will be printed.
 * <br>If specified multiple times, last passed wins.
 * <br>If not specified, System.out will be used instead.
 * <p>
 * @param i : Path to a file from which to read more arguments.
 * <br>All whitespace (\r, \n, \t and space) are valid separators.
 * <br>All arguments will be appended to the current list.
 * <br>If specified multiple times (e.g. in the file(s)), the evaluation will be chained.
 * 
 * @author DaJay42
 */
public class GameMain {
	
	/**Set flag to use multithreading. Current results show that the overhead is not worth it.*/
	public static final boolean DO_THREADING = false;
	
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
						out.printf("Error: invalid argument '%s' passed to '-t', ignoring...\n", lArgs.get(i));
					}
					break;
				case "-r":
					try{
						i++;
						ruleSet = Class.forName(lArgs.get(i)).asSubclass(GameRuleSet.class);
						out.printf("Set ruleset to '%s'.\n", lArgs.get(i));
					}catch(Exception e){
						out.printf("Error: invalid class '%s' passed to '-r', ignoring...\n", lArgs.get(i));
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
						out.printf("Error: invalid argument '%s' passed to '-p', ignoring...\n", lArgs.get(i));
					}
					break;
				case "-s":
					while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
						i++;
						try{
							listStrategies.add(Class.forName(lArgs.get(i)).asSubclass(GameStrategy.class));
							out.printf("Added strategy '%s'.\n", lArgs.get(i));
						}catch(Exception e){
							out.printf("Error: invalid class '%s' passed to '-s', ignoring...\n", lArgs.get(i));
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
						out.printf("Error: invalid file '%s' passed to '-i', ignoring...\n", lArgs.get(i));
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
						out.printf("Error: could not create file '%s' passed to '-o', ignoring...\n", lArgs.get(i));
						
					}
					break;
				default:
					out.printf("Warning: unexpected argument '%s', ignoring...\n", lArgs.get(i));
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

package gamesim;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import gamesim.strategies.*;
import gamesim.tourneys.*;
import gamesim.gamerules.*;
import gamesim.players.*;


/**
 * Main Object. Everything starts here.
 * 
 * <p>All main parameters have the format '-x'.
 *<br>All parameters are optional.
 *<br>All parameters are order-independent,
 * however it is recommended to pass '-v', '-o' and '-y' first, and '-i' last.
 *<br>Lists of arguments are separated by whitespace,
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
	
	
	/**  -1: results only (errors redirected to System.err)
	 *<br>0: results and errors
	 *<br>1: results, errors and warnings
	 *<br>2: results, errors, warnings and info
	 */
	private static int verbosity = 2;
	private final static int errors = 0;
	private final static int warnings = 1;
	private final static int infos = 2;
	
	private static boolean alwaysYes = false;
	
	private static PrintStream out = System.out;
	
	public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args){
		boolean err = false;
		ArrayList<String> lArgs = new ArrayList<String>();
		ArrayList<String> files = new ArrayList<String>();
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
			case "-y":
				i++;
				alwaysYes = true;
			case "-v":
				i++;
				try{
					verbosity = Integer.parseInt(lArgs.get(i));
				}catch(Exception e){
					err = true;
					printException("Error: invalid argument '%s' passed to '-v', skipping...", lArgs.get(i));
				}
				break;
			case "-t":
				try{
					i++;
					tourney = Class.forName(lArgs.get(i)).asSubclass(GameTourney.class);
					printInfo("Set tournament to '%s'.", lArgs.get(i));
					//default args are useless now
					tourneyArgs = new String[0];

					listTArgs = new ArrayList<String>();
					while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
						i++;
						listTArgs.add(lArgs.get(i));
						printInfo("Added tArg '%s'.", lArgs.get(i));
					}
					
				}catch(Exception e){
					err = true;
					printException("Error: invalid argument '%s' passed to '-t', skipping...", lArgs.get(i));
				}
				break;
			case "-r":
				try{
					i++;
					ruleSet = Class.forName(lArgs.get(i)).asSubclass(GameRuleSet.class);
					printInfo("Set ruleset to '%s'.", lArgs.get(i));
				}catch(Exception e){
					err = true;
					printException("Error: invalid class '%s' passed to '-r', skipping...", lArgs.get(i));
				}
				break;
			case "-p":
				try{
					i++;
					player = Class.forName(lArgs.get(i)).asSubclass(GamePlayer.class);
					printInfo("Set player model to '%s'.", lArgs.get(i));
					
					listPArgs = new ArrayList<String>();
					while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
						i++;
						listPArgs.add(lArgs.get(i));
						printInfo("Added pArg '%s'.", lArgs.get(i));
					}
				}catch(Exception e){
					err = true;
					printException("Error: invalid argument '%s' passed to '-p', skipping...", lArgs.get(i));
				}
				break;
			case "-s":
				while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
					i++;
					try{
						listStrategies.add(Class.forName(lArgs.get(i)).asSubclass(GameStrategy.class));
						printInfo("Added strategy '%s'.", lArgs.get(i));
					}catch(Exception e){
						err = true;
						printException("Error: invalid class '%s' passed to '-s', skipping...", lArgs.get(i));
					}
				}
				break;
			case "-i":
				while(i+1 < lArgs.size() && !lArgs.get(i+1).startsWith("-")){
					i++;
					try{
						if(files.contains(lArgs.get(i))){
							err = true;
							printException("Error: file '%s' specified multiple times, skipping...", lArgs.get(i));
							continue;
						}
						
						printInfo("GameTheorySimulator: Queueing arguments from '%s'.", lArgs.get(i));
						files.add(lArgs.get(i));
						
						List<String> text = Files.readAllLines(FileSystems.getDefault().getPath(lArgs.get(i)));
						for(String line : text){
							for(String word : line.replaceAll("\t", " ").split(" ")){
								if(word.length() > 0){
									if(word.startsWith("//")){
										break;
									}
									lArgs.add(word);
								}
							}
						}
					}catch(Exception e){
						err = true;
						printException("Error: invalid file '%s' passed to '-i', skipping...", lArgs.get(i));
					}
				}
			break;
			case "-o":
				try {
					i++;

					if(files.contains(lArgs.get(i))){
						err = true;
						printException("Error: file '%s' specified multiple times, skipping...", lArgs.get(i));
						continue;
					}
					files.add(lArgs.get(i));
					
					printInfo("GameTheorySimulator: redirecting output to '%s'",lArgs.get(i));
					if(out != System.out)
						out.close();
				
					Path p = FileSystems.getDefault().getPath(lArgs.get(i));
					out = new PrintStream(Files.newOutputStream(p),true);
					printInfo("GameTheorySimulator: redirecting output to '%s'",lArgs.get(i));
				} catch (Exception e) {
					err = true;
					printException("Error: could not create file '%s' passed to '-o', skipping...", lArgs.get(i));
				}
				break;
			default:
				err = true;
				printWarning("Warning: unexpected argument '%s', skipping...", lArgs.get(i));
				break;
			}
		}
		printResult("-----");
		
		try{
			if(err && !alwaysYes){
				System.out.printf("An error occurred while parsing the input parameters.%nContinue anyway?%n");
				String s = "";
				while(!s.equalsIgnoreCase("y") && !s.equalsIgnoreCase("n")){
					System.out.println("(y/n)");
					s = in.readLine();
				}
				if(s.equalsIgnoreCase("n")){
					System.out.println("Aborted.");
					return;
				}
				printResult("-----");
			}
			
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
			
			printInfo("-----\nGameTheorySimulator: finished");
			if(out != System.out)
				System.out.println("-----\nGameTheorySimulator: finished");
			
		}catch(Exception e){
			printException(e);
		}finally{
			if(out != System.out)
				out.close();
		}
	}
	
	public static void printException(String msg, Object...objects){
		if(verbosity >= errors){
			out.printf(msg+"%n", objects);
		}else{
			System.err.printf(msg+"%n", objects);
		}
	}
	public static void printException(Exception e){
		if(verbosity >= errors){
			e.printStackTrace(out);
		}else{
			e.printStackTrace();
		}
	}
	public static void printException(Exception e, String msg, Object...objects){
		printException(msg, objects);
		printException(e);
	}
	public static void printInfo(String msg, Object...objects){
		if(verbosity >= infos)
			out.printf(msg+"%n", objects);
	}
	public static void printWarning(String msg, Object...objects){
		if(verbosity >= warnings)
			out.printf(msg+"%n", objects);
	}
	public static void printResult(String msg, Object...objects){
		out.printf(msg+"%n", objects);
	}
}

package gamesim;

import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import gamesim.strategies.*;
import gamesim.tourneys.*;
import gamesim.gamerules.*;


/**
 * Main Object. Everything starts here.
 * All parameters are optional and order-independent,
 * though it is recommended to pass -o first and -i last.
 * 
 * @param -t : Full class name of the GameTourney to be played.
 * If specified multiple times, last passed wins.
 * Default: gamesim.tourneys.EconomicModel
 * 
 * @param -r : Full class name of the GameRuleSet to follow.
 * If specified multiple times, last passed wins.
 * Default: gamesim.gamerules.AxelrodPrisoners
 * 
 * @param -s : Comma-and-space-separated list of
 * the full class names of the GameStrategies that will participate.
 * If specified multiple times, the lists will be appended.
 * Default: (see defaultStrategies)
 * 
 * @param -a : Comma-and-space-separated list of 
 * (String) arguments to be passed to the GameTourney in use.
 * If specified multiple times, the lists will be appended.
 * Default: empty list
 * 
 * @param -o : Path to a file, to which results will be printed.
 * If specified multiple times, last passed wins.
 * If not specified, System.out will be used instead.
 * 
 * @param -i : Path to a file from which to read more arguments.
 * All whitespace (\r, \n, \t and space) are valid separators.
 * All arguments passed after this will be ignored.
 * If specified multiple times (i.e. in the file(s)), the evaluation will be chained.
 * 
 * @author DaJay42
 */
public class GameMain {
	
	
	public static PrintStream out = System.out;
	
	public static void main(String[] args){
		
		//defaults
		Class<? extends GameRuleSet> ruleSet = AxelrodPrisoners.class;
		Class<? extends GameTourney> tourney = EconomicModel.class;
		String[] tourneyArgs = {};
		ArrayList<String> listArgs = new ArrayList<String>();
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
		
		try{
			for(int i = 0; i < args.length; i++){
				switch(args[i]){
				case "-t":
					try{
						tourney = Class.forName(args[i+1]).asSubclass(GameTourney.class);
						out.printf("Set tournament to '%s'.\n", args[i+1]);
						//default args are useless now
						tourneyArgs = new String[0];
					}catch(Exception e){
						out.printf("Warning: invalid class '%s' passed to '-t', ignoring...\n", args[i+1]);
					}
					i++;
					break;
				case "-r":
					try{
						ruleSet = Class.forName(args[i+1]).asSubclass(GameRuleSet.class);
						out.printf("Set ruleset to '%s'.\n", args[i+1]);
					}catch(Exception e){
						out.printf("Warning: invalid class '%s' passed to '-r', ignoring...\n", args[i+1]);
					}
					i++;
					break;
				case "-s":
					try{
						listStrategies.add(Class.forName(args[i+1].replaceAll(",", "")).asSubclass(GameStrategy.class));
						out.printf("Added strategy '%s'.\n", args[i+1]);
					}catch(Exception e){
						out.printf("Warning: invalid class '%s' passed to '-s', ignoring...\n", args[i+1]);
					}
					i++;
					while(args[i].endsWith(",")){
						try{
							listStrategies.add(Class.forName(args[i+1].replaceAll(",", "")).asSubclass(GameStrategy.class));
							out.printf("Added strategy '%s'.\n", args[i+1]);
						}catch(Exception e){
							out.printf("Warning: invalid class '%s' passed to '-s', ignoring...\n", args[i+1].replaceAll(",", ""));
						}
						i++;
					}
					break;
				case "-a":
					listArgs.add(args[i+1].replaceAll(",", ""));
					out.printf("Added tArg '%s'.\n", args[i+1]);
					i++;
					while(args[i].endsWith(",")){
						listArgs.add(args[i+1].replaceAll(",", ""));
						out.printf("Added tArg '%s'.\n", args[i+1]);
						i++;
					}
					break;
				case "-i":
					try{
						List<String> fi = Files.readAllLines(FileSystems.getDefault().getPath(args[i+1]));
						ArrayList<String> lArgs = new ArrayList<String>();
						for(String line : fi){
							for(String word : line.replaceAll("\t", " ").split(" ")){
								if(word.length() > 0){
									lArgs.add(word);
								}
							}
						}
						out.printf("GameTheorySimulator: Now reading arguments from '%s'.\n", args[i+1]);
						args = lArgs.toArray(args);
						i = -1;
					}catch(Exception e){
						out.printf("Warning: invalid file '%s' passed to '-i', ignoring...\n", args[i+1]);
					}
				break;
				case "-o":
					out.println("GameTheorySimulator: redirecting output to '"+args[i+1]+"'");
					if(out != System.out)
						out.close();
					
					Path p = FileSystems.getDefault().getPath(args[i+1]);
					out = new PrintStream(Files.newOutputStream(p),true);
					out.println("GameTheorySimulator: redirecting output to '"+args[i+1]+"'");
					i++;
					break;
				default:
					out.printf("Info: unexpected argument '%s', ignoring...\n", args[i]);
					break;
				}
			}
			out.println("-----");
			
			if(listStrategies.size() < 2){
				for(Class<?> c : defaultStrategies){
					if(GameStrategy.class.isAssignableFrom(c))
						listStrategies.add(c.asSubclass(GameStrategy.class));
				}
			}
			
			if(listArgs.size() > 0){
				tourneyArgs = listArgs.toArray(tourneyArgs);
			}
			
			GameRunner runner = new GameRunner(ruleSet, listStrategies, tourney, tourneyArgs);
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

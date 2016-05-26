package gamesim;

import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import gamesim.strategies.*;
import gamesim.tourneys.*;
import gamesim.gamerules.*;


/**
 * Main Object. Everything starts here.
 * All parameters are optional and order-independent.

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
 * Default: (see GameMein.allStrategies)
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
		Class<?>[] allStrategies = {
			FiftyFifty.class,
			AlwaysC.class, 
			AlwaysD.class,
			Alternate.class,
			
			TitForTat.class,
			TitForTwoTat.class,
			Friedman.class,
			Downing.class,
			Joss.class,
			Pavlov.class,
			
			DTitForTat.class,
			CDowning.class,
			
			Switch.class,
			Panic.class,
			};
		
		try{
			for(int i = 0; i < args.length; i++){
				switch(args[i]){
				case "-t":
					tourney = Class.forName(args[i+1]).asSubclass(GameTourney.class);
					//default args are useless now
					tourneyArgs = new String[0];
					i++;
					break;
				case "-r":
					ruleSet = Class.forName(args[i+1]).asSubclass(GameRuleSet.class);
					i++;
					break;
				case "-s":
					listStrategies.add(Class.forName(args[i+1].replaceAll(",", "")).asSubclass(GameStrategy.class));
					while(args[i+1].endsWith(",")){
						i++;
						listStrategies.add(Class.forName(args[i+1].replaceAll(",", "")).asSubclass(GameStrategy.class));
					}
					break;
				case "-a":
					listArgs.add(args[i+1].replaceAll(",", ""));
					while(args[i+1].endsWith(",")){
						i++;
						listArgs.add(args[i+1].replaceAll(",", ""));
					}
					break;
				case "-i":
					List<String> fi = Files.readAllLines(FileSystems.getDefault().getPath(args[i+1]));
					ArrayList<String> lArgs = new ArrayList<String>();
					for(String line : fi){
						for(String word : line.replaceAll("\t", " ").split(" ")){
							if(word.length() > 0){
								lArgs.add(word);
							}
						}
					}
					args = lArgs.toArray(args);
					i = -1;
					break;
				case "-o":
					out = new PrintStream(args[i+1]);
					out.println("/**GameTheorySimulator outputting to "+args[i+1]+"**/");
					i++;
				default:
					break;
				}
			}
			
			if(listStrategies.size() == 0){
				for(Class<?> c : allStrategies){
					if(GameStrategy.class.isAssignableFrom(c))
						listStrategies.add(c.asSubclass(GameStrategy.class));
				}
			}
			
			if(listArgs.size() > 0){
				tourneyArgs = listArgs.toArray(tourneyArgs);
			}
			
			GameRunner runner = new GameRunner(ruleSet, listStrategies, tourney, tourneyArgs);
			runner.run();
			
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

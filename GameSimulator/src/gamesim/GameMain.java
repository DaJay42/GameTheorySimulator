package gamesim;

import gamesim.strategies.*;
import gamesim.tourneys.*;
import gamesim.gamerules.*;


/**
 * Main Object. Everything starts here.
 * To change execution parameters, modify t, r, ss in main method.
 * TODO: implement some sort of user interface (GUI/CLI/file-based?),
 * so you don't have to play around *inside* the code...
 * 
 * @author DaJay42
 */
public class GameMain {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		
		/* To consider for input:
		 * Creating Object from String classname:
		 * Class.forName(classname).newInstance();
		 */
		
		GameTourney t = new RoundRobinFixed(200);
		
		GameType r =  new AxelrodPrisoners();
		
		GameStrategy[] ss = {
				new FiftyFifty(),
				new AlwaysC(), 
				new AlwaysD(),
				new Alternate(),
				
				new TitForTat(),
				new TitForTwoTat(),
				new DTitForTat(),
				new Friedman(),
				new Downing(),
				new Joss(),
				new Pavlov(),
				
				new Switch(),
				new Panic(),
				};
		
		GameRunner runner = new GameRunner(t, r, ss);
		runner.run();
	}
}

/**
 * 
 */
package gamesim;

/**The root object from which all modular components of the simulation are derived.
 * @author DaJay42
 *
 */
public abstract class GameEntity {
	protected String[] arg0;
	
	@SuppressWarnings("unused")
	private GameEntity(){};
	
	protected GameEntity(String... args){
		arg0 = args;
	}
	
}

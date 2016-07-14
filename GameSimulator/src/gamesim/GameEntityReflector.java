package gamesim;

import java.lang.reflect.Constructor;

/**A reflection-based factory for GameEntity objects.
 * @author DaJay42
 *
 */
public class GameEntityReflector {
	
	public static GameEntity create(Class<? extends GameEntity> clazz) throws InstantiationException{
		GameEntity product = null;
		try {
			product = clazz.newInstance();
			assert(product != null);
		} catch (Exception e) {
			throw new InstantiationException("No suitable constructor found for class "+clazz.getName());
			
		}
		return product;
	}
	
	public static GameEntity createWithArgs(Class<? extends GameEntity> clazz, String... args) throws InstantiationException{
		GameEntity product = null;
		try {
			for(Constructor<?> c : clazz.getConstructors()){
				if(c.getParameterTypes().length == 1 && c.getParameterTypes()[0] == String[].class){
					Object[] o = {args};
					
						product = (GameEntity) c.newInstance(o);
					
					break;
				}
			}
			assert(product != null);
		} catch (Exception e) {
			throw new InstantiationException("No suitable constructor found for class "+clazz.getName()+" with args "+args.toString());
			
		}
		return product;
	}
	
	public static GamePlayer createPlayer(Class<? extends GamePlayer> clazz, Class<? extends GameStrategy> strat,  String... args) throws InstantiationException{
		GamePlayer product = null;
		try {
			for(Constructor<?> c : clazz.getConstructors()){
				if(c.getParameterTypes().length == 2 && c.getParameterTypes()[1] == String[].class){
					Object[] o = {strat, args};
					
						product = (GamePlayer) c.newInstance(o);
					
					break;
				}
			}
			assert(product != null);
		} catch (Exception e) {
			throw new InstantiationException("No suitable constructor found for class "+clazz.getName()+" with args "+args.toString());
			
		}
		return product;
	}
}

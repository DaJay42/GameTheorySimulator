package gamesim.util;

/**An utility class for sorting of rankings
 * and similar shenanigans.
 * Sorting works DESCENDING.
 * @author DaJay42
 *
 * @param <T> Any type you like.
 */
public class ScoreTuple<T> implements Comparable<ScoreTuple<T>> {
	
	int score;
	T payload;
	
	public ScoreTuple(int score, T payload){
		this.score = score;
		this.payload = payload;
	}
	
	public int getScore(){
		return score;
	}
	
	public T getPayload(){
		return payload;
	}
	
	/**Sorts ScoreTuples in DESCENDING order of score.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ScoreTuple<T> other) {
		return other.score - score;
	}

}

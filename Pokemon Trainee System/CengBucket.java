public class CengBucket {

	Integer localD = 0;
	Integer bucketSize = CengPokeKeeper.getBucketSize();
	Integer currSize = 0;
	CengPoke [] bucket = new CengPoke[bucketSize];
	Boolean visitedByDelete = false;
	public CengBucket(){

	}
	public CengBucket(Integer localdepth){
		this.localD = localdepth;
	}
	public boolean isEmpty(){
		return currSize!=bucketSize;
	}

	public void deletePokebyIndex(Integer index){
		bucket[index]= null;
		for(int i=index;i<currSize-1;i++){
			bucket[i]=bucket[i+1];
		}
		bucket[currSize-1]= null;
		currSize--;
	}

	public void addPoke(CengPoke poke){
		bucket[currSize] = poke;
		currSize++;
	}









	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	
	public int pokeCount()
	{
		// TODO: Return the pokemon count in the bucket.
		return 0;		
	}
	
	public CengPoke pokeAtIndex(int index)
	{
		// TODO: Return the corresponding pokemon at the index.
		return null;
	}
	
	public int getHashPrefix()
	{
		// TODO: Return hash prefix length.
		return 0;
	}
	
	public Boolean isVisited()
	{
		// TODO: Return whether the bucket is found while searching.
		return false;		
	}
	
	// Own Methods
}

public class CengHashRow {

	public CengBucket pointer;
	public String hash_prefix = "0";

	public CengHashRow(CengBucket bucket){
		this(bucket, "0");
	}
	public CengHashRow(CengBucket bucket, String prefix){
		pointer = bucket;
		hash_prefix = prefix;
	}

	public void resetBucket(){
		pointer.currSize = 0;
	}







	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	
	public String hashPrefix()
	{
		// TODO: Return row's hash prefix (such as 0, 01, 010, ...)
		return "-1";		
	}
	
	public CengBucket getBucket()
	{
		// TODO: Return the bucket that the row points at.
		return null;		
	}
	
	public boolean isVisited()
	{
		// TODO: Return whether the row is used while searching.
		return false;		
	}
	
	// Own Methods
}

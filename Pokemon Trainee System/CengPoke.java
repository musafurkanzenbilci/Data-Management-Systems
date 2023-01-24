public class CengPoke {
	
	private Integer pokeKey;
	
	private String pokeName;
	private String pokePower;
	private String pokeType;
	
	public CengPoke(Integer pokeKey, String pokeName, String pokePower, String pokeType)
	{
		this.pokeKey = pokeKey;
		this.pokeName = pokeName;
		this.pokePower = pokePower;
		this.pokeType = pokeType;
	}
	
	// Getters
	
	public Integer pokeKey()
	{
		return pokeKey;
	}
	public String pokeName()
	{
		return pokeName;
	}
	public String pokePower()
	{
		return pokePower;
	}
	public String pokeType()
	{
		return pokeType;
	}
		
	// GUI method - do not modify
	public String fullName()
	{
		return "" + pokeKey() + "\t" + pokeName() + "\t" + pokePower() + "\t" + pokeType;
	}
		
	// Own Methods
	public double customLog(double base, double logNumber) {
		return Math.log(logNumber) / Math.log(base);
	}


	public String getHash(){
		Integer len = (int)customLog(2,CengPokeKeeper.getHashMod());
		String res = Integer.toBinaryString(pokeKey % CengPokeKeeper.getHashMod());
		if(res.length() < len){
			int dif = (int)len - res.length();
			for(int i=0;i<dif;i++){
				res = "0"+ res;
			}
		}
		return res;
	}
}

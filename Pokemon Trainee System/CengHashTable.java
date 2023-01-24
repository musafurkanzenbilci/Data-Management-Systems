import java.lang.Math;
import java.util.ArrayList;
public class CengHashTable {
	Integer globalD = 0;
	ArrayList<CengHashRow> directory = new ArrayList<CengHashRow>();
	//CengHashRow [] directory = new CengHashRow[1];
	Integer hashMod = CengPokeKeeper.getHashMod();
	public class Bucket{

	}
	public CengHashTable()
	{
		CengBucket bucket = new CengBucket();
		//bucket.currSize = 1;
		directory.add(new CengHashRow(bucket));

		CengPoke tmp = new CengPoke(1,"Pikachu","100","Electric");
		this.addPoke(tmp);
		tmp = new CengPoke(4,"Charizard","200","Fire");
		this.addPoke(tmp);
		tmp = new CengPoke(5,"Squirtle","120","Water");
		this.addPoke(tmp);
		tmp = new CengPoke(7,"Raichu","250","Electric");
		this.addPoke(tmp);/*
		tmp = new CengPoke(0,"denade","nada","jamesbond");
		this.addPoke(tmp);*/

		/*

		directory.get(0).pointer.bucket[0]=tmp;*/
		// TODO: Create a hash table with only 1 row.
	}

	public void deletePoke(Integer pokeKey)
	{
		Integer i = 0,j = 0,numberOfEmptyBuckets = 0;
		while(i != Math.pow(2,this.globalD)){
			j = 0;
			while(j < directory.get(i).pointer.currSize){
				if(directory.get(i).pointer.bucket[j].pokeKey() == pokeKey){
					directory.get(i).pointer.deletePokebyIndex(j);
				}
				j++;
			}
			if(directory.get(i).pointer.currSize == 0 && !directory.get(i).pointer.visitedByDelete){
				numberOfEmptyBuckets++;
				directory.get(i).pointer.visitedByDelete=true;
			}
			i++;
		}

		System.out.println("\"delete\": {");
		System.out.print("\t\"emptyBucketNum\": "+numberOfEmptyBuckets+"\n");
		System.out.print("}\n");
	}

	public void addPoke(CengPoke poke) {

		Integer hashValue = poke.pokeKey() % hashMod;
		String bin = this.prepareBin(hashValue);
		//String bin = Integer.toBinaryString(hashValue);
		String prefix = bin.substring(0, globalD);
		//Create a condition for the initial hashTable
		if (globalD == 0) {
			prefix = "0";
			//if related bucket is empty, put the poke inside and increment currSize
			if (directory.get(0).pointer.isEmpty()) {
				directory.get(0).pointer.bucket[directory.get(0).pointer.currSize] = poke;
				directory.get(0).pointer.currSize++;
			} else {
				//if bucket is not empty
				//Since initially there is no other bucket, we will directly duplicate the directory
				Integer dirSize = (int) Math.pow(2, globalD);
				globalD++;
				prefix = bin.substring(0, globalD);
				//Look for new page to allocate before splitting

				//Splitting the bucket

				//1-Increment the local depth
				directory.get(0).pointer.localD++;

				//Double the directory since it is necessary in the initial case
				for (int i = 0; i < dirSize; i++) {
					//Copy the each row in the old table
					CengHashRow tempRow = new CengHashRow(directory.get(0).pointer, directory.get(i).hash_prefix);
					String oldhash = directory.get(i).hash_prefix;
					directory.get(i).hash_prefix = "0";//+oldhash;//make it directly 0 for 1st expansion
					tempRow.hash_prefix = "1";//+oldhash;//make it directly 1 for 1st expansion
					directory.add(tempRow);
				}
				//2-Allocate new page with new local depth
				//TODO:Find the new page for other cases :: to find the new bucket, 2^(globalD-1) ekle
				//For the initial case, we will use directory[1] directly
				//In here we will just make the bucket of new page empty or create a bucket from scratch

				directory.get(1).pointer = new CengBucket(directory.get(0).pointer.localD);


				//3-Re-distribution of records
				//Find the row to be inserted by replacing the 0 at the beginning by 1
				Integer len = directory.get(0).pointer.currSize;
				String firstHash = directory.get(0).hash_prefix;
				Integer strlen = (directory.get(0).hash_prefix).length();
				String nextHash = firstHash.substring(1, strlen) + "1";

				//Loop through the elements of buckets and check their prefix
				for (int i = 0; i < len; i++) {
					int localDepth = directory.get(0).pointer.localD;
					String prefixOfPoke = (directory.get(0).pointer.bucket[i].getHash()).substring(0, localDepth);
					if (prefixOfPoke.equals(nextHash)) {
						//Transfer the poke to the next bucket
						CengPoke pekka = directory.get(0).pointer.bucket[i];
						Integer key = pekka.pokeKey();
						String name = pekka.pokeName();
						String power = pekka.pokePower();
						String type = pekka.pokeType();
						CengPoke tempPoke = new CengPoke(key, name, power, type);
						//delete from old bucket
						directory.get(0).pointer.deletePokebyIndex(i);
						//to find the new bucket, 2^(globalD-1) ekle
						//insert to new bucket
						directory.get(1).pointer.addPoke(tempPoke);
					}
				}

				//4-Add the new entry
				int localDepth = directory.get(0).pointer.localD;
				String prefixOfPoke = (poke.getHash()).substring(0, localDepth);

				if (prefixOfPoke.equals(firstHash) && directory.get(0).pointer.isEmpty()) {
					directory.get(0).pointer.addPoke(poke);
				} else if (prefixOfPoke.equals(nextHash) && directory.get(1).pointer.isEmpty()) {
					directory.get(1).pointer.addPoke(poke);
				} else {
					this.addPoke(poke);
				}

			}

		} else {
			//All cases except initial
			//We have hashValue, bin and prefix
			//a) if related bucket is empty, put the poke inside and increment currSize
			CengHashRow relatedRow = findByHash(prefix);
			/*
			printRow(relatedRow);
			System.out.println("isEmpty: "+ relatedRow.pointer.isEmpty()+" prefix: "+prefix+" globalD: "+globalD);*/
			if (relatedRow.pointer.isEmpty()) {
				relatedRow.pointer.bucket[directory.get(0).pointer.currSize] = poke;
				relatedRow.pointer.currSize++;
			}
			//b) if bucket is not empty
			else {
				Integer dirSize = (int) Math.pow(2, globalD);
				//globalD*=2;//TODO:Carry this tto doubling directory block

				//2-Allocate new page: By default, we will duplicate it anyway but we should not
				//Check if doubling directory is necessary,
				if (relatedRow.pointer.localD == globalD) {
					//double the directory
					//only increment local depths of full and new bucket

					this.globalD=this.globalD+1;
					for (int i = 0; i < dirSize; i++) {
						CengHashRow tempRow = new CengHashRow(directory.get(i).pointer, directory.get(i).hash_prefix);
						String oldhash = directory.get(i).hash_prefix;
						directory.get(i).hash_prefix = oldhash + "0";
						tempRow.hash_prefix = oldhash + "1";
						//if it is related rows, increment the localD and reset
						if (directory.get(i).hash_prefix.equals(relatedRow.hash_prefix)) {
							directory.get(i).pointer.localD++;
							tempRow.pointer = null;
							tempRow.pointer = new CengBucket(directory.get(i).pointer.localD);
						}
						directory.add(tempRow);
					}
					//Redistribution
					String hashOfTargetRow = (relatedRow.hash_prefix).substring(0,globalD-1) + "1";
					CengHashRow targetRow = this.findByHash(hashOfTargetRow);

					Integer len = relatedRow.pointer.currSize;
					String nextHash = targetRow.hash_prefix;

					//Loop through the elements of buckets and check their prefix
					for(int j=0;j<len;j++){
						int localDepth = relatedRow.pointer.localD;
						if(relatedRow.pointer.bucket[j] != null){
							String prefixOfPoke = (relatedRow.pointer.bucket[j].getHash()).substring(0,localDepth);
							if(prefixOfPoke.equals(nextHash)){
								//Transfer the poke to the next bucket
								CengPoke pekka = relatedRow.pointer.bucket[j];
								Integer key = pekka.pokeKey();String name= pekka.pokeName();
								String power= pekka.pokePower();String type= pekka.pokeType();
								CengPoke tempPoke = new CengPoke(key,name,power,type);
								//delete from old bucket
								relatedRow.pointer.deletePokebyIndex(j);
								//to find the new bucket, 2^(globalD-1) ekle
								//insert to new bucket
								targetRow.pointer.addPoke(tempPoke);
							}
						}

					}

					//4-Add the new entry
					int localDepth = relatedRow.pointer.localD;
					String prefixOfPoke = (poke.getHash()).substring(0, localDepth);
					if (prefixOfPoke.equals(relatedRow.hash_prefix) && relatedRow.pointer.isEmpty()) {
						relatedRow.pointer.addPoke(poke);
					} else if (prefixOfPoke.equals(targetRow.hash_prefix) && targetRow.pointer.isEmpty()) {
						targetRow.pointer.addPoke(poke);
					} else {
						this.addPoke(poke);
					}


				} else {
					//look for other row . we can use to find the new bucket, 2^(globalD-1) ekle
					//check if it points to the same pointer
					String hashOfTargetRow = (relatedRow.hash_prefix).substring(0,globalD-1) + "1";
					CengHashRow conjRow = this.findByHash(hashOfTargetRow);
					boolean pointtoSame = conjRow.pointer == relatedRow.pointer;
					if(pointtoSame){
						//Reset the second
						relatedRow.pointer.localD++;
						conjRow.pointer = new CengBucket(relatedRow.pointer.localD);
						//Redistribute between

						Integer len = relatedRow.pointer.currSize;
						String nextHash = conjRow.hash_prefix;

						//Loop through the elements of buckets and check their prefix
						for(int j=0;j<len;j++){
							int localDepth = relatedRow.pointer.localD;
							String prefixOfPoke = (relatedRow.pointer.bucket[j].getHash()).substring(0,localDepth);
							if(prefixOfPoke.equals(nextHash)){
								//Transfer the poke to the next bucket
								CengPoke pekka = relatedRow.pointer.bucket[j];
								Integer key = pekka.pokeKey();String name= pekka.pokeName();
								String power= pekka.pokePower();String type= pekka.pokeType();
								CengPoke tempPoke = new CengPoke(key,name,power,type);
								//delete from old bucket
								relatedRow.pointer.deletePokebyIndex(j);
								//to find the new bucket, 2^(globalD-1) ekle
								//insert to new bucket
								conjRow.pointer.addPoke(tempPoke);
							}
						}

						//Add the element
						//4-Add the new entry
						int localDepth = relatedRow.pointer.localD;
						String prefixOfPoke = (poke.getHash()).substring(0, localDepth);

						if (prefixOfPoke.equals(relatedRow.hash_prefix) && relatedRow.pointer.isEmpty()) {
							relatedRow.pointer.addPoke(poke);
						} else if (prefixOfPoke.equals(conjRow.hash_prefix) && conjRow.pointer.isEmpty()) {
							conjRow.pointer.addPoke(poke);
						} else {
							//this.addPoke(poke);
							this.globalD=this.globalD+1;
							for (int i = 0; i < dirSize; i++) {
								CengHashRow tempRow = new CengHashRow(directory.get(i).pointer, directory.get(i).hash_prefix);
								String oldhash = directory.get(i).hash_prefix;
								directory.get(i).hash_prefix = oldhash + "0";
								tempRow.hash_prefix = oldhash + "1";
								//if it is related rows, increment the localD and reset
								if (directory.get(i).hash_prefix.equals(relatedRow.hash_prefix)) {
									directory.get(i).pointer.localD++;
									tempRow.pointer = null;
									tempRow.pointer = new CengBucket(directory.get(i).pointer.localD);
								}
								directory.add(tempRow);
							}
							//Redistribution
							hashOfTargetRow = (relatedRow.hash_prefix).substring(0,globalD-1) + "1";
							CengHashRow targetRow = this.findByHash(hashOfTargetRow);

							 len = relatedRow.pointer.currSize;
							 nextHash = targetRow.hash_prefix;

							//Loop through the elements of buckets and check their prefix
							for(int j=0;j<len;j++){
								 localDepth = relatedRow.pointer.localD;
								if(relatedRow.pointer.bucket[j] != null){
									 prefixOfPoke = (relatedRow.pointer.bucket[j].getHash()).substring(0,localDepth);
									if(prefixOfPoke.equals(nextHash)){
										//Transfer the poke to the next bucket
										CengPoke pekka = relatedRow.pointer.bucket[j];
										Integer key = pekka.pokeKey();String name= pekka.pokeName();
										String power= pekka.pokePower();String type= pekka.pokeType();
										CengPoke tempPoke = new CengPoke(key,name,power,type);
										//delete from old bucket
										relatedRow.pointer.deletePokebyIndex(j);
										//to find the new bucket, 2^(globalD-1) ekle
										//insert to new bucket
										targetRow.pointer.addPoke(tempPoke);
									}
								}

							}

							//4-Add the new entry
							 localDepth = relatedRow.pointer.localD;
							 prefixOfPoke = (poke.getHash()).substring(0, localDepth);
							if (prefixOfPoke.equals(relatedRow.hash_prefix) && relatedRow.pointer.isEmpty()) {
								relatedRow.pointer.addPoke(poke);
							} else if (prefixOfPoke.equals(targetRow.hash_prefix) && targetRow.pointer.isEmpty()) {
								targetRow.pointer.addPoke(poke);
							} else {
								this.addPoke(poke);
							}
						}


					}
				}
				/*
				//If it is not, find the other row to allocate and reset its bucket pointer
				System.out.println("GlobalD: "+globalD+" localD: "+relatedRow.pointer.localD);
				for(int i=0;i<dirSize;i++){
					//1-Increment the local depth
					directory.get(i).pointer.localD++;
					//Copy the each row in the old table
					CengHashRow tempRow = new CengHashRow(directory.get(i).pointer, directory.get(i).hash_prefix);
					String oldhash = directory.get(i).hash_prefix;
					directory.get(i).hash_prefix= oldhash + "0";
					tempRow.hash_prefix = oldhash + "1";

					tempRow.pointer = new CengBucket(directory.get(i).pointer.localD);

					//3-Redistribute the values
					//Find the row to be inserted by replacing the 0 at the beginning by 1
					Integer len = directory.get(i).pointer.currSize;
					String firstHash = directory.get(i).hash_prefix;
					Integer strlen = (directory.get(i).hash_prefix).length();
					String nextHash = tempRow.hash_prefix;//firstHash.substring(1, strlen) + "1";

					//Loop through the elements of buckets and check their prefix
					for(int j=0;j<len;j++){
						boolean isReseted = false;
						System.out.println(i+" " + j + " -------------------------------------------- BEGINNNING DEFAULT:");
						printRow(directory.get(i));
						System.out.println(i+" " + j + " -------------------------------------------- TEMPROW BEFORE:");
						printRow(tempRow);
						int localDepth = directory.get(i).pointer.localD;
						String prefixOfPoke = (directory.get(i).pointer.bucket[j].getHash()).substring(0,localDepth);
						System.out.println("prefixOfPoke: "+prefixOfPoke+" nextHash: "+nextHash+" isEqual: "+prefixOfPoke.equals(nextHash));
						if(prefixOfPoke.equals(nextHash)){
							//Transfer the poke to the next bucket
							if(!isReseted){
								System.out.println("Reseted the bucket"+tempRow.hash_prefix);

								isReseted = true;
							}
							CengPoke pekka = directory.get(i).pointer.bucket[j];
							Integer key = pekka.pokeKey();String name= pekka.pokeName();
							String power= pekka.pokePower();String type= pekka.pokeType();
							CengPoke tempPoke = new CengPoke(key,name,power,type);
							//delete from old bucket
							directory.get(i).pointer.deletePokebyIndex(j);
							//to find the new bucket, 2^(globalD-1) ekle
							//insert to new bucket
							tempRow.pointer.addPoke(tempPoke);
						}
						System.out.println(i+" " + j + " -------------------------------------------- TEMPROW AFTER:");
						printRow(tempRow);
					}

					//4-Add the new entry
					int localDepth = tempRow.pointer.localD;
					String prefixOfPoke = (poke.getHash()).substring(0,localDepth);

					if(prefixOfPoke.equals(firstHash) && directory.get(i).pointer.isEmpty()){
						System.out.println("HERE IS THE NEW ENTRY: "+prefixOfPoke+" firstHash: "+firstHash+" nextHash: "+nextHash);
						directory.get(0).pointer.addPoke(poke);
					} else if(prefixOfPoke.equals(nextHash) && tempRow.pointer.isEmpty()) {
						System.out.println("HERE IS THE NEW ENTRY: "+prefixOfPoke+" firstHash: "+firstHash+" nextHash: "+nextHash);
						tempRow.pointer.addPoke(poke);
					}
					else {
						//this.addPoke(poke);
					}

					directory.add(tempRow);
				}

				//4-Add the new entry
				//TODO:We dont know where to duplicate(We might compare the local depth and global depth to decide)
				//TODO:Sort the rows at the end

			}
			*/

			}


			// TODO: Empty Implementation
		}

		sortTheDirectory();
	}

	public void searchPoke(Integer pokeKey)
	{
		Integer i = 0,j = 0,indicator = 0;
		System.out.print("\"search\": {\n\t");
		while(i != Math.pow(2,this.globalD)){
			j = 0;
			while(j < directory.get(i).pointer.currSize){
				if(directory.get(i).pointer.bucket[j].pokeKey() == pokeKey){
					if(indicator>0){
						System.out.print(",\n\t");
					}
					indicator++;
					CengHashRow relatedRow = directory.get(i);
					int k=0;

					System.out.print("\"row\": {\n\t\t");
					System.out.print("\"hashPref\": " + String.valueOf(relatedRow.hash_prefix) + ",\n\t\t");
					System.out.print("\"bucket\": {\n\t\t\t");
					System.out.print("\"hashLength\": " + String.valueOf(relatedRow.pointer.localD) + ",\n\t\t\t");
					System.out.print("\"pokes\": [\n\t\t\t\t");
					while(k < relatedRow.pointer.currSize){
						System.out.print("\"poke\": {\n\t\t\t\t\t");
						System.out.print("\"hash\": "+(relatedRow.pointer.bucket[k].getHash())+",\n\t\t\t\t\t");
						System.out.print("\"pokeKey\": " + String.valueOf(relatedRow.pointer.bucket[k].pokeKey()) + ",\n\t\t\t\t\t");
						System.out.print("\"pokeName\": " + relatedRow.pointer.bucket[k].pokeName() + ",\n\t\t\t\t\t");
						System.out.print("\"pokePower\": " + relatedRow.pointer.bucket[k].pokePower() + ",\n\t\t\t\t\t");
						System.out.print("\"pokeType\": " + relatedRow.pointer.bucket[k].pokeType() + "\n\t\t\t\t");
						//System.out.print("}\n\t\t\t");
						if(k!=relatedRow.pointer.currSize-1){
							System.out.print("},\n\t\t\t");
						}else{
							System.out.print("}\n\t\t\t");
						}
						k++;
					}


					System.out.print("]\n\t\t");
					System.out.print("}\n\t");
					System.out.print("}");

				}

				j++;
			}

			i++;
		}
		System.out.print("\n}\n");

		// TODO: Empty Implementation
	}

	public void print()
	{
		System.out.print("\"table\": {\n\t");
		Integer i = 0,j = 0;
		while(i != Math.pow(2,this.globalD)){
			System.out.print("\"row\": {\n\t\t");
			System.out.print("\"hashPref\": " + String.valueOf(directory.get(i).hash_prefix) + ",\n\t\t");
			System.out.print("\"bucket\": {\n\t\t\t");
			System.out.print("\"hashLength\": " + String.valueOf(directory.get(i).pointer.localD) + ",\n\t\t\t");
			System.out.print("\"pokes\": [\n\t\t\t\t");
			j = 0;
			while(j < directory.get(i).pointer.currSize){
				if(j>0){ System.out.print("\t");}
				System.out.print("\"poke\": {\n\t\t\t\t\t");
				System.out.print("\"hash\": "+(directory.get(i).pointer.bucket[j].getHash())+",\n\t\t\t\t\t");
				System.out.print("\"pokeKey\": " + String.valueOf(directory.get(i).pointer.bucket[j].pokeKey()) + ",\n\t\t\t\t\t");
				System.out.print("\"pokeName\": " + directory.get(i).pointer.bucket[j].pokeName() + ",\n\t\t\t\t\t");
				System.out.print("\"pokePower\": " + directory.get(i).pointer.bucket[j].pokePower() + ",\n\t\t\t\t\t");
				System.out.print("\"pokeType\": " + directory.get(i).pointer.bucket[j].pokeType() + "\n\t\t\t\t");

				if(j!=directory.get(i).pointer.currSize-1){
					System.out.print("},\n\t\t\t");
				}else{
					System.out.print("}\n\t\t\t");
				}
				j++;
			}


			System.out.print("]\n\t\t");
			System.out.print("}\n\t");
			if(i!=Math.pow(2,this.globalD)-1){
				System.out.print("},\n\t");
			}else{
				System.out.print("}\n");
			}
			i+=1;
		}
		System.out.print("}\n");
	}

	// GUI-Based Methods
	// These methods are required by GUI to work properly.

	public int prefixBitCount()
	{
		// TODO: Return table's hash prefix length.
		return 0;
	}

	public int rowCount()
	{
		// TODO: Return the count of HashRows in table.
		return 0;
	}

	public CengHashRow rowAtIndex(int index)
	{
		// TODO: Return corresponding hashRow at index.
		return null;
	}

	// Own Methods

	public CengHashRow findByHash(String hash){
		int len=directory.size();
		for(int i=0; i<len; i++) {
			if (directory.get(i).hash_prefix.equals(hash)) {
				return directory.get(i);
			}
		}
		return null;
	}

	public void printRow(CengHashRow relatedRow){
		int j=0;
		System.out.print("\"search\": {\n\t");
		System.out.print("\"row\": {\n\t\t");
		System.out.print("\"hashPref\": " + String.valueOf(relatedRow.hash_prefix) + ",\n\t\t");
		System.out.print("\"bucket\": {\n\t\t\t");
		System.out.print("\"hashLength\": " + String.valueOf(relatedRow.pointer.localD) + ",\n\t\t\t");
		System.out.print("\"pokes\": [\n\t\t\t\t");
		while(j < relatedRow.pointer.currSize){
			System.out.print("\"poke\": {\n\t\t\t\t\t");
			System.out.print("\"hash\": "+(relatedRow.pointer.bucket[j].getHash())+",\n\t\t\t\t\t");
			System.out.print("\"pokeKey\": " + String.valueOf(relatedRow.pointer.bucket[j].pokeKey()) + ",\n\t\t\t\t\t");
			System.out.print("\"pokeName\": " + relatedRow.pointer.bucket[j].pokeName() + ",\n\t\t\t\t\t");
			System.out.print("\"pokePower\": " + relatedRow.pointer.bucket[j].pokePower() + ",\n\t\t\t\t\t");
			System.out.print("\"pokeType\": " + relatedRow.pointer.bucket[j].pokeType() + "\n\t\t\t\t");
			System.out.print("}\n\t\t\t");
			if(j!=relatedRow.pointer.currSize-1){
				System.out.print("},\n\t\t\t");
			}else{
				System.out.print("}\n\t\t\t");
			}
			j++;
		}


		System.out.print("]\n\t\t");
		System.out.print("}\n\t");
		System.out.print("}\n");
		System.out.print("}\n");
	}

	public double customLog(double base, double logNumber) {
		return Math.log(logNumber) / Math.log(base);
	}

	public String prepareBin(Integer hashValue){
			Integer len = (int)customLog(2,CengPokeKeeper.getHashMod());
			String res = Integer.toBinaryString(hashValue % CengPokeKeeper.getHashMod());
			if(res.length() < len){
				int dif = (int)len - res.length();
				for(int i=0;i<dif;i++){
					res = "0"+ res;
				}
			}
			return res;

	}

	public void sortTheDirectory(){
		int i, key, j;
		CengHashRow keytemp;
		int len = (int)Math.pow(2,this.globalD);
		for (i = 1; i < len; i++)
		{
			key = Integer.parseInt(directory.get(i).hash_prefix,2);
			keytemp = directory.get(i);
			j = i - 1;

        /* Move elements of arr[0..i-1], that are
        greater than key, to one position ahead
        of their current position */
			while (j >= 0 && Integer.parseInt(directory.get(j).hash_prefix,2) > key)
			{
				directory.set(j+1,directory.get(j));
				j = j - 1;
			}
			directory.set(j+1,keytemp);
		}






	}

}
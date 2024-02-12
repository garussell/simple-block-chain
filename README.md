# Simple blockchain

An exercise to help individuals understand the basics of blockchain.

### The Exercise

The exercise - download the codebase and get the tests to pass!

- Start with the hash implementation (previousHash + timestamp + nonce).
- Move to isEmpty and Size.
- Ensure the negative tests pass!

Hope you enjoy the exercise!

Thanks, @barinek

© 2022 by Continuum Collective, Inc. All rights reserved.


### Documenting my progress:

This is what I did to get the tests to pass.

1. Altered Block.java `this.hash = calculatedHash();` and update that function:  
```
public String calculatedHash() throws NoSuchAlgorithmException {

	String data = previousHash + Long.toString(timestamp) + Integer.toString(nonce);

	return calculateHash(data);

}
```
- The method was returning 'null' out of the box.  By changing the constructor in the `Block.java` class from `this.hash = null` to `this.hash = calculatedHash();`, the value of `.hash` would call the `calculatedHash()` method.  That method concatenates the block properties which are specified in `Block.java` (`previousHash`, `timestamp`, and `nonce`)into a string `data`.  
- Using the `java.security.NoSuchAlgorithmException` import, the `calculatedHash()` method is able to compute the SHA-256 hash, which has the data uniqueness from the inputs to the `data` string.  Any small change to the block's properties will result in a completely different return value.  It is therefore immutable, which is crucial for the integrity and security of the blockchain.

2. I adjusted the `inEmpty()` method to return false only if the `size()` is empty:
```
public boolean isEmpty() {

	return size() == 0;

}
```
- This led to a failing test assertion for `size()`, because it was hard-coded to return `0` for all scenarios.  Size should reflect the `size()` of the blockchain.  
- Adjusting the `size()` method to be a true reflection of the size raised the issue of not having a storage system for the Blocks.  After some quick research, I found that in the real world, LinkedLists are an appropriate data structure for this simple implementation of a blockchain.  
- I imported `java.util.LinkedList;` add the `.blocks` variable, and used that to furnish the `.size()` and `.add()` methods.

3. The `.isValid()` method was the next @Test to throw errors.  Out of the box, this method contained some commented out pseudo code that was useful in figuring out the logic.  Since we now have to traverse the LinkedList, we will need to utilize a `for` loop.  
- Looping was utilized to 1. check if blocks are already mined 2. check if it matches previous hashes, and 3. make sure the hash has the correct calculation by comparison to existing hash.  All of those 3 conditions must return `true` in order to be valid.
   
```
public boolean isValid() throws NoSuchAlgorithmException {

	// Check mined

	for (Block block : blocks) {

		if (!isMined(block)) {

		  return false; // Block is not mined

		}

	}

  // Check previous hash matches

	for (int i = 1; i < blocks.size(); i++) {

		if (!blocks.get(i).getPreviousHash().equals(blocks.get(i - 1).getHash())) {

		  return false; // Previous hash does not match

		}

	}

  // Check hash is correctly calculated

	for (Block block : blocks) {
	
		if (!block.getHash().equals(block.calculatedHash())) {
		
		  return false; // Hash is not correctly calculated
		
		}
	
	}

	return true; // All checks passed

}
```

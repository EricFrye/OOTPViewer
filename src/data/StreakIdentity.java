package data;

public class StreakIdentity {
	
	private String [] identity;
	
	public StreakIdentity (String [] identity) {
		this.identity = identity;
	}
	
	public boolean equals (Object other) {
		
		if (other instanceof StreakIdentity) {
			
			StreakIdentity otherStreak = (StreakIdentity)other;
			
			if (this.identity.length != otherStreak.length()) {
				return false;
			}
			
			for (int curIdentityFieldIndex = 0; curIdentityFieldIndex < this.identity.length; curIdentityFieldIndex++) {
				
				//if there is any mismatch in an identity field than it is different
				if (!this.identity[curIdentityFieldIndex].equals(otherStreak.identity[curIdentityFieldIndex])) {
					return false;
				}
				
			}
			
			return true;
			
		}
		
		else {
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param index
	 * @return The identity value at the given index, or an empty string if the index is out of bounds
	 */
	public String getIdentityValue (int index) {
		
		if (index < 0 || index >= this.identity.length) {
			return "";
		}
		
		else {
			return this.identity[index];
		}
		
	}
	
	public int length () {
		return this.identity.length;
	}
	
}

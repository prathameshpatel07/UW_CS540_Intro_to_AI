public class Helper {
	
	/** 
    * Class constructor.
    */
	private Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		
		// TODO Add your code here
		int inum;
		boolean prime=true;
		if (x==0 || x==1) prime = false;
		else if(x == 2) prime = true;
		else {
			for(int i=2; i<=x/2; i++)
			{
				inum= x % i;
				if(inum==0)
				{
					prime=false;
					break;
				}
			}
		}
		return prime;
	}

	/**
	* This method is used to get the largest prime factor 
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {

    	// TODO Add your code here
		int i;
		int copyx;
		int largestprime;
		copyx = x;
		largestprime = x;
		
		while(copyx % 2 == 0) {
			largestprime = 2;
			copyx >>= 1;
		}
		for (i = 3; i <= copyx; i+=2) { 
			while (copyx % i == 0) {
				largestprime = i;
				copyx = copyx/i;
			}
		}

		
		return largestprime;
	}
}
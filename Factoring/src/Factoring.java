import java.util.Scanner;
//author Fredrik Hernqvist

public class Factoring {
	public static void main(String[] Args)
	{
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.print("Enter one number to get its factors and two numbers to get their gcd." +
					"\nAll numbers must be between 1 and 100. Type \"quit\" to exit." +
					"\n> ");
			String in = scanner.nextLine();
			if (in.equalsIgnoreCase("Quit")) //if you type quit
				break; //break out of loop
			try {
				if (in.indexOf(' ') == -1) //if there is no space...
				{
					int n = Integer.valueOf(in);
					if (!valid(n))
						continue;
					System.out.println(factors(n)); //...were finding factors
				}
				else //otherwise...
				{
					int index = in.indexOf(' '); //....parse the substrings on each side of the space...
					int a = Integer.valueOf(in.substring(0, index));
					int b = Integer.valueOf(in.substring(index+1, in.length()));
					if (!valid(a) || !valid(b))
						continue;
					System.out.println(gcd(a, b));//...to find their gcd
				}
			} catch (Exception e){ // errors shold be caused by invalid input
				System.out.println("Error");
			}
		}
	}
	
	public static boolean valid (int n) // returns false and displays error if n < 1 or 100 < n
	{
		if (n < 1 || n > 100)
		{
			System.out.println("Number out of bounds.");//prints the error
			return false;
		}
		return true;
	}
	
	public static String factors (int n) //returns all factors of n
	{
		String str = "";//dont actually think we gain any time by calculating sqrt for numbers <= 100, better to just loop through them all
		for (int i = 1; i < n; i ++)
			if (n%i == 0)
				str += (String)(i+" ");
		return (str+n);
	}
	
	public static int gcd(int a, int b) // returns gcd of a and b
	{
		for (int i = Math.min(a, b); i > 0; i--)
			if (a%i == 0 && b%i == 0)
				return i;
		return 1;
	}
}

import static org.junit.Assert.*;

import org.junit.Test;


public class FactoringTest {

	@Test
	public void testFactors() {
		assertEquals("1", Factoring.factors(1));
		assertEquals("1 2", Factoring.factors(2));
		assertEquals("1 2 4 5 10 20 25 50 100", Factoring.factors(100));
	}

	@Test
	public void testGcd() {
		assertEquals(1, Factoring.gcd(1, 100));
		assertEquals(100, Factoring.gcd(100, 100));
		assertEquals(4, Factoring.gcd(4, 16));
		assertEquals(1, Factoring.gcd(1, 1));
	}

}

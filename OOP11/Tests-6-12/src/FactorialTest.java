import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactorialTest {

    Factorial Factorial;

    @BeforeEach
    void setUp() {
        Factorial = new Factorial();
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void factorialNumber() {
        assertEquals(24, Factorial.factorial(4));
    }

    @Test
    void factorialZero() {
        int fact=1;
        int number=0;
        fact = Factorial.factorial(number);
        //System.out.println("Factorial of "+number+" is: "+fact);

        assertEquals(0, fact);
    }

    @Test
    void factorialOne() {
        int fact=1;
        int number=1;
        fact = Factorial.factorial(number);
        //System.out.println("Factorial of "+number+" is: "+fact);

        assertEquals(1, fact);
    }
}
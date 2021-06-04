import me.darrionat.matrixlib.algebra.sets.Complex;
import me.darrionat.matrixlib.algebra.sets.Rational;

import java.math.BigInteger;

public class TestPowers {
    public static void main(String[] args) {
        // Raising a complex value to a power can be done extremely fast time
        long start = System.currentTimeMillis();
        Complex c = new Complex(new Rational(new BigInteger("3"), BigInteger.ONE), new Rational(new BigInteger("4"), BigInteger.ONE));
        System.out.println(c.pow(10000));
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
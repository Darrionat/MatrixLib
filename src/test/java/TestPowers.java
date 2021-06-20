import me.darrionat.matrixlib.algebra.Expression;

public class TestPowers {
    public static void main(String[] args) {
        evaluate("3-4*5+7+2^3");
        evaluate("3+4i-81*4+4i");
        evaluate("(3+12)^2");
        evaluate("4(5)");
        evaluate("23(3)");
        evaluate("8^(3/2)");
        evaluate("(3/5)*(2/3)");
    }

    private static void evaluate(String s) {
        long start = System.currentTimeMillis();
        System.out.println(s + "=" + Expression.buildExpression(s).evaluate() + "\t" + (System.currentTimeMillis() - start) + "ms");
    }
}
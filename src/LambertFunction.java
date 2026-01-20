import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LambertFunction {
    final static double e = 1E-100;
    private static final MathContext MC = new MathContext(100, RoundingMode.HALF_UP);
    private static final BigDecimal EPSILON = new BigDecimal("1E-100");

    public static Double findLambertFunctionRoot(Double functionValue, Double approxSolution) {
        double diff = Double.POSITIVE_INFINITY;
        double count = 0.0d;
        while (diff > e) {

            Double functionExpression = approxSolution * (Math.pow(Math.E, approxSolution)) - functionValue;

            Double derivativeOfFunctionExpression = approxSolution * (Math.pow(Math.E, approxSolution)) + Math.pow(Math.E, approxSolution);
            Double nextApproxSolution = approxSolution - (functionExpression / derivativeOfFunctionExpression);
            diff = Math.abs(nextApproxSolution - approxSolution);
            approxSolution = nextApproxSolution;
            count++;
        }
        IO.println("\n" + count + "\n");
        IO.println("\ndiff " + diff + "\n");
        return approxSolution;

    }


    public static BigDecimal findLambertFunctionRoot(BigDecimal functionValue, BigDecimal approxSolution) {
        BigDecimal delta = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal count = BigDecimal.valueOf(0);

        while (delta.compareTo(EPSILON) > 0) {
            // e^x calculation using double for simplicity, or replace with a Taylor series for higher precision
            BigDecimal expX = BigDecimal.valueOf(Math.exp(approxSolution.doubleValue()));

            // functionExpression = approxSolution * expX - functionValue
            BigDecimal nextApproxSolution = getNextApproxSolution(functionValue, approxSolution, expX);

            delta = nextApproxSolution.subtract(approxSolution, MC).abs();
            approxSolution = nextApproxSolution;
            count = count.add(BigDecimal.ONE);

        }
        IO.println("\n" + count + "\n");
        IO.println("\ndelta " + delta + "\n");
        return approxSolution;
    }

    private static BigDecimal getNextApproxSolution(BigDecimal functionValue, BigDecimal approxSolution, BigDecimal expX) {
        BigDecimal functionExpression = approxSolution.multiply(expX, MC).subtract(functionValue, MC);

        // derivativeOfFunctionExpression = approxSolution * expX + expX
        BigDecimal derivativeOfFunctionExpression = approxSolution.multiply(expX, MC).add(expX, MC);

        // nextApproxSolution = approxSolution - (functionExpression / derivativeOfFunctionExpression)
        return approxSolution.subtract(functionExpression.divide(derivativeOfFunctionExpression, MC), MC);
    }


}

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Series {

    private static final MathContext MC = new MathContext(100, RoundingMode.HALF_UP);
    private static final BigDecimal EPSILON = new BigDecimal("1E-10");

    public static BigDecimal findBaselSolution() {
        BigDecimal start = BigDecimal.ONE;
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal value =BigDecimal.ONE;
        while (value.compareTo(EPSILON) > 0) {
            BigDecimal denominator = start.multiply(start);
            value=BigDecimal.ONE.divide(denominator,MC);
            sum = sum.add(value,MC);
            start = start.add(BigDecimal.ONE);
        }
        IO.println("\n" + start + "\n");
        IO.println("\n" + sum + "\n");
        return sum;
    }
}

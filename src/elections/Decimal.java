package elections;

import java.math.BigInteger;
import java.util.Objects;

public class Decimal implements Comparable<Decimal> {

    private static final int numdigits = 9;
    private static final BigInteger denom = BigInteger.TEN.pow(numdigits);
    public static final Decimal ZERO = new Decimal(BigInteger.ZERO);
    public static final Decimal ONE = new Decimal(denom);

    private final BigInteger val;

    public Decimal(BigInteger val) {
        this.val = val;
    }

    public static Decimal valueOf(long n) {
        return new Decimal(BigInteger.valueOf(n).multiply(denom));
    }

    public BigInteger val() {
        return val;
    }

    public int signum() {
        return val.signum();
    }

    public Decimal add(Decimal d) {
        return new Decimal(val.add(d.val));
    }

    public Decimal subtract(Decimal d) {
        return new Decimal(val.subtract(d.val));
    }

    public Decimal multiplydown(Decimal d) {
        return new Decimal(divdown(val.multiply(d.val), denom));
    }

    public Decimal multiplyup(Decimal d) {
        return new Decimal(divup(val.multiply(d.val), denom));
    }

    public Decimal dividedown(Decimal d) {
        return new Decimal(divdown(val.multiply(denom), d.val));
    }

    public Decimal divideup(Decimal d) {
        return new Decimal(divup(val.multiply(denom), d.val));
    }

    private static BigInteger divdown(BigInteger a, BigInteger b) {
        if (b.signum() == 0) {
            throw new IllegalArgumentException();
        }
        if (a.signum() == 0) {
            return BigInteger.ZERO;
        }
        if (a.signum() * b.signum() > 0) {
            return a.abs().divide(b.abs());
        }
        return a.abs().add(b.abs()).subtract(BigInteger.ONE).divide(b.abs()).negate();
    }

    private static BigInteger divup(BigInteger a, BigInteger b) {
        if (b.signum() == 0) {
            throw new IllegalArgumentException();
        }
        if (a.signum() == 0) {
            return BigInteger.ZERO;
        }
        if (a.signum() * b.signum() < 0) {
            return a.abs().divide(b.abs()).negate();
        }
        return a.abs().add(b.abs()).subtract(BigInteger.ONE).divide(b.abs());
    }

    @Override
    public int compareTo(Decimal d) {
        return val.compareTo(d.val);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.val);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Decimal other = (Decimal) obj;
        return Objects.equals(this.val, other.val);
    }

    @Override
    public String toString() {
        String integerpart = val.divide(denom).toString();
        String decimalpart = val.mod(denom).add(denom).toString().substring(1);
        return integerpart + "." + decimalpart;
    }
}

package officedraw;

import java.math.BigInteger;
import java.util.Objects;

public class BigFraction implements Comparable<BigFraction> {

    public static final BigFraction ZERO = new BigFraction(0);
    public static final BigFraction ONE = new BigFraction(1);

    private final BigInteger top;
    private final BigInteger bottom;

    public BigFraction(String s) {
        if (s.matches("^-?\\d+$")) {
            top = new BigInteger(s);
            bottom = BigInteger.ONE;
        } else if (s.matches("^-?\\d+/\\d+$")) {
            String[] a = s.split("/");
            BigInteger numerator = new BigInteger(a[0]);
            BigInteger denominator = new BigInteger(a[1]);
            if (denominator.signum() == 0) {
                throw new NumberFormatException();
            }
            BigInteger gcd = numerator.gcd(denominator);
            top = numerator.divide(gcd);
            bottom = denominator.divide(gcd);
        } else {
            throw new NumberFormatException();
        }
    }

    public BigFraction(BigInteger numerator, BigInteger denominator) {
        if (denominator.signum() == 0) {
            throw new IllegalArgumentException();
        }
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
        BigInteger gcd = numerator.gcd(denominator);
        top = numerator.divide(gcd);
        bottom = denominator.divide(gcd);
    }

    public BigFraction(BigInteger n) {
        this(n, BigInteger.ONE);
    }

    public BigFraction(long top, long bottom) {
        this(BigInteger.valueOf(top), BigInteger.valueOf(bottom));
    }

    public BigFraction(long n) {
        this(BigInteger.valueOf(n), BigInteger.ONE);
    }

    public BigFraction add(BigFraction f) {
        return new BigFraction(top.multiply(f.bottom).add(bottom.multiply(f.top)), bottom.multiply(f.bottom));
    }

    public BigFraction subtract(BigFraction f) {
        return new BigFraction(top.multiply(f.bottom).subtract(bottom.multiply(f.top)), bottom.multiply(f.bottom));
    }

    public BigFraction negate() {
        return new BigFraction(top.negate(), bottom);
    }

    public BigFraction multiply(BigFraction f) {
        return new BigFraction(top.multiply(f.top), bottom.multiply(f.bottom));
    }

    public BigFraction divide(BigFraction f) {
        return new BigFraction(top.multiply(f.bottom), bottom.multiply(f.top));
    }

    public BigFraction inverse() {
        return new BigFraction(bottom, top);
    }

    public BigInteger numerator() {
        return top;
    }

    public BigInteger denominator() {
        return bottom;
    }

    public int signum() {
        return top.signum();
    }

    @Override
    public int compareTo(BigFraction o) {
        return this.subtract(o).signum();
    }

    @Override
    public String toString() {
        return bottom.equals(BigInteger.ONE) ? top.toString() : top + "/" + bottom;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.top);
        hash = 47 * hash + Objects.hashCode(this.bottom);
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
        BigFraction other = (BigFraction) obj;
        return top.equals(other.top) && bottom.equals(other.bottom);
    }
}

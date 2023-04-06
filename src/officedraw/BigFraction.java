package officedraw;

import java.math.BigInteger;
import java.util.Objects;

public class BigFraction implements Comparable<BigFraction> {

    public static final BigFraction ZERO = new BigFraction(0);
    public static final BigFraction ONE = new BigFraction(1);

    private final BigInteger top;
    private final BigInteger bottom;

    public BigFraction(BigInteger top, BigInteger bottom) {
        BigInteger gcd = top.gcd(bottom);
        top = top.divide(gcd);
        bottom = bottom.divide(gcd);
        if (bottom.signum() < 0) {
            top = top.negate();
            bottom = bottom.negate();
        }
        this.top = top;
        this.bottom = bottom;
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

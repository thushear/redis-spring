package net.shopin.concurrency;

/**
 * OneValueCache
 * <p/>
 * Immutable holder for caching a number and its factors
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午1:48
 */
@Immutable
public class OneValueCache {

    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger lastNumber, BigInteger[] factors) {
        this.lastNumber = lastNumber;
        this.lastFactors = Arrays.copyOf(factors,factors.length);
    }

    public BigInteger[] getFactors(BigInteger i){
        if(lastNumber==null||!lastNumber.equals(i))
            return null;
        else
            return Arrays.copyOf(lastFactors,lastFactors.length);
    }


}

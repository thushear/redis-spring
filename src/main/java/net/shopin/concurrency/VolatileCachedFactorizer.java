package net.shopin.concurrency;
/**
 * VolatileCachedFactorizer
 * <p/>
 * Caching the last result using a volatile reference to an immutable holder object
 *
 * @author Brian Goetz and Tim Peierls
 */


import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午1:53
 */
@ThreadSafe
public class VolatileCachedFactorizer extends GenericServlet implements Servlet {

    private volatile OneValueCache cache = new OneValueCache(null,null);

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
        encodeIntoResponse(res, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[]{i};
    }
}

package net.shopin.concurrency;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Factorizer
 * <p/>
 * Factorizing servlet that caches results using Memoizer
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-12
 * Time: 下午5:18
 */
@ThreadSafe
public class Factorizer extends GenericServlet implements Servlet {

    private final Computalbe<BigInteger,BigInteger[]> c =
            new Computalbe<BigInteger, BigInteger[]>() {
                @Override
                public BigInteger[] compute(BigInteger arg) throws InterruptedException {
                    return factor(arg);  //To change body of implemented methods use File | Settings | File Templates.
                }
            };
    private final Computalbe<BigInteger,BigInteger[]> cache = new Memoizer<BigInteger, BigInteger[]>(c);



    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(resp, "factorization interrupted");
        }
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    void encodeError(ServletResponse resp, String errorString) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[]{i};
    }
}

package net.shopin.concurrency;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 说明:互斥锁的使用,线程安全,性能低下
 * User: kongming
 * Date: 14-6-10
 * Time: 下午3:47
 */
@ThreadSafe
public class SynchronizedFactorizer extends GenericServlet implements Servlet {

    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;


    @Override
    public synchronized void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }

    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] { i };
    }
}

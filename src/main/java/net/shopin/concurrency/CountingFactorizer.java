package net.shopin.concurrency;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 说明:通过给无状态的servlet组件引入线程安全的Atomic 原子性操作来使得
 *      servlet线程安全
 * User: kongming
 * Date: 14-6-10
 * Time: 下午3:29
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {

    private final AtomicLong count = new AtomicLong(0);

    public long getCount(){
        return count.get();
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(res, factors);

    }

    void encodeIntoResponse(ServletResponse res, BigInteger[] factors) {}
    BigInteger extractFromRequest(ServletRequest req) {return null; }
    BigInteger[] factor(BigInteger i) { return null; }
}

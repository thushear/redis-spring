package net.shopin.concurrency;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 无状态的对象永远是线程安全的
 * 说明:  a stateless servlet
 * User: kongming
 * Date: 14-6-10
 * Time: 下午3:09
 */
@ThreadSafe
public class StatelessFactorizer  extends GenericServlet implements Servlet {


    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(res, factors);
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

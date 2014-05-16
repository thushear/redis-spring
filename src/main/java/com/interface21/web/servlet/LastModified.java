package com.interface21.web.servlet;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-16
 * Time: 下午2:15
 */

import javax.servlet.http.HttpServletRequest;

/**
 * Same contract as for Servlet
 * Any resource can implement this.
 */
public interface LastModified {

    /**
     * Same contract as for Servlet.getLastModified
     * Invoked <b>before</b> request processing.
     */
    long getLastModified(HttpServletRequest request);

}

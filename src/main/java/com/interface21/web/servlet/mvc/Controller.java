package com.interface21.web.servlet.mvc;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-16
 * Time: 下午2:13
 */

import com.interface21.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple MVC controller. Analogous to the
 * WEBWORK OR MAVERICK class!?
 * <br/>The strength of this approach is its simplicity.
 * However, the downside is that too much processing often ends
 * up in the web tier.
 * @author Rod Johnson
 * @version $RevisionId$
 */
public interface Controller {

    /**
     * Process the request and return a ModelAndView object which the ControllerServlet
     * will render.
     * @return a ModelAndView for the ControllerServlet to render. A null
     * return is not an error. It indicates that this object completed
     * request processing itself, and there is no ModelAndView to render.
     */
    ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}

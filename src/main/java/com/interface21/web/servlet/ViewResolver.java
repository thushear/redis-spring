package com.interface21.web.servlet;

import com.interface21.context.ApplicationContextAware;

import javax.servlet.ServletException;
import java.util.Locale;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-16
 * Time: 上午11:30
 */
/**
 * Interface to be implemented by objects that can resolve views.
 * View state doesn't change during the running of the application,
 * so implementations are free to cache views.
 * @author  Rod Johnson
 * @version $Revision: 1.1.1.1 $
 */
public interface ViewResolver extends ApplicationContextAware {

    /** Resolve the given view by name
     * @param viewName name of the view to resolve
     * @param locale Locale in which to resolve the view. ViewResolvers
     * that support internationalization should respect this.
     * @throws ServletException if the view cannot be resolved.
     */
    View resolveViewname(String viewName, Locale locale) throws ServletException;


}

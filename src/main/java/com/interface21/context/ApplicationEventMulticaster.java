package com.interface21.context;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-13
 * Time: 上午11:37
 */
/**
 * Subclass of ApplicationListener to be implemented by
 * listeners that can broadcast events to other listeners.
 * @author  Rod Johnson
 * @since January 19, 2001
 * @version $RevisionId$
 */
public interface ApplicationEventMulticaster extends ApplicationListener{

    /**
     * Add a listener to be notified of all events
     * @param l listener to add
     */
    void addApplicationListener(ApplicationListener l);

    /**
     * Remove a listener in the notification list]
     * @param l listener to remove
     */
    void removeApplicationListener(ApplicationListener l);

    /**
     * Remove all listeners registered with this multicaster.
     * It will perform no action on event notification until more
     * listeners are registered.
     */
    void removeAllListeners();


}

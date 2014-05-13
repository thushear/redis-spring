package com.interface21.context;

import java.util.EventListener;

/**
 * Created by kongming on 14-5-12.
 */
public interface ApplicationListener extends EventListener {

    /**
     * Handle an application event
     * @param e event to respond to
     */
    void onApplicationEvent(ApplicationEvent e);



}

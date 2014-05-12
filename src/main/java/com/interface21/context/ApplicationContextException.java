package com.interface21.context;

import com.interface21.core.NestedRuntimeException;

/**
 * Created by kongming on 14-5-12.
 */
public class ApplicationContextException extends NestedRuntimeException{

    /**
     * Constructs an <code>ApplicationContextException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, Throwable t) {
        super(msg, t);
    }
}

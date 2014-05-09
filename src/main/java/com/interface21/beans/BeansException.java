/**
 * 
 */
package com.interface21.beans;

import com.interface21.core.NestedRuntimeException;

/**
 * @author kongming
 * Abstract superclass for all exceptions thrown in the beans package
 * and subpackages
 * @author Rod
 */
public abstract class BeansException extends NestedRuntimeException {

	 /**
     * Constructs an <code>ReflectionException</code> with the specified detail message.
     * @param msg the detail message.
     */
	public BeansException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}

	/**
     * Constructs an <code>ReflectionException</code> with the specified detail message.
     * @param msg the detail message.
     */
	public BeansException(String msg) {
		super(msg);
	}
}

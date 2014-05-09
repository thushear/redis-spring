/**
 * 
 */
package com.interface21.core;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author kongming
 *
 */
/**
 * Handy class for wrapping Exceptions so to allow a method to throw
 * just the wrapper and not have to throw a whole bunch of Exceptions.
 * (a la RemoteException)
 * printStackTrace() etc. are forwarded to the wrapped Exception.
 * Abstact to force the programmer to extend the class
 * @author Rod Johnson
 * @version $Id: NestedRuntimeException.java,v 1.1.1.1 2002/08/01 12:52:03 Rod Johnson Exp $
 */
public abstract class NestedRuntimeException extends RuntimeException implements HasRootCause {

	/**
	 * Nested Exception to hold wrapped exception
	 */
	private Throwable rootCause;

	/**
	  * Constructs a <code>ExceptionWrapperException</code> with the specified
	  * detail message.
	  * @param s the detail message
	  */
	public NestedRuntimeException(String s) {
		super(s);
	}

	/**
	   * Constructs a <code>RemoteException</code> with the specified
	   * detail message and nested exception.
	   *
	   * @param s the detail message
	   * @param ex the nested exception
	   */
	public NestedRuntimeException(String s, Throwable rootCause) {
		super(s);
		this.rootCause = rootCause;
	}

	/* (non-Javadoc)
	 * @see com.interface21.core.HasRootCause#getRootCause()
	 */
	/**
	 * may be null
	 */
	@Override
	public Throwable getRootCause() {
		return rootCause;
	}

	@Override
	public String getMessage() {
		if (rootCause == null)
			return super.getMessage();
		else
			return super.getMessage() + "; nested exception is :\n\t " + rootCause.toString();
	}

	
	/**
	 * Prints the composite message and the embedded stack trace to
	 * the specified stream <code>ps</code>.
	 * @param ps the print stream
	 */
	@Override
	public void printStackTrace(PrintStream s) {
		if (rootCause == null) {
			super.printStackTrace(s);
		} else {
			s.println(this);
			rootCause.printStackTrace(s);
		}
	}

	   /**
	    * Prints the composite message and the embedded stack trace to
	    * the specified print writer <code>pw</code>
	    * @param pw the print writer
	    */
	@Override
	public void printStackTrace(PrintWriter s) {
		if(rootCause == null){
			super.printStackTrace(s);
		}else{
			s.println(this);
			rootCause.printStackTrace(s);
		}
	}

	/**
	 * prints the composite message to <code>System.err</code>
	 */
	@Override
	public void printStackTrace() {
		printStackTrace(System.err);
	}
	
	

}

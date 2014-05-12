package com.interface21.beans;

import com.interface21.core.NestedCheckedException;

/**
 * Created with IntelliJ IDEA.
 * User: kongming
 * Date: 14-5-12
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public class TestFailedException extends NestedCheckedException {

    /**
     * The CVS version number.
     */
    public static final String REVISION_ID ="$Id: TestFailedException.java,v 1.3 2001/04/23 10:41:37 allanm Exp $";

    public TestFailedException( String message ) {
        super( message );
    }


    public TestFailedException( String message, Throwable t) {
        super( message, t);
    }

}

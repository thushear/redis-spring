package com.interface21.context;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-13
 * Time: 下午1:31
 */
/**
 * Subinterface of MessageSource to be implemented by objects that
 * can resolve messages hierarchically.
 * @author  Rod Johnson
 * @version $RevisionId$
 */
public interface NestingMessageSource extends MessageSource {

    /** Set the parent that will be used to try to resolve messages
     * that this object can't resolve.
     * @param parent parent MessageSource that will be used to try to resolve messages
     * that this object can't resolve. May be null, in which case]
     * no further resolution will be possible
     */
    void setParent(MessageSource parent);


}

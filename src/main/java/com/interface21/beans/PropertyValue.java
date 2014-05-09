/**
 * 
 */
package com.interface21.beans;
/**
 * Class to hold information and value for an individual property.
 * Using an object here, rather than just storing all properties in a 
 * map keyed by property name, allows for more flexibility, and the
 * ability to handle indexed properties etc. if necessary.
 * <br/>Note that the value doesn't need to be the final required type:
 * a BeanWrapper implementation should handle any necessary conversion,
 * as this object doesn't know anything about the objects it will be
 * applied to.
 * @author  Rod Johnson
 * @since 13 May 2001
 * @version $Id: PropertyValue.java,v 1.1.1.1 2002/08/01 12:52:02 Rod Johnson Exp $
 */
public class PropertyValue {
	
	//---------------------------------------------------------------------
    // Instance data
    //---------------------------------------------------------------------
	/**Property name*/
	private String name;
	/**value of the Property*/
	private Object value;
	
	//---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------
	/** Creates new PropertyValue 
     * @param name name of the property
     * @param value value of the property (possibly before type conversion)
     */
	public PropertyValue(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	
	
	//---------------------------------------------------------------------
    // Public methods
	//---------------------------------------------------------------------

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	 /** Return the value of the property
     * @return the value of the property. Type conversion
     * will probably not have occurred. It is the responsibility
     * of BeanWrapper implementations to perform type conversion.
     */
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}



	@Override
	public String toString() {
		return "PropertyValue [name=" + name + ", value=" + value + "]";
	}
	
	
	
	
	
}

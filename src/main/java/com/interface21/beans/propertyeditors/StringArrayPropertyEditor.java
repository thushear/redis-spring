/**
 * 
 */
package com.interface21.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

import com.interface21.util.StringUtils;

/**
 * @author kongming
 *
 */
/**
 * NB: must be registered
 * Is registered by BeanWrapperImpl
 * Format is defined in java.util.Properties documentation.
 * Each property must be on a new line.
 */
public class StringArrayPropertyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] sa = StringUtils.commaDelimitedListToStringArray(text);
		setValue(sa);
	}

	
	
}

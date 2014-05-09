/**
 * 
 */
package com.interface21.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Properties;
import java.util.StringTokenizer;

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
public class PropertiesEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Properties props = new Properties();
		//Zap whitespace
		StringTokenizer st = new StringTokenizer(text);
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			System.out.println("token = [" + token + "]");
			 // Tokens look like "/welcome.html=mainController"
			int eqpos = token.indexOf("=");
			String url = token.substring(0,eqpos);
			String beanName = token.substring(eqpos + 1);
			props.put(url, beanName);
		}
		
		setValue(props);
	}
	
	
	

}

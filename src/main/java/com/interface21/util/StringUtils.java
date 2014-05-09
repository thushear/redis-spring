/**
 * 
 */
package com.interface21.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kongming
 *
 */
@SuppressWarnings("rawtypes")
public abstract class StringUtils {
	
	
	/** Convenience method to return a Collection as a delimited (e.g. CSV)
	    * String. Useful for toString() implementations
	    * @param c Collection to display
	    * @param delim delimiter to use (probably a ,)
	    */
	    public static String collectionToDelimitedString(Collection c, String delim) {
	         if (c == null)
	            return "null";
	        return iteratorToDelimitedString(c.iterator(), delim);
	    }    // collectionToDelimitedString
	    /** 
	     * Convenience method to return a Collection as a delimited (e.g. CSV)
	    * String. Useful for toString() implementations
	    * @param c Collection to display
	    * @param delim delimiter to use (probably a ,)
	    */
	    public static String iteratorToDelimitedString(Iterator itr, String delim) {
	        if (itr == null)
	            return "null";
	        else {
	            StringBuffer sb = new StringBuffer();
	            int i = 0;
	            while (itr.hasNext()) {
	                if (i++ > 0)
	                    sb.append(delim);
	                sb.append(itr.next());
	            }
	            return sb.toString();
	        }
	    }    // collectionToDelimitedString

	/** Convenience method to return a String array as a delimited (e.g. CSV)
	   * String. Useful for toString() implementations
	   * @param arr array to display. Elements may be of any type (toString() will be
	   * called on each element).
	   * @param delim delimiter to use (probably a ,)
	   */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (arr == null)
			return "null";
		else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < arr.length; i++) {
				if (i > 0)
					sb.append(delim);
				sb.append(arr[i]);
			}
			return sb.toString();
		}
	}

	/** Take a String which is a delimited list and convert it to a String array
	   * @param s String
	   * @param delimiter delimiter. This will not be returned
	   * @return an array of the tokens in the list
	   */
	public static String[] delimitedListToStringArray(String s, String delimiter) {
		if (s == null)
			return new String[0];
		if (delimiter == null || "".equals(delimiter.trim()))
			return new String[] { s };
		List l = new LinkedList();
		int delimCount = 0;
		int pos = 0;
		int delpos = 0;
		while ((delpos = s.indexOf(delimiter, pos)) != -1) {
			l.add(s.substring(pos, delpos));
			pos = delpos + delimiter.length();
		}
		if (pos <= s.length()) {
			//add the rest of string
			l.add(s.substring(pos));
		}
		return (String[]) l.toArray(new String[l.size()]);

	}

	/** Convert a CSV list into an array of Strings
	    * @param s CSV list
	    * @return an array of Strings. Returns the empty array if
	    * s is null.
	    */
	public static String[] commaDelimitedListToStringArray(String s) {
		return delimitedListToStringArray(s, ",");
	}

	public static void main(String[] args) {
		String s = "jijifs,jijij,jifjsijf,fsfsa,fsfs,fasf,afssfasf,fas";
		//		String[] array = delimitedListToStringArray(s, ",,");
		String[] array = commaDelimitedListToStringArray(s);
		prettyPrint(array);
		s = arrayToDelimitedString(array, ":");
		System.out.println(s);
	}

	private static void prettyPrint(String[] array) {
		System.out.println("Print the array");
		for (String s : array) {
			System.out.println(s);
		}
	}

}

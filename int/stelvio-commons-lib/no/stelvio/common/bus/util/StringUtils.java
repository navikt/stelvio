/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.stelvio.common.bus.util;

/**
 * <p>
 * Note: This version of StringUtils.java is stripped for no.stelvio.commons.bus.util and contains only the methods needed by
 * no.stelvio.common.bus.util.DateUtils.
 * </p>
 * 
 * <p>
 * Operations on {@link java.lang.String}that are <code>null</code> safe.
 * </p>
 * 
 * <ul>
 * <li><b>IsEmpty/IsBlank </b>- checks if a String contains text</li>
 * <li><b>Replace/Overlay </b>- Searches a String and replaces one String with another</li>
 * </ul>
 * 
 * <p>
 * The <code>StringUtils</code> class defines certain words related to String handling.
 * </p>
 * 
 * <ul>
 * <li>null -<code>null</code></li>
 * <li>empty - a zero-length string (<code>""</code>)</li>
 * <li>space - the space character (<code>' '</code>, char 32)</li>
 * <li>whitespace - the characters defined by {@link Character#isWhitespace(char)}</li>
 * <li>trim - the characters &lt;= 32 as in {@link String#trim()}</li>
 * </ul>
 * 
 * <p>
 * <code>StringUtils</code> handles <code>null</code> input Strings quietly. That is to say that a <code>null</code> input
 * will return <code>null</code>. Where a <code>boolean</code> or <code>int</code> is being returned details vary by
 * method.
 * </p>
 * 
 * <p>
 * A side effect of the <code>null</code> handling is that a <code>NullPointerException</code> should be considered a bug in
 * <code>StringUtils</code> (except for deprecated methods).
 * </p>
 * 
 * <p>
 * Methods in this class give sample code to explain their operation. The symbol <code>*</code> is used to indicate any input
 * including <code>null</code>.
 * </p>
 * 
 * @see java.lang.String
 * @author <a href="http://jakarta.apache.org/turbine/">Apache Jakarta Turbine </a>
 * @author GenerationJavaCore
 * @author <a href="mailto:test@example.com">Jon S. Stevens </a>
 * @author <a href="mailto:test@example.com">Daniel Rall </a>
 * @author <a href="mailto:test@example.com">Greg Coladonato </a>
 * @author <a href="mailto:test@example.com">Henri Yandell </a>
 * @author <a href="mailto:test@example.com">Ed Korthof </a>
 * @author <a href="mailto:test@example.com">Rand McNeely </a>
 * @author Stephen Colebourne
 * @author <a href="mailto:test@example.com">Fredrik Westermarck </a>
 * @author Holger Krauth
 * @author <a href="mailto:test@example.com">Alexander Day Chaffee </a>
 * @author <a href="mailto:test@example.com">Henning P. Schmiedehausen </a>
 * @author Arun Mammen Thomas
 * @author Gary Gregory
 * @author Phil Steitz
 * @author Al Chou
 * @author Michael Davey
 * @since 1.0
 * @version $Id: StringUtils.java 161243 2005-04-14 04:30:28Z ggregory $
 */
public class StringUtils {

	// This is a stripped version of org.apache.commons.lang.StringUtils which
	// only contains
	// the methods needed by no.stelvio.common.bus.util.DateUtil.
	// Added to stelvio-commons-lib 07.05.2007 by Dag Christoffersen

	/**
	 * <p>
	 * <code>StringUtils</code> instances should NOT be constructed in standard programming. Instead, the class should be used
	 * as <code>StringUtils.trim(" foo ");</code>.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean instance to operate.
	 * </p>
	 */
	public StringUtils() {
		// no init.
	}

	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * 
	 *  
	 *   StringUtils.isEmpty(null)      = true
	 *   StringUtils.isEmpty(&quot;&quot;)        = true
	 *   StringUtils.isEmpty(&quot; &quot;)       = false
	 *   StringUtils.isEmpty(&quot;bob&quot;)     = false
	 *   StringUtils.isEmpty(&quot;  bob  &quot;) = false
	 *   
	 *  
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the String. That functionality is available in
	 * isBlank().
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * 
	 *  
	 *   StringUtils.isBlank(null)      = true
	 *   StringUtils.isBlank(&quot;&quot;)        = true
	 *   StringUtils.isBlank(&quot; &quot;)       = true
	 *   StringUtils.isBlank(&quot;bob&quot;)     = false
	 *   StringUtils.isBlank(&quot;  bob  &quot;) = false
	 *   
	 *  
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Replaces multiple characters in a String in one go. This method can also be used to delete characters.
	 * </p>
	 * 
	 * <p>
	 * For example: <br />
	 * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input returns <code>null</code>. An empty ("") string input returns an empty string. A
	 * null or empty set of search characters returns the input string.
	 * </p>
	 * 
	 * <p>
	 * The length of the search characters should normally equal the length of the replace characters. If the search characters
	 * is longer, then the extra search characters are deleted. If the search characters is shorter, then the extra replace
	 * characters are ignored.
	 * </p>
	 * 
	 * <pre>
	 * 
	 *  
	 *   StringUtils.replaceChars(null, *, *)           = null
	 *   StringUtils.replaceChars(&quot;&quot;, *, *)             = &quot;&quot;
	 *   StringUtils.replaceChars(&quot;abc&quot;, null, *)       = &quot;abc&quot;
	 *   StringUtils.replaceChars(&quot;abc&quot;, &quot;&quot;, *)         = &quot;abc&quot;
	 *   StringUtils.replaceChars(&quot;abc&quot;, &quot;b&quot;, null)     = &quot;ac&quot;
	 *   StringUtils.replaceChars(&quot;abc&quot;, &quot;b&quot;, &quot;&quot;)       = &quot;ac&quot;
	 *   StringUtils.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;yz&quot;)  = &quot;ayzya&quot;
	 *   StringUtils.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;y&quot;)   = &quot;ayya&quot;
	 *   StringUtils.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;yzx&quot;) = &quot;ayzya&quot;
	 *   
	 *  
	 * </pre>
	 * 
	 * @param str
	 *            String to replace characters in, may be null
	 * @param searchChars
	 *            a set of characters to search for, may be null
	 * @param replaceChars
	 *            a set of characters to replace, may be null
	 * @return modified String, <code>null</code> if null string input
	 * @since 2.0
	 */
	public static String replaceChars(String str, String searchChars, String replaceChars) {
		if (isEmpty(str) || isEmpty(searchChars)) {
			return str;
		}
		if (replaceChars == null) {
			replaceChars = "";
		}
		boolean modified = false;
		StringBuffer buf = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int index = searchChars.indexOf(ch);
			if (index >= 0) {
				modified = true;
				if (index < replaceChars.length()) {
					buf.append(replaceChars.charAt(index));
				}
			} else {
				buf.append(ch);
			}
		}
		if (modified) {
			return buf.toString();
		} else {
			return str;
		}
	}

	/**
	 * 
	 * <p>
	 * Deletes all whitespaces from a String as defined by
	 * 
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * 
	 * 
	 * <pre>
	 * 
	 *  
	 *  
	 *   StringUtils.deleteWhitespace(null) = null
	 *  
	 *   StringUtils.deleteWhitespace(&quot;&quot;) = &quot;&quot;
	 *  
	 *   StringUtils.deleteWhitespace(&quot;abc&quot;) = &quot;abc&quot;
	 *  
	 *   StringUtils.deleteWhitespace(&quot; ab c &quot;) = &quot;abc&quot;
	 *  
	 *   
	 *  
	 * </pre>
	 * 
	 * 
	 * 
	 * @param str
	 *            the String to delete whitespace from, may be null
	 * 
	 * @return the String without whitespaces, <code>null</code> if null String input
	 * 
	 */

	public static String deleteWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}

		if (count == sz) {
			return str;
		}

		return new String(chs, 0, count);
	}

	/**
	 * 
	 * <p>
	 * Checks if the String contains only unicode digits.
	 * 
	 * A decimal point is not a unicode digit and returns false.
	 * </p>
	 * 
	 * 
	 * 
	 * <p>
	 * <code>null</code> will return <code>false</code>.
	 * 
	 * An empty String ("") will return <code>true</code>.
	 * </p>
	 * 
	 * 
	 * 
	 * <pre>
	 * 
	 * 
	 *  StringUtils.isNumeric(null) = false
	 * 
	 *  StringUtils.isNumeric(&quot;&quot;) = true
	 * 
	 *  StringUtils.isNumeric(&quot; &quot;) = false
	 * 
	 *  StringUtils.isNumeric(&quot;123&quot;) = true
	 * 
	 *  StringUtils.isNumeric(&quot;12 3&quot;) = false
	 * 
	 *  StringUtils.isNumeric(&quot;ab2c&quot;) = false
	 * 
	 *  StringUtils.isNumeric(&quot;12-3&quot;) = false
	 * 
	 *  StringUtils.isNumeric(&quot;12.3&quot;) = false
	 * 
	 *  
	 * </pre>
	 * 
	 * 
	 * 
	 * @param str
	 *            the String to check, may be null
	 * 
	 * @return <code>true</code> if only contains digits, and is non-null
	 * 
	 */

	public static boolean isNumeric(String str) {

		if (str == null) {

			return false;

		}

		int sz = str.length();

		for (int i = 0; i < sz; i++) {

			if (Character.isDigit(str.charAt(i)) == false) {

				return false;

			}

		}

		return true;

	}

	/**
	 * This method ensures that the output String has only valid XML unicode characters as specified by the XML 1.0 standard.
	 * For reference, please see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the standard</a>. This method
	 * will return an empty String if the input is null or empty.
	 * 
	 * @param in
	 *            The String whose non-valid characters we want to remove.
	 * @return The in String, stripped of non-valid characters.
	 */
	public static String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
			if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

}
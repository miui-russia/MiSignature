/**
 * @author neckau
 */
package test.com.zero.misignature;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import junit.framework.Assert;

import org.junit.Test;

import com.zero.misignature.SignatureUtils;

/**
 * @author neckau
 *
 */
public class SignatureUtilsTest
{

	@Test
	public void testSort()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("aa", "1");
		map.put("cc", "32");
		map.put("ca", "31");
		map.put("dd", "4");
		map.put("bb", "2");
		map.put("ab", "");
		map.put("ac", null);
		Vector<String> result1 = SignatureUtils.sort(map);
		assertEquals(5, result1.size());
		assertEquals("aa=1", result1.get(0));
		assertEquals("bb=2", result1.get(1));
		assertEquals("ca=31", result1.get(2));
		assertEquals("cc=32", result1.get(3));
		assertEquals("dd=4", result1.get(4));
		
		result1 = SignatureUtils.sort(null);
		assertEquals(0, result1.size());
	}
	
	@Test
	public void testAppend()
	{
		Vector<String> vec = new Vector<String>();
		vec.add("a");
		vec.add("b");
		vec.add("c");
		vec.add("d");
		vec.add("e");
		assertEquals("a;b;c;d;e", SignatureUtils.append(vec, ";"));
		
		vec.clear();
		assertEquals("", SignatureUtils.append(vec, ";"));
		
		vec.clear();
		vec.add("a");
		vec.add("b");
		assertEquals("ab", SignatureUtils.append(vec, null));
		
		assertEquals("", SignatureUtils.append(null, ";"));
	}
}

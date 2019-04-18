package com.asiainfo.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Map;

import com.asiainfo.compiler.JavaStringCompiler;
import com.asiainfo.compiler.test.bean.BeanProxy;
import com.asiainfo.compiler.test.bean.User;

public class JavaStringCompilerTest {
	
	JavaStringCompiler compiler = new JavaStringCompiler();

	static final String SINGLE_JAVA = "/* a single java class to one file */  "
			+ "package com.asiainfo.compiler.test;                            "
			+ "import com.asiainfo.compiler.test.bean.*;                      "
			+ "public class UserProxy extends User implements BeanProxy {     "
			+ "    boolean _dirty = false;                                    "
			+ "    public void setId(String id) {                             "
			+ "        super.setId(id);                                       "
			+ "        setDirty(true);                                        "
			+ "    }                                                          "
			+ "    public void setName(String name) {                         "
			+ "        super.setName(name);                                   "
			+ "        setDirty(true);                                        "
			+ "    }                                                          "
			+ "    public void setCreated(long created) {                     "
			+ "        super.setCreated(created);                             "
			+ "        setDirty(true);                                        "
			+ "    }                                                          "
			+ "    public void setDirty(boolean dirty) {                      "
			+ "        this._dirty = dirty;                                   "
			+ "    }                                                          "
			+ "    public boolean isDirty() {                                 "
			+ "        return this._dirty;                                    "
			+ "    }                                                          "
			+ "}                                                              ";

	public void testCompileSingleClass() throws Exception {
		
		Map<String, byte[]> results = compiler.compile("UserProxy.java", SINGLE_JAVA);
		assertEquals(1, results.size());
		assertTrue(results.containsKey("com.asiainfo.compiler.test.UserProxy"));
		Class<?> clazz = compiler.loadClass("com.asiainfo.compiler.test.UserProxy", results);
		// get method
		Method setId = clazz.getMethod("setId", String.class);
		Method setName = clazz.getMethod("setName", String.class);
		Method setCreated = clazz.getMethod("setCreated", long.class);
		// try instance
		Object obj = clazz.newInstance();
		// get as proxy
		BeanProxy proxy = (BeanProxy) obj;
		assertFalse(proxy.isDirty());
		// set
		setId.invoke(obj, "A-123");
		setName.invoke(obj, "Fly");
		setCreated.invoke(obj, 123000999);
		// get as user
		User user = (User) obj;
		assertEquals("A-123", user.getId());
		assertEquals("Fly", user.getName());
		assertEquals(123000999, user.getCreated());
		assertTrue(proxy.isDirty());
		System.out.println(user);
	}

	/**
	 * @Fields MULTIPLE_JAVA : TODO
	 */
	static final String MULTIPLE_JAVA = "/* a single class to many files */   "
			+ "package com.asiainfo.compiler.test;                            "
			+ "import java.util.*;                                            "
			+ "public class Multiple {                                        "
			+ "    List<Bird> list = new ArrayList<Bird>();                   "
			+ "    public void add(String name) {                             "
			+ "        Bird bird = new Bird();                                "
			+ "        bird.name = name;                                      "
			+ "        this.list.add(bird);                                   "
			+ "    }                                                          "
			+ "    public Bird getFirstBird() {                               "
			+ "        return this.list.get(0);                               "
			+ "    }                                                          "
			+ "    public static class StaticBird {                           "
			+ "        public int weight = 100;                               "
			+ "    }                                                          "
			+ "    class NestedBird {                                         "
			+ "        NestedBird() {                                         "
			+ "            System.out.println(list.size() + \" birds...\");   "
			+ "        }                                                      "
			+ "    }                                                          "
			+ "}                                                              "
			+ "/* package level */                                            "
			+ "class Bird {                                                   "
			+ "    String name = null;                                        "
			+ "    public String toString() {                                 "
			+ "        return \"name=\" + name;                               "
			+ "    }                                                          "
			+ "}                                                              ";

	public void testCompileMultipleClasses() throws Exception {
		
		Map<String, byte[]> results = compiler.compile("Multiple.java", MULTIPLE_JAVA);
		assertEquals(4, results.size());
		assertTrue(results.containsKey("com.asiainfo.compiler.test.Multiple"));
		assertTrue(results.containsKey("com.asiainfo.compiler.test.Multiple$StaticBird"));
		assertTrue(results.containsKey("com.asiainfo.compiler.test.Multiple$NestedBird"));
		assertTrue(results.containsKey("com.asiainfo.compiler.test.Bird"));
		Class<?> clz = compiler.loadClass("com.asiainfo.compiler.test.Multiple", results);
		// try instance
		Object multi = clz.newInstance();
		Method add = clz.getMethod("add", String.class);
		add.invoke(multi, "wuya");
		
		Method get = clz.getMethod("getFirstBird", (Class<?>[]) null);
		Object bird = get.invoke(multi, new Object[0]);
		System.out.println(bird.getClass() + ", " + bird);
	}
	
	public static void main(String[] args) throws Exception {
		
		JavaStringCompilerTest test = new JavaStringCompilerTest();
		test.testCompileSingleClass();
		test.testCompileMultipleClasses();
	}
}

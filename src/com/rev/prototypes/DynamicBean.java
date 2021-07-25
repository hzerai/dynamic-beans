/**
 * 
 */
package com.rev.prototypes;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Habib Zerai
 *
 */
public class DynamicBean {
	private Daughter instance;

	public DynamicBean(String beanName, Map<String, String> fields) {
		this.instance = Utility.getBean(beanName, fields);
	}

	public Class<?> getDynamicClass() {
		return this.instance.getClass();
	}

	public DynamicBean(String name) {
		this(name, null);
	}

	public void set(String key, Object value) {
		this.instance.set(key, value);
	}

	public Object get(String key) {
		return this.instance.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(instance.getName() + "[ ");
		Iterator<String> keys = instance.getKeys().iterator();

		while (keys.hasNext()) {
			String key = keys.next();
			if (get(key) != null) {
				sb.append(key);
				sb.append(" : ");
				sb.append(get(key));
				if (keys.hasNext())
					sb.append(", ");
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("]");
		return sb.toString();
	}

	public static boolean isRegistred(String beanName) {
		return Utility.isRegistredBean(beanName);
	}

	/**
	 * @param out
	 */
	public void toString(PrintStream out) {
		out.println(this);
	}

}

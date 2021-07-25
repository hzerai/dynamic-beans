/**
 * 
 */
package com.rev.prototypes;

import static com.rev.prototypes.Templates.ATTRIBUTE;
import static com.rev.prototypes.Templates.ATTRIBUTEINIT;
import static com.rev.prototypes.Templates.ATTRIBUTEKEY;
import static com.rev.prototypes.Templates.GETTER;
import static com.rev.prototypes.Templates.PROTOTYPE;
import static com.rev.prototypes.Templates.SETTER;
import static com.rev.types.JavaTypes.BOOLEAN;
import static com.rev.types.JavaTypes.CHAR;
import static com.rev.types.JavaTypes.CHARACTER;
import static com.rev.types.JavaTypes.DOUBLE;
import static com.rev.types.JavaTypes.FLOAT;
import static com.rev.types.JavaTypes.INTEGER;
import static com.rev.types.JavaTypes.LONG;
import static com.rev.types.JavaTypes.b;
import static com.rev.types.JavaTypes.d;
import static com.rev.types.JavaTypes.f;
import static com.rev.types.JavaTypes.i;
import static com.rev.types.JavaTypes.l;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Habib Zerai
 *
 */
abstract class Utility {
	private static Map<String, Class> CLASSREGISTER = new HashMap<>();
	private static Map<String, Daughter> BEANREGISTER = new HashMap<>();
	private static Map<String, CtClass> CTCLASS_REGISTER = new HashMap<>();
	private static Map<String, Set<String>> KEYREGISTER = new HashMap<>();
	private static Map<String, Map<String, String>> KEYTYPES = new HashMap<>();

	static CtClass getPrototype(String name) {
		if (CTCLASS_REGISTER.containsKey(name)) {
			return CTCLASS_REGISTER.get(name);
		}
		CtClass ctClazz = null;
		try {
			ctClazz = initiliazeCtClass(ctClazz);
			if (name != null)
				ctClazz.setName(name);
		} catch (NotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		CTCLASS_REGISTER.put(name, ctClazz);
		return ctClazz;
	}
	//Employee maps
	static Daughter getBean(String name, Map fields) {
		if (fields == null || fields.isEmpty()) {
			if (!CTCLASS_REGISTER.containsKey(name)) {
				System.err.println("#################### WARN ################## : CAREFUL ! CREATING EMPTY BEAN ! ");
			} else {
				return (Daughter) cloned(BEANREGISTER.get(name));
			}
		}
		if (CTCLASS_REGISTER.containsKey(name)) {
			System.err.println("#################### WARN ################## : CAREFUL ! CREATING EMPTY BEAN ! ");
			return (Daughter) cloned(BEANREGISTER.get(name));
		}
		return getJavaBean(Utility.createFields(getPrototype(name), fields));
	}

	/**
	 * @param daughter
	 * @return
	 */
	private static Daughter cloned(Daughter daughter) {
		try {
			return (Daughter) daughter.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static CtClass initiliazeCtClass(CtClass ctClazz) throws NotFoundException, ClassNotFoundException {
		String prot = PROTOTYPE + CLASSREGISTER.size();
		ClassPool cp = ClassPool.getDefault();
		cp.insertClassPath(new ClassClassPath(Class.forName(prot)));
//		try {
//			ClassPool.getDefault().getClassLoader().loadClass(prot);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
		ctClazz = ClassPool.getDefault().get(prot);
		createField(ctClazz, "ctClass", CtClass.class.getName(), "null");
		createField(ctClazz, "keys", Set.class.getName(), "null");
		return ctClazz;
	}

	static Class getJavaClass(CtClass prototype) {
		if (CLASSREGISTER.containsKey(prototype.getName())) {
			return CLASSREGISTER.get(prototype.getName());
		}
		Class clazz = null;
		try {
			clazz = prototype.toClass();
			CLASSREGISTER.put(prototype.getName(), clazz);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	static Daughter getJavaBean(CtClass prototype) {
		if (BEANREGISTER.containsKey(prototype.getName())) {
			try {
				return (Daughter) ((Daughter) BEANREGISTER.get(prototype.getName())).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		Daughter obj = null;
		obj = createBean(getJavaClass(prototype), prototype);
		BEANREGISTER.put(prototype.getName(), (Daughter) obj);
		try {
			return (Daughter) ((Daughter) obj).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new Daughter();
	}

	private static Daughter createBean(Class<?> clazz, CtClass prototype) {
		Daughter obj = null;
		try {
			obj = (Daughter) clazz.newInstance();
			obj.setCtClass(prototype);
			obj.setKeys(KEYREGISTER.get(prototype.getSimpleName()));
			obj.setKeyTypes(KEYTYPES.get(prototype.getSimpleName()));
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}

	static CtClass createField(CtClass ctClass, String fieldName, String javaType, String defaultValue) {
		javaType = getRealJavaType(javaType);
		try {
			ctClass.addField(CtField.make(getAttributKey(fieldName), ctClass));
			ctClass.addField(CtField.make(getClassAttribut(fieldName, javaType, defaultValue), ctClass));
			ctClass.addMethod(CtMethod.make(getSetterMethod(fieldName, javaType), ctClass));
			ctClass.addMethod(CtMethod.make(getGetterMethod(fieldName, javaType), ctClass));
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}

		if (KEYREGISTER.containsKey(ctClass.getSimpleName())) {
			KEYREGISTER.get(ctClass.getSimpleName()).add(fieldName);
			KEYTYPES.get(ctClass.getSimpleName()).put(fieldName, javaType);
		} else {
			Set<String> set = new HashSet<>();
			set.add(fieldName);
			KEYREGISTER.put(ctClass.getSimpleName(), set);
			Map<String, String> keyType = new HashMap<>();
			keyType.put(fieldName, javaType);
			KEYTYPES.put(ctClass.getSimpleName(), keyType);
		}
		return ctClass;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	private static String getAttributKey(String fieldName) {
		return String.format(ATTRIBUTEKEY, createKeyForAttribute(fieldName), fieldName);
	}

	/**
	 * @param fieldName
	 * @return
	 */
	private static String createKeyForAttribute(String fieldName) {
		String key = "KEY_";
		for (int i = 0; i < fieldName.length(); i++) {
			char ch = fieldName.charAt(i);
			if (Character.isUpperCase(ch)) {
				key += "_" + ch;
			} else {
				key += ch;
			}
		}
		return key.toUpperCase();
	}

	private static String addQuotes(String defaultValue) {
		return "\"" + defaultValue + "\"";
	}

	private static String getSetterMethod(String fieldName, String javaType) {
		String fieldNameCapitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return String.format(SETTER, fieldNameCapitalized, javaType, fieldName, fieldName, fieldName);
	}

	private static String getGetterMethod(String fieldName, String javaType) {
		String fieldNameCapitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return String.format(GETTER, javaType, fieldNameCapitalized, fieldName);
	}

	private static String getRealJavaType(String javaType) {
		switch (javaType) {
		case i:
			return INTEGER;
		case f:
			return FLOAT;
		case b:
			return BOOLEAN;
		case d:
			return DOUBLE;
		case CHAR:
			return CHARACTER;
		case l:
			return LONG;
		}
		return javaType;
	}

	private static String getClassAttribut(String fieldName, String javaType, String defaultValue) {
		if (defaultValue == null && javaType.matches("int|boolean|float|double|long|char")) {
			return String.format(ATTRIBUTE, javaType, fieldName);
		}
		return String.format(ATTRIBUTEINIT, javaType, fieldName, getDefaultValue(defaultValue, javaType));
	}

	private static String getDefaultValue(String defaultValue, String javaType) {
//		if (javaType.equals(String.class.getName()))
//			return addQuotes(defaultValue);
		return defaultValue;
	}

	/**
	 * @param ctClass
	 * @param fields
	 */
	static CtClass createFields(CtClass ctClass, Map<String, String> fields) {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			createField(ctClass, entry.getKey(), entry.getValue(), null);
		}
		return ctClass;
	}

	static boolean isRegistredBean(String beanName) {
		return BEANREGISTER.containsKey(beanName);
	}
}

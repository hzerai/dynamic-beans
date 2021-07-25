/**
 * 
 */
package com.rev.services;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Habib Zerai
 *
 */
public class ServiceBuilder {

public static Map<String,ServiceImpl> services = new HashMap<>();
	
	public static ServiceImpl getService(String beanAlias) {
		return services.get(beanAlias);
	}
	
	public static void registerService(ServiceImpl service) {
		services.put(service.getBeanAlias(),service);
	}
	
}

/**
 * 
 */
package com.rev.services;

/**
 * @author Habib Zerai
 *
 */
public class ServiceFacade {

	/**
	 * @param service
	 * @param action
	 * @param id
	 * @return
	 */
	public static String invokeService(String service, String action, String id) {

		ServiceImpl srv = new ServiceImpl(new ServiceQueryImpl(service, action, id));
		return srv.getResult().toString();
	}
	
	
	/**
	 * @param service
	 * @param action
	 * @return
	 */
	public static String invokeService(String service, String action) {

		ServiceImpl srv = new ServiceImpl(new ServiceQueryImpl(service, action, null));
		return srv.getResult().toString();
	}

}

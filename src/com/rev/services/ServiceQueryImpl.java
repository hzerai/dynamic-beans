/**
 * 
 */
package com.rev.services;

import com.rev.services.interfaces.AbstractServiceQuery;

/**
 * @author Habib Zerai
 *
 */
public class ServiceQueryImpl implements AbstractServiceQuery {

	private final String beanAlias;
	private String action;
	private String id;

	
	ServiceQueryImpl(String beanAlias, String action, String id) {
		this.beanAlias = beanAlias;
		this.action = action;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBeanAlias() {
		return beanAlias;
	}

	

}

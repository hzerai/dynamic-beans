/**
 * 
 */
package com.rev.services.interfaces;

import java.util.Collection;

import com.rev.prototypes.DynamicBean;

/**
 * @author Habib Zerai
 *
 */
public interface AbstractService {
	
	Collection<?> findAll();
	Collection<?> find();
	Collection<?> findOnePage();
	Object findOne();
	Object save();
	Boolean delete();
	Object update();

}

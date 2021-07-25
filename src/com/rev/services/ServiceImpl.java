/**
 * 
 */
package com.rev.services;

import static com.rev.types.JavaTypes.STRING;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.rev.prototypes.DynamicBean;
import com.rev.services.interfaces.AbstractService;

/**
 * @author Habib Zerai
 *
 */
public class ServiceImpl extends ServiceInfo implements AbstractService {

	ServiceQueryImpl serviceQuery;
	private static final Collection<DynamicBean> testRepo = buildRepo();

	ServiceImpl(ServiceQueryImpl serviceQuery) {
		this.serviceQuery = serviceQuery;
	}

	/**
	 * @return
	 */
	private static Collection<DynamicBean> buildRepo() {
		Map<String, String> field = new HashMap<String, String>();
		field.put("identifier", STRING);
		field.put("city", STRING);
		field.put("country", STRING);
		DynamicBean adress = new DynamicBean("Adress", field);
		adress.set("identifier", "1");
		adress.set("city", "Tunis");
		adress.set("country", "Tunisia");
		DynamicBean adress1 = new DynamicBean("Adress");
		adress1.set("identifier", "2");
		adress1.set("city", "Paris");
		adress1.set("country", "France");
		DynamicBean adress2 = new DynamicBean("Adress");
		adress2.set("identifier", "3");
		adress2.set("city", "instanbul");
		adress2.set("country", "Turiky");

		Collection<DynamicBean> result = new ArrayList<>();
		result.add(adress);
		result.add(adress1);
		result.add(adress2);
		return result;
	}

	@Override
	public Collection<?> findAll() {
		return testRepo;
	}

	@Override
	public Collection<?> find() {
		return testRepo;
	}

	@Override
	public Collection<?> findOnePage() {
		return null;
	}

	@Override
	public Object findOne() {
		return testRepo.stream().filter(d -> serviceQuery.getId().equals(d.get("identifier"))).findFirst().get();
	}

	@Override
	public Object save() {
		return null;
	}

	@Override
	public Boolean delete() {
		return testRepo.remove(
				testRepo.stream().filter(d -> serviceQuery.getId().equals(d.get("identifier"))).findFirst().get());
	}

	@Override
	public Object update() {
		return null;
	}

	/**
	 * @return
	 */
	public String getResult() {
		switch (serviceQuery.getAction()) {
		case "find":
			return find().toString();
		case "findOne":
			return findOne().toString();
		case "delete":
			return delete().toString();
		}
		return null;
	}
}

/**
 * 
 */
package org.fedoraproject.candlepin.api.test;

import org.fedoraproject.candlepin.api.ApiHandler;
import org.fedoraproject.candlepin.api.OrgApi;
import org.fedoraproject.candlepin.model.BaseModel;
import org.fedoraproject.candlepin.model.ObjectFactory;
import org.fedoraproject.candlepin.model.Organization;
import org.fedoraproject.candlepin.model.User;

import junit.framework.TestCase;

/**
 * @author mmccune
 *
 */
public class ApiTest extends TestCase {

	public void testAuthentication() throws Exception {
		User u = new User();
		u.setLogin("admin");
		u.setPassword("password");
		
		ObjectFactory.get().store(u);
		
		ApiHandler handler = ApiHandler.get();
		String token = handler.login(u.getLogin(), "bad-password");
		assertNull(token);
		token = handler.login(u.getLogin(), u.getPassword());
		assertNotNull(token);
	}
	
	public void testLookupOrg() throws Exception {
		Organization o = new Organization(BaseModel.generateUUID());
		ObjectFactory.get().store(o);

		User u = new User();
		u.setLogin("admin");
		u.setPassword("password");
		ObjectFactory.get().store(u);
		
		String token = ApiHandler.get().login(u.getLogin(), u.getPassword());
		
		OrgApi oapi = new OrgApi();
		Organization lookedup = (Organization) oapi.get("BAD-UUID-NOTFOUND");
		assertNull(lookedup);
		lookedup = ApiHandler.get().getOrg(token, o.getUuid());
		assertNotNull(lookedup);
		
		// Check bad token
		boolean failed = false;
		try {
			lookedup = ApiHandler.get().getOrg("BAD-TOKEN", o.getUuid());
		} catch (Exception e) {
			failed = true;
		}
		assertTrue(failed);
		
	}
}

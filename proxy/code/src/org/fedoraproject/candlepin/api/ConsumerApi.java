/**
 * Copyright (c) 2008 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.fedoraproject.candlepin.api;

import org.fedoraproject.candlepin.model.Consumer;
import org.fedoraproject.candlepin.model.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/consumer")
public class ConsumerApi extends BaseApi {

    @Override
    protected Class getApiClass() {
        return Consumer.class;
    }
    

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Consumer> list() {
        List<Object> u = ObjectFactory.get().listObjectsByClass(getApiClass());
        List<Consumer> consumers = new ArrayList<Consumer>();
        for (Object o : u) {
            consumers.add((Consumer) o);
        }
        return consumers;
    }
    
}

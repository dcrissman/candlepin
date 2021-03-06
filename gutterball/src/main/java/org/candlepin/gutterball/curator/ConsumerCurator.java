/**
 * Copyright (c) 2009 - 2012 Red Hat, Inc.
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
package org.candlepin.gutterball.curator;

import org.candlepin.gutterball.model.Consumer;
import org.candlepin.gutterball.mongodb.MongoConnection;

import com.google.inject.Inject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import java.util.Date;
import java.util.List;

/**
 * A curator that manages DB operations on the 'consumers' collection.
 */
public class ConsumerCurator extends MongoDBCurator<Consumer> {
    public static final String COLLECTION = "consumers";

    @Inject
    public ConsumerCurator(MongoConnection mongo) {
        super(Consumer.class, mongo);
        this.collection.createIndex(new BasicDBObject("deleted", -1));
        this.collection.createIndex(new BasicDBObject("created", -1));
        this.collection.createIndex(new BasicDBObject("uuid", 1));
        this.collection.createIndex(new BasicDBObject("owner.key", 1));
    }

    @Override
    public String getCollectionName() {
        return COLLECTION;
    }

    public Consumer findByUuid(String uuid) {
        return (Consumer) collection.findOne(new BasicDBObject("uuid", uuid));
    }

    public WriteResult setConsumerDeleted(String uuid, Date deleted) {
        DBObject query = new BasicDBObject("uuid", uuid);
        DBObject update = new BasicDBObject("$set", new BasicDBObject("deleted", deleted));
        return collection.update(query, update);
    }

    public List<String> getUuidsOnDate(Date targetDate, List<String> owners, List<String> uuids) {
        BasicDBObjectBuilder queryBuilder = BasicDBObjectBuilder.start();

        if (owners != null && !owners.isEmpty()) {
            queryBuilder.append("owner.key", new BasicDBObject("$in", owners));
        }

        if (uuids != null && !uuids.isEmpty()) {
            queryBuilder.append("uuid", new BasicDBObject("$in", uuids));
        }

        Date toCheck = targetDate == null ? new Date() : targetDate;

        BasicDBObject deletedShouldBeNull = new BasicDBObject("deleted", null);
        BasicDBObject deletedGreaterThanTarget =
                new BasicDBObject("deleted", new BasicDBObject("$gt", toCheck));

        BasicDBList or = new BasicDBList();
        or.add(deletedShouldBeNull);
        or.add(deletedGreaterThanTarget);

        queryBuilder.append("$or", or);

        // Valid uuids must have been created before/on the target date.
        queryBuilder.append("created", new BasicDBObject("$lte", toCheck));

        return collection.distinct("uuid", queryBuilder.get());
    }
}

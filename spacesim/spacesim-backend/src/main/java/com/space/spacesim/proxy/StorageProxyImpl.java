package com.space.spacesim.proxy;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.iterator.ORecordIteratorCluster;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.space.spacesim.model.entity.AbstractEntity;
import com.space.spacesim.model.entity.EmptyEntity;
import com.space.spacesim.store.PersistentEntity;

public class StorageProxyImpl implements StorageProxy {

	private static final Logger logger = LoggerFactory.getLogger(StorageProxyImpl.class);

	@Inject
	private EngineProxy engine;

	@Inject
	private OPartitionedDatabasePool pool;

	@Inject
	private Injector injector;

	@Override
	public Entity getEntity(ORID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends Entity> Collection<E> getEntitiesOfType(Class<E> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadIntoEngine(ORID id) {
		try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
			Object loaded = db.load(id);
			if (loaded instanceof Entity) {
				engine.addEntity((Entity) loaded);
			} else {
				logger.error("trying to load {} into the engine, but this is no Entity", loaded);
			}
		}
	}
	

	@Override
	public <E extends AbstractEntity<E>, PE extends PersistentEntity<E>> void loadAllIntoEngine(Class<E> type) {
		try (ODatabaseDocumentTx db = pool.acquire()) {
			// db.browseCluster(type.getSimpleName()).setFetchPlan("*:-1");
			logger.debug("loading all");
			ORecordIteratorCluster<ODocument> iterator = db.browseCluster(type.getSimpleName());
			for (ODocument doc : db.browseCluster(type.getSimpleName())) {

				E entity = injector.getInstance(Key.get(type, EmptyEntity.class));
				PersistentEntity<E> persistentEntity = injector.getInstance(PersistentEntity.class);
				persistentEntity.setType(type);
				persistentEntity.setEntity(doc);
				persistentEntity.load();
				if (entity instanceof AbstractEntity) {
					((AbstractEntity<E>) entity).setPersistentEntity(persistentEntity);
				}
				logger.debug("loading {} ", entity);
				engine.addEntity(type.cast(entity));
			}

		}
	}

}

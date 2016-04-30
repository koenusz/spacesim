package com.guicemodel;

import com.badlogic.ashley.core.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.space.spacesim.store.PersistentEntity;

public class PersistenceEntityProvider<E extends Entity> implements Provider<PersistentEntity<E>> {

	@Inject
	private OPartitionedDatabasePool pool;

	private Class<E> type;

	@Inject
	public PersistenceEntityProvider(Class<E> type) {
		this.type = type;
	}

	@Override
	public PersistentEntity<E> get() {

		try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
			PersistentEntity<E> per = db.newInstance(PersistentEntity.class.getSimpleName());
			per.setType(type);
			per.setPool(pool);
			return per;
		}
	}

}

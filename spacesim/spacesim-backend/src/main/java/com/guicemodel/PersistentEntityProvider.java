package com.guicemodel;

import com.badlogic.ashley.core.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.space.spacesim.store.PersistentEntity;

public class PersistentEntityProvider<E extends Entity> implements Provider<PersistentEntity<E>> {

	@Inject
	private OPartitionedDatabasePool pool;

	private Class<E> type;

	@Inject
	public PersistentEntityProvider(Class<E> type) {
		this.type = type;
	}

	@Override
	public PersistentEntity<E> get() {

		PersistentEntity<E> per;
		try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
			per = db.newInstance(PersistentEntity.class.getSimpleName());
			per.setPool(pool);
			per.setType(type);
		}
		return per;
	}

}

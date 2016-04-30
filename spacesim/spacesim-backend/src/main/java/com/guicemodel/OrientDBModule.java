package com.guicemodel;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.serialization.serializer.object.OObjectSerializer;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.serialization.OObjectSerializerContext;
import com.orientechnologies.orient.object.serialization.OObjectSerializerHelper;
import com.space.spacesim.model.entity.EmptyEntity;
import com.space.spacesim.model.entity.Ship;
import com.space.spacesim.store.PersistentEntity;

public class OrientDBModule extends AbstractModule {

	// private static final Logger logger =
	// LoggerFactory.getLogger(OrientDBModule.class);

	@Inject
	private Injector injector;

	private final OPartitionedDatabasePool pool;

	public OrientDBModule(String url, String userName, String password) {
		super();
		pool = new OPartitionedDatabasePool(url, userName, password);
		pool.setAutoCreate(true);
	}

	@Provides
	public OPartitionedDatabasePool providesPool() {
		return pool;
	}

	@Override
	protected void configure() {
		try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
		providesOObjectSerializerContext().bind(new OObjectSerializer<Ship, String>() {

			@Override
			public Object serializeFieldValue(Class<?> iClass, Ship ship) {
				if (ship.getId() == null) {
					return "";
				}
				return ship.getId();
			}

			@Override
			public Object unserializeFieldValue(Class<?> iClass, String id) {

				if (id == null) {
					return null;
				}
				Ship ship = injector.getInstance(Key.get(Ship.class, EmptyEntity.class));
				ship.load(id);
				return ship;
			}
		}, db);
		OObjectSerializerHelper.bindSerializerContext(Ship.class, providesOObjectSerializerContext());

		db.getEntityManager().registerEntityClasses("com.space.spacesim.model.ship.component");
		db.getEntityManager().registerEntityClasses("com.space.spacesim.model.util.component");
		db.getEntityManager().registerEntityClass(PersistentEntity.class);
		addCluster(Ship.class.getSimpleName());
	}}

	private void addCluster(String clusterName) {
		try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
		if (db.getClusterIdByName(clusterName) == -1) {
			db.addCluster(clusterName);
			db.getMetadata().getSchema().getClass(PersistentEntity.class)
					.addCluster(clusterName);
		}}
	}


	@Provides
	@Singleton
	public OObjectSerializerContext providesOObjectSerializerContext() {
		return new OObjectSerializerContext();
	}

}

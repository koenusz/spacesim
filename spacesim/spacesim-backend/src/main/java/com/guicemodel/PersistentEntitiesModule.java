package com.guicemodel;

import java.lang.reflect.Constructor;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.space.spacesim.model.entity.EmptyEntity;
import com.space.spacesim.model.entity.Ship;
import com.space.spacesim.model.util.system.NameSystem;
import com.space.spacesim.proxy.StorageProxy;
import com.space.spacesim.proxy.StorageProxyImpl;
import com.space.spacesim.store.PersistentEntity;

public class PersistentEntitiesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageProxy.class).to(StorageProxyImpl.class);
		bind(new TypeLiteral<PersistentEntity<Ship>>() {}).toProvider(new PersistentEntityProvider<>(Ship.class));
		try {
			Constructor<Ship> constructor = Ship.class.getConstructor(NameSystem.class);
			bind(Ship.class).annotatedWith(EmptyEntity.class).toConstructor(constructor);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
	}

}

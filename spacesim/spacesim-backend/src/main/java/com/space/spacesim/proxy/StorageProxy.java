package com.space.spacesim.proxy;

import java.util.Collection;

import com.badlogic.ashley.core.Entity;
import com.orientechnologies.orient.core.id.ORID;
import com.space.spacesim.model.entity.AbstractEntity;
import com.space.spacesim.store.PersistentEntity;

public interface StorageProxy {

	public Entity getEntity(ORID id);
	
	public <E extends Entity> Collection<E> getEntitiesOfType(Class<E> type);
	
	public void loadIntoEngine(ORID id);
	
	public <E extends AbstractEntity<E>, PE extends PersistentEntity<E>> void loadAllIntoEngine(Class<E> type);
	
}

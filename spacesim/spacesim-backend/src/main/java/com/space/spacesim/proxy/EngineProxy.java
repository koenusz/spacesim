package com.space.spacesim.proxy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public interface EngineProxy {

	public void addEntity(Entity entity);

	public void removeEntity(Entity entity);

	public void removeAllEntities();

	public ImmutableArray<Entity> getEntities();

	public void addSystem(EntitySystem system);

	public void removeSystem(EntitySystem system);

	public <T extends EntitySystem> T getSystem(Class<T> type);

	public ImmutableArray<EntitySystem> getSystems();

	public ImmutableArray<Entity> getEntitiesFor(Family family);

	public void addEntityListener(EntityListener listener);

	public void addEntityListener(int priority, EntityListener listener);

	public void addEntityListener(Family family, EntityListener listener);

	public void addEntityListener(Family family, int priority, EntityListener listener);

	public void removeEntityListener(EntityListener listener);

	public void update(float deltaTime);

}

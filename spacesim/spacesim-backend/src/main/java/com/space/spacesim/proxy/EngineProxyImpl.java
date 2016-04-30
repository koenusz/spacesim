package com.space.spacesim.proxy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.inject.Inject;

public class EngineProxyImpl implements EngineProxy {

	
	private Engine engine;
	
	@Inject
	private EngineProxyImpl(Engine engine) {
		this.engine = engine;
	}
	

	@Override
	public void addEntity(Entity entity) {
		engine.addEntity(entity);

	}

	@Override
	public void removeEntity(Entity entity) {
		engine.removeEntity(entity);

	}

	@Override
	public void removeAllEntities() {
		engine.removeAllEntities();

	}

	@Override
	public ImmutableArray<Entity> getEntities() {

		return engine.getEntities();
	}

	@Override
	public void addSystem(EntitySystem system) {
		engine.addSystem(system);

	}

	@Override
	public void removeSystem(EntitySystem system) {
		engine.removeSystem(system);

	}

	@Override
	public <T extends EntitySystem> T getSystem(Class<T> type) {

		return engine.getSystem(type);
	}

	@Override
	public ImmutableArray<EntitySystem> getSystems() {

		return engine.getSystems();
	}

	@Override
	public ImmutableArray<Entity> getEntitiesFor(Family family) {

		return engine.getEntitiesFor(family);
	}

	@Override
	public void addEntityListener(EntityListener listener) {
		engine.addEntityListener(listener);

	}

	@Override
	public void addEntityListener(int priority, EntityListener listener) {
		engine.addEntityListener(priority, listener);

	}

	@Override
	public void addEntityListener(Family family, EntityListener listener) {
		engine.addEntityListener(family, listener);

	}

	@Override
	public void addEntityListener(Family family, int priority, EntityListener listener) {
		engine.addEntityListener(family, priority, listener);

	}

	@Override
	public void removeEntityListener(EntityListener listener) {
		engine.removeEntityListener(listener);

	}

	@Override
	public void update(float deltaTime) {
		engine.update(deltaTime);

	}

}

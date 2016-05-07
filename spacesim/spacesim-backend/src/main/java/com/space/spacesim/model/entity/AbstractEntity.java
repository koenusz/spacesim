package com.space.spacesim.model.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.orientechnologies.orient.core.id.ORID;
import com.space.spacesim.model.util.component.NameComponent;
import com.space.spacesim.model.util.system.NameSystem;
import com.space.spacesim.store.PersistentEntity;
import com.space.spacesim.store.Storable;

public abstract class AbstractEntity<A extends AbstractEntity<A>> extends Entity implements Storable<A>, Namable {

	private static final Logger logger = LoggerFactory.getLogger(AbstractEntity.class);

	private NameSystem names;

	private PersistentEntity<A> persistentEntity;

	protected AbstractEntity(NameSystem names, PersistentEntity<A> persistentEntity) {
		super();
		this.persistentEntity = persistentEntity;
		this.names = names;
		if (!persistentEntity.hasNameComponent()) {
			addNameComponent();
		}

	}

	protected AbstractEntity(NameSystem names) {
		super();
		this.names = names;
	}

	private void addNameComponent() {
		NameComponent name = new NameComponent();
		name.setType(this.getClass().getSimpleName());
		addComponent(name, NameComponent.class);
	}

	public void reload() {
		persistentEntity = persistentEntity.reload();
	}

//	public void load(String id) {
//		if (StringUtils.isEmpty(id)) {
//			return;
//		}
//		persistentEntity = persistentEntity.load(id);
//	}

	public void load() {
		persistentEntity = persistentEntity.load();
	}

	public void save() {
		this.persistentEntity = persistentEntity.save();
		for(Component com : persistentEntity.getComponents())
		{
			updateComponent(com);
		}
	}

	public <C extends Component> void addComponent(Class<C> type) {

		Component component = persistentEntity.addComponent(null, type);
		addComponentToEngine(component);
	}

	public <C extends Component> void addComponent(Component component, Class<C> type) {
		component = persistentEntity.addComponent(component, type);
		addComponentToEngine(component);
	}

	private void updateComponent(Component component) {
		this.remove(component.getClass());
		this.add(component);
	}

	private void addComponentToEngine(Component component) {
		this.add(component);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(names.toString(this));
		return builder.toString();
	}

	@Override
	public <C extends Component> void removeComponent(Class<C> type) {
		persistentEntity.removeComponent(type);

	}

	@Override
	public void setName(String name) {
		this.getComponent(NameComponent.class).setName(name);
	}

	@Override
	public String getName() {

		return this.getComponent(NameComponent.class).getName();
	}

	@Override
	public void setClassification(String classification) {
		this.getComponent(NameComponent.class).setClassification(classification);

	}

	@Override
	public String getClassification() {
		return this.getComponent(NameComponent.class).getClassification();
	}

	@Override
	public PersistentEntity<A> getPersistentEntity() {
		return persistentEntity;
	}

	@Override
	public void setPersistentEntity(PersistentEntity<A> persistentEntity) {
		this.persistentEntity = persistentEntity;
		this.persistentEntity.getComponents().stream().forEach(component -> add(component));
	}

	public ORID getId() {
		return persistentEntity.getId();
	}

}

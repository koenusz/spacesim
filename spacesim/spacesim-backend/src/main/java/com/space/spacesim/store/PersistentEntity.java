package com.space.spacesim.store;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.google.inject.Inject;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.space.spacesim.model.util.component.NameComponent;

import lombok.Setter;

public class PersistentEntity<E extends Entity> {

	private static final Logger logger = LoggerFactory.getLogger(PersistentEntity.class);

	@Id
	@Setter
	private ORID id;

	// private Set<ODocument> components = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Component> components = new HashSet<>();

	@Setter
	transient Class<E> type;

	@Inject
	@Setter
	transient private OPartitionedDatabasePool pool;

	public PersistentEntity() {
	};

	private OObjectDatabaseTx acquire() {
		return new OObjectDatabaseTx(pool.acquire());
	}

	public PersistentEntity<E> reload() {
		try (OObjectDatabaseTx db = acquire()) {
			return db.reload(this);
		}
	}

	public PersistentEntity<E> load(String id) {
		try (OObjectDatabaseTx db = acquire()) {
			return db.load(id);
		}
	}

	public PersistentEntity<E> load() {
		try (OObjectDatabaseTx db = acquire()) {
			return db.load(id);
		}
	}

	private void attachComponentBag() {
		try (OObjectDatabaseTx db = acquire()) {
			for (Component com : components) {
				db.attach(com);
				// logger.debug("attached {} ", com);
			}
		}
	}

	private void detachComponentBag() {
		try (OObjectDatabaseTx db = acquire()) {
			Set<Component> detached = new HashSet<>();
			for (Component com : components) {
				detached.add(db.detachAll(com, true));
				// logger.debug("detached {} ", com);
			}
			this.components = detached;
		}
	}

	public PersistentEntity<E> save() {

		attachComponentBag();
		try (OObjectDatabaseTx db = acquire()) {
			logger.debug("saving {} {} ", getId(), components);
			db.attach(this);
			PersistentEntity<E> stored = db.save(this, type.getSimpleName());
			stored.setPool(pool);
			return db.detachAll(stored, true);
		}

	}

	public <C extends Component> Component addComponent(Component component, Class<C> type) {
		if (component == null) {
			try {
				component = type.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("Error creating component");
				return null;
			}
		}

		try (OObjectDatabaseTx db = acquire()) {
			if (checkExistsObjectOfType(components, type)) {
				throw new RuntimeException(
						"trying to add a component " + component + " which already exists in the set of " + id);
			}

			OClass oclass = db.getMetadata().getSchema().getClass(PersistentEntity.class);
			if (!oclass.existsProperty(type.getSimpleName())) {
				// TODO schema updates might not be ideal in this spot. it gets
				// called during every new instanciation od ship.
				OClass ocomponent = db.getMetadata().getSchema().getClass(type);
				oclass.createProperty(type.getSimpleName(), OType.LINK, ocomponent).setMax("1");
				db.getMetadata().getSchema().save();
			}
			// TODO: add a checkl to see if the component is already added. only
			// 1
			// of each type is allowed.
			// ODocument doc = db.getRecordByUserObject(component, true);

			components.add(component);
			return component;
		}
	}

	private boolean checkExistsObjectOfType(Collection<?> c, Class<?> type) {
		for (Object o : c) {
			if (type.isInstance(o)) {
				return true;
			}
		}
		return false;
	}

	public <C extends Component> void removeComponent(Class<C> type) {
		for (Component com : components) {
			if (com.getClass().equals(type)) {
				components.remove(com);
			}
		}
	}

	public Set<Component> getComponents() {
		return components;
	}

	public boolean hasNameComponent() {
		for (Component com : components) {
			if (NameComponent.class.isInstance(com)) {
				return true;
			}
		}
		return false;
	}

	public ORID getId() {
		return id;
	}
}

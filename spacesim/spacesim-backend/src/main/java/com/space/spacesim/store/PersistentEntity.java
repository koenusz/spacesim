package com.space.spacesim.store;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.google.inject.Inject;
import com.guicemodel.ComponentFactory;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.space.spacesim.model.util.component.NameComponent;

import lombok.NonNull;
import lombok.Setter;

public class PersistentEntity<E extends Entity> {

	private static final Logger logger = LoggerFactory.getLogger(PersistentEntity.class);

	@NonNull
	@Setter
	private ODocument entity = new ODocument();

	private List<Component> components = new ArrayList<>();

	@Setter
	private Class<E> type;

	@Inject
	private ComponentFactory factory;

	@Setter
	@Inject
	private OPartitionedDatabasePool pool;

	public void load() {

		// TODO: make sure the ashley engine does not hold onto old components

		try (ODatabaseDocumentTx db = pool.acquire()) {
			entity = db.load(entity);
			for (Object fieldValue : entity.fieldValues()) {

				if (fieldValue instanceof ORecordId) {

					ODocument odoc = db.load((ORecordId) fieldValue);
					Component com = findComponent(odoc.getClassName());
					if (com != null) {
						copyFieldsFromEntity(com);
					} else {
						// If there is no component already, in effect if this
						// is a newly loaded instance. Create a new component.
						com = factory.create(odoc.getClassName());
					}
					components.add(com);
				}
			}
		}

	}

	private Component findComponent(String className) {
		for (Component com : components) {
			if (com.getClass().getSimpleName().equals(className)) {
				return com;
			}
		}
		return null;
	}

	private ODocument findDoc(String className) {
		if (entity.containsField(className)) {
			return entity.field(className);
		}
		// logger.error("field {} not found in DB entity {}", className, type +
		// getId().toString());
		ODocument doc = new ODocument();
		doc.setClassName(className);
		return doc;
	}

	public void save() {

		try (ODatabaseDocumentTx db = pool.acquire()) {
			logger.debug("saving {} {} ", getId(), components);
			for (Component component : components) {
				copyFieldsToEntity(component);
			}
			entity = db.save(entity, type.getSimpleName());
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

		try (ODatabaseDocumentTx db = pool.acquire()) {
			if (checkExistsObjectOfType(components, type)) {
				throw new RuntimeException(
						"trying to add a component " + component + " which already exists in the set of " + getId());
			}

			OClass oclass = db.getMetadata().getSchema().getClass(PersistentEntity.class);
			if (!oclass.existsProperty(type.getSimpleName())) {
				// TODO schema updates might not be ideal in this spot. it gets
				// called during every new instantiation of ship.
				OClass ocomponent = db.getMetadata().getSchema().getClass(type);
				oclass.createProperty(type.getSimpleName(), OType.LINK, ocomponent).setMax("1");
				db.getMetadata().getSchema().save();
			}

			ODocument componentDoc = new ODocument();
			componentDoc.setClassName(component.getClass().getSimpleName());
			components.add(component);
			entity.field(type.getSimpleName(), componentDoc);
			return component;
		}
	}

	private void copyFieldsToEntity(Component component) {
		
		ODocument doc = findDoc(component.getClass().getSimpleName());
		
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(component.getClass(), Object.class)
					.getPropertyDescriptors()) {

				// propertyEditor.getReadMethod() exposes the getter
				// btw, this may be null if you have a write-only property
				// logger.debug("readmethod {}", propertyDescriptor.getReadMethod());
				// logger.debug("name {}", propertyDescriptor.getName());
				Object value = propertyDescriptor.getReadMethod().invoke(component);
				doc.field(propertyDescriptor.getName(), value);

			}
			entity.field(component.getClass().getSimpleName(), doc);
			
			
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("Error while introspecting {} ", component);
			e.printStackTrace();
		}
	}

	private void copyFieldsFromEntity(Component component) {
		try {
			ODocument doc = findDoc(component.getClass().getSimpleName());
			
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(component.getClass(), Object.class)
					.getPropertyDescriptors()) {

				// propertyEditor.getReadMethod() exposes the getter
				// btw, this may be null if you have a write-only property
				logger.debug("readmethod {}", propertyDescriptor.getReadMethod());
				logger.debug("name {}", propertyDescriptor.getName());
				Object fieldvalue =  doc.field(propertyDescriptor.getName());
						
				propertyDescriptor.getWriteMethod().invoke(component,fieldvalue);
				

			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("Error while introspecting {} ", component.getClass().getSimpleName());
			e.printStackTrace();
		}
	}

	private boolean checkExistsKeyOfType(Map<?, ?> c, Class<?> type) {
		return checkExistsObjectOfType(c.keySet(), type);
	}

	private boolean checkExistsObjectOfType(Collection<?> c, Class<?> type) {
		for (Object o : c) {
			if (type.isInstance(o)) {
				return true;
			}
		}
		return false;
	}

	public <C extends Component> void removeComponent(Class<C> componentType) {

		for (Component com : components) {
			if (com.getClass().equals(componentType)) {
				components.remove(com);
			}
		}
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
		return entity.getIdentity();
	}
}

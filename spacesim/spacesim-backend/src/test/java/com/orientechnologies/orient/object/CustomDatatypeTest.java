package com.orientechnologies.orient.object;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.badlogic.ashley.core.Entity;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.serialization.serializer.object.OObjectSerializer;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.serialization.OObjectSerializerContext;
import com.orientechnologies.orient.object.serialization.OObjectSerializerHelper;
import com.space.spacesim.model.common.component.Target;
import com.space.spacesim.model.entity.AbstractEntity;
import com.space.spacesim.model.entity.Ship;
import com.space.spacesim.model.ship.component.BeamWeapon;
import com.space.spacesim.model.util.component.NameComponent;
import com.space.spacesim.model.util.system.NameSystem;
import com.space.spacesim.store.PersistentEntity;

public class CustomDatatypeTest {

	public static class WrappedString {
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Key [value=" + getValue() + "]";
		}
	}

	public static class Entity2 {
		private String name;

		private WrappedString data;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public WrappedString getData() {
			return data;
		}

		public void setData(WrappedString value) {
			this.data = value;
		}

		@Override
		public String toString() {
			return "Entity [name=" + getName() + ", data=" + getData() + "]";
		}
	}

	// private final String REMOTE = "remote:192.168.99.100:32769/customType";
	private final String LOCAL = "memory:CustomDatatypeTest";

	@Test
	public void reproduce() throws Exception {
		final OObjectDatabaseTx db = new OObjectDatabaseTx(LOCAL);
		db.create();
		// WrappedString custom datatype registration (storing it as
		// OType.STRING)
		OObjectSerializerContext serializerContext = new OObjectSerializerContext();
		serializerContext.bind(new OObjectSerializer<WrappedString, String>() {
			@Override
			public String serializeFieldValue(Class<?> iClass, WrappedString iFieldValue) {
				return iFieldValue.getValue();
			}

			@Override
			public WrappedString unserializeFieldValue(Class<?> iClass, String iFieldValue) {
				final WrappedString result = new WrappedString();
				result.setValue(iFieldValue);
				return result;
			}
		}, db);
		OObjectSerializerHelper.bindSerializerContext(WrappedString.class, serializerContext);

		// we want schema to be generated
		db.setAutomaticSchemaGeneration(true);

		// register our entity
		db.getEntityManager().registerEntityClass(Entity2.class);

		// Validate DB did figure out schema properly
		Assert.assertEquals(db.getMetadata().getSchema().getClass(Entity2.class).getProperty("data").getType(),
				OType.STRING);

		WrappedString w = new WrappedString();
		w.setValue("wrapped");

		Entity2 e = new Entity2();
		e.setData(w);

		db.save(e);

	}

	// @Test(dependsOnMethods="reproduce")
	public void storeCustomType() {
		WrappedString w = new WrappedString();
		w.setValue("wrapped");

		Entity2 e = new Entity2();
		e.setData(w);

		try (OObjectDatabaseTx db = new OObjectDatabaseTx(LOCAL)) {

			db.save(e);
		}
	}

	// @Test
	public void nowShip() throws Exception {
		final OObjectDatabaseTx db = new OObjectDatabaseTx(LOCAL);
		db.open("root", "root");

		// WrappedString custom datatype registration (storing it as
		// OType.STRING)
		OObjectSerializerContext serializerContext = new OObjectSerializerContext();
		serializerContext.bind(new OObjectSerializer<Entity, ORID>() {

			@Override
			public ORID serializeFieldValue(Class<?> iClass, Entity iFieldValue) {

				if (iFieldValue instanceof AbstractEntity) {
					return ((AbstractEntity) iFieldValue).getId();
				}

				return null;
			}

			@Override
			public Entity unserializeFieldValue(Class<?> iClass, ORID iFieldValue) {
				final Entity result = new Entity();

				return result;
			}
		}, db);
		OObjectSerializerHelper.bindSerializerContext(Entity.class, serializerContext);

		// we want schema to be generated
		db.setAutomaticSchemaGeneration(true);

		// register our entity
		db.getEntityManager().registerEntityClass(PersistentEntity.class);
		db.getEntityManager().registerEntityClass(BeamWeapon.class);

		// Validate DB did figure out schema properly
		Assert.assertEquals(db.getMetadata().getSchema().getClass(BeamWeapon.class).getProperty("target").getType(),
				OType.LINK);
	}

	// @Test(dependsOnMethods="nowShip")
	public void storeShip() {

		Ship ship = new Ship(new NameSystem());
		ship.setName("testShip");
		ship.add(new NameComponent());

		BeamWeapon w = new BeamWeapon();
		w.setTarget(ship.getComponent(Target.class));

		try (OObjectDatabaseTx db = new OObjectDatabaseTx(LOCAL)) {
			db.open("root", "root");
			db.save(w);
		}
	}
}
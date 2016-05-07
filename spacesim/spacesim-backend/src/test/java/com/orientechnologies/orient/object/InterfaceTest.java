package com.orientechnologies.orient.object;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;


/**
 * A class used for providing proof of concept of our dynamic schema generation. 
 * 
 *
 */
public class InterfaceTest {

	
	public interface Component {

		public String getName();

	}

	public class ComponentA implements Component {

		private String name = "componenta";
		private ComponentB b;

		public ComponentB getB() {
			return b;
		}

		public void setB(ComponentB b) {
			this.b = b;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public class ComponentB implements Component {

		private String name = "componentb";

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}
	
	public class ComponentC implements Component {

		private String name = "componentc";

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public class ComponentEntity {

		@Id
		private ORID id;

		private Set<Component> components = new HashSet<>();

		public Set<Component> getComponents() {
			return components;
		}

		public void setComponents(Set<Component> components) {
			this.components = components;
		}
		public <C extends Component> void addComponent(OObjectDatabaseTx db, Component component, Class<C> type)
		{
			
			OClass oclass = db.getMetadata().getSchema().getClass(ComponentEntity.class);
			if (!oclass.existsProperty(type.getSimpleName())) {
				// TODO schema updates might not be ideal in this spot. it gets
				// called during every new instanciation od ship.
				OClass ocomponent = db.getMetadata().getSchema().getClass(type);
				oclass.createProperty(type.getSimpleName(), OType.LINK, ocomponent).setMax("1");
				db.getMetadata().getSchema().save();
			}
			components.add(component);
		}
		

		public ORID getId() {
			return id;
		}

		public void setId(ORID id) {
			this.id = id;
		}
	}

	@Test
	public void nest() throws Exception {
		try (OObjectDatabaseTx db = new OObjectDatabaseTx("memory:nest")) {
			db.create();
			// we want schema to be generated
			//db.setAutomaticSchemaGeneration(true);

			// register our entity
			db.getEntityManager().registerEntityClass(ComponentA.class);
			db.getEntityManager().registerEntityClass(ComponentB.class);
			db.getEntityManager().registerEntityClass(ComponentEntity.class);

			// Validate DB did figure out schema properly
//			Assert.assertEquals(
//					db.getMetadata().getSchema().getClass(ComponentEntity.class).getProperty("components").getType(),
//					OType.LINKSET);

			ComponentEntity e = new ComponentEntity();
			e.addComponent(db, new ComponentA(), ComponentA.class);
			e.addComponent(db, new ComponentB(), ComponentB.class);
			for(Component c : e.getComponents())
			{
				if(c instanceof ComponentA)
				{
					((ComponentA) c).setB(new ComponentB());
				}
			}
			

			e = db.save(e);
			
			ComponentEntity f = db.load(e.getId());
			
			Assert.assertEquals(e.getId(), f.getId());
		}

	}
	
	@Test
	public  void  noNest() throws Exception {
		try (OObjectDatabaseTx db = new OObjectDatabaseTx("memory:nonest")) {
			db.create();
			// we want schema to be generated
			//db.setAutomaticSchemaGeneration(true);

			// register our entity
			db.getEntityManager().registerEntityClass(ComponentA.class);
			db.getEntityManager().registerEntityClass(ComponentB.class);
			db.getEntityManager().registerEntityClass(ComponentC.class);
			db.getEntityManager().registerEntityClass(ComponentEntity.class);

			// Validate DB did figure out schema properly
//			Assert.assertEquals(
//					db.getMetadata().getSchema().getClass(ComponentEntity.class).getProperty("components").getType(),
//					OType.LINKSET);

			ComponentEntity e = new ComponentEntity();
			e.addComponent(db,new ComponentB(), ComponentB.class);
			e.addComponent(db,new ComponentC(), ComponentC.class);

			e = db.save(e);
			
			ComponentEntity f = db.load(e.getId());
			
			Assert.assertEquals(e.getId(), f.getId());
		}

	}
}

package com.space.spacesim.model.util.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.inject.Inject;
import com.space.spacesim.model.util.component.NameComponent;

public class NameSystem extends EntitySystem {

	@Inject
	ComponentMapper<NameComponent> names;

	public void setType(Entity entity, String type) {
		names.get(entity).setName(type);
	}

	public void setName(Entity entity, String name) {
		NameComponent po = names.get(entity);
		po.setName(name);
	}

	public String getType(Entity entity) {
		return names.get(entity).getType();
	}

	public String getName(Entity entity) {
		return names.get(entity).getName();
	}

	public String toString(Entity entity) {
		if (entity == null) {
			return "Entity is null";
		}

		String name = names.get(entity).getName();
		String type = names.get(entity).getType();
		return "{" + type + ":" + name + "}";
	}

}

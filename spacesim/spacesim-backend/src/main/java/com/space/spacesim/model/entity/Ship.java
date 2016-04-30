package com.space.spacesim.model.entity;

import com.google.inject.Inject;
import com.space.spacesim.model.ship.component.Armor;
import com.space.spacesim.model.ship.component.BeamWeapon;
import com.space.spacesim.model.ship.component.Hull;
import com.space.spacesim.model.ship.component.Shield;
import com.space.spacesim.model.util.system.NameSystem;
import com.space.spacesim.store.PersistentEntity;

public class Ship extends AbstractEntity<Ship> {

	@Inject
	public Ship(NameSystem names, PersistentEntity<Ship> persistentEntity) {
		super(names, persistentEntity);
	}

	public Ship(NameSystem names) {
		super(names);
	}

	// TODO: decide if this allowed to stay
	public void initDefaultComnponentSet() {
		this.addComponent(Armor.class);
		this.addComponent(Shield.class);
		this.addComponent(Hull.class);
		this.addComponent(BeamWeapon.class);

	}

}

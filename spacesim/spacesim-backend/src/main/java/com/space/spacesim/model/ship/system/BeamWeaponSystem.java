package com.space.spacesim.model.ship.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.inject.Inject;
import com.space.spacesim.model.ship.component.BeamWeapon;
import com.space.spacesim.model.ship.component.Shield;

public class BeamWeaponSystem extends EntitySystem {

	private static final Logger logger = LoggerFactory.getLogger(ShieldSystem.class);

	@Inject
	ComponentMapper<BeamWeapon> beamWeapons;

	@Inject
	Engine engine;

	private ImmutableArray<Entity> entities;


	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(BeamWeapon.class).get());
	}

	public void update(float deltaTime) {
	}
	
	//TODO Should be moved to a more generic attacksystem
	public void attack(Entity attacker, Entity target)
	{
		BeamWeapon weapon = attacker.getComponent(BeamWeapon.class);
		shoot(target, weapon.getPower());
	}

	private void shoot(Entity target, int damageAmount) {

		ShieldSystem shieldSystem = engine.getSystem(ShieldSystem.class);
		int overflow = shieldSystem.recieveBeamDamage(target.getComponent(Shield.class), damageAmount);
		logger.debug("overflow {}", overflow);
	}

}

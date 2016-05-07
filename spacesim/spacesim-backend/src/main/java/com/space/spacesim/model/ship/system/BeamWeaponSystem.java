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
import com.space.spacesim.model.common.component.Target;
import com.space.spacesim.model.ship.component.BeamWeapon;

public class BeamWeaponSystem extends EntitySystem {

	private static final Logger logger = LoggerFactory.getLogger(ShieldSystem.class);

	@Inject
	ComponentMapper<BeamWeapon> beamWeapons;

	@Inject
	Engine engine;

	private ImmutableArray<Entity> entities;

	public void target(Entity attacker, Target target) {
		logger.debug("{} targetting {} ", attacker, target);
			
		BeamWeapon attackerWeapon = beamWeapons.get(attacker);
		if (attackerWeapon != null) {
			attackerWeapon.setTarget(target);
		} else {
			logger.error("beamweapon for {} not found" + attacker);
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(BeamWeapon.class).get());
	}

	public void update(float deltaTime) {
		for (Entity entity : entities) {
			BeamWeapon beamWeapon = beamWeapons.get(entity);
			if (beamWeapon.getTarget() != null) {
				shoot(beamWeapon.getTarget(), beamWeapon.getPower());
			}
		}
	}

	private void shoot(Target target, int damageAmount) {

		ShieldSystem shieldSystem = engine.getSystem(ShieldSystem.class);
		int overflow = shieldSystem.recieveBeamDamage(target.getShield(), damageAmount);
		logger.debug("overflow {}", overflow);
	}

}

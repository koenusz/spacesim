package com.space.spacesim.model.ship.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.inject.Inject;
import com.space.spacesim.model.ship.component.BeamWeapon;
import com.space.spacesim.model.ship.component.Hull;
import com.space.spacesim.model.ship.component.Shield;
import com.space.spacesim.model.util.component.NameComponent;

public class ShieldSystem extends EntitySystem {

	private static final Logger logger = LoggerFactory.getLogger(ShieldSystem.class);

	@Inject
	ComponentMapper<Shield> shields;

	@Inject
	ComponentMapper<Hull> hulls;

	public void powerUp(Entity entity) {
		
		String name = entity.getComponent(NameComponent.class).getName();
		logger.debug("powering up shields of {}", name);
		Shield shield = shields.get(entity);
		shield.setActivated(true); 
		Hull hull = hulls.get(entity);
		shield.setShieldwidth(hull.getSize());
		shield.setMatrix(new int[shield.getShieldstrength()][hull.getSize()]);

		// a shield of 0 means it is not damaged
		for (int[] strengthLevel : shield.getMatrix()) {
			for (int i = 0; i < strengthLevel.length; i++) {
				strengthLevel[i] = 1;
			}
		}

		logger.debug("shield up with strengt {} and width {}", shield.getShieldstrength(), shield.getShieldwidth());
	}

	/**
	 * Damages the shield. Has an array of {@link int} as parameter so the
	 * weapon can determine how the shield will be damaged.
	 * <p>
	 * {@link BeamWeapon} damage goes by layer. It is not possible to damage the
	 * underlying layer if the layer above it is not damaged yet.
	 * 
	 * @param entity,
	 *            the target
	 * @param damageAmount
	 * 
	 * @return Returns the amount of damage the shield could not absorb;
	 */
	public int recieveBeamDamage(Entity entity, int damageAmount) {

		int overflow = damageAmount;
		Shield shield = shields.get(entity);
		if (shield.isActivated()) {
			logger.debug("shields taking {} damage", damageAmount);
			// determines from where the shield will be stripper
			for (int[] level : shield.getMatrix()) {
				if (overflow > 0) {
					// first if the damage taken is greater then the strength
					// left
					// in the level, deplete the level completely.
					// total strength minus damage taken.
					int levelStrength = count(level);
					if (levelStrength <= overflow) {
						allToZero(level);
						overflow = overflow - levelStrength;
					} else {
						// here we know that the amount of damage left is
						// smaller then the shield strength of the level.
						for (int i = 0; i < level.length; i++) {

							// if the shield is up damage it
							if (level[i] == 1) {
								level[i] = 0;
								overflow--;
								if(overflow == 0)
								{
									logger.debug(shieldStatus(entity));
									return overflow;
								}
							}
						}
					}
				}
			}
		}
		logger.debug(shieldStatus(entity));
		return overflow;
	}

	private void allToZero(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = 0;
		}
	}

	// number of 1 values
	private int count(int[] array) {
		int count = 0;
		for (int i : array) {
			if (i == 1)
				count++;
		}
		return count;
	}

	public String shieldStatus(Entity entity) {
		Shield shield = shields.get(entity);
		StringBuilder builder = new StringBuilder();
		builder.append("X means shield present");
		if (!shield.isActivated()) {
			return "Status: inactive";
		}
		int i = 1;
		for (int[] strengthLevel : shield.getMatrix()) {
			builder.append("\n\t" + i + "\t");

			int powerLeft = 0;
			for (int hullPoint : strengthLevel)
				if (hullPoint == 1) {
					builder.append("|X");
					powerLeft++;
				} else {
					builder.append("| ");
				}
			builder.append("| power: " + powerLeft);
		}
		return builder.toString();
	}

}

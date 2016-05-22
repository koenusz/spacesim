package com.space.spacesim.model.ship.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public void powerDown(Shield shield) {
		shield.setMatrix(null);
		shield.setActivated(false);
	}

	/**
	 * Initialise the shieldmatrix, a value of 1 in a shieldpoint means there is
	 * active shield in this slot.
	 * 
	 * @param entity
	 */
	public void powerUp(Entity entity) {

		String name = entity.getComponent(NameComponent.class).getName();
		logger.debug("powering up shields of {}", name);
		Shield shield = shields.get(entity);
		shield.setActivated(true);
		Hull hull = hulls.get(entity);
		shield.setShieldwidth(hull.getSize());
		shield.setMatrix(intitialiseMatrix(shield));

		logger.debug("shield up with strength {} and width {}", shield.getShieldstrength(), shield.getShieldwidth());
	}

	private List<List<Integer>> intitialiseMatrix(Shield shield) {
		List<List<Integer>> matrix = new ArrayList<List<Integer>>();
		for (int levelIndex = shield.getShieldstrength(); levelIndex > 0; levelIndex--) {
			matrix.add(new ArrayList<Integer>(Collections.nCopies(shield.getShieldwidth(), 1)));
		}
		return matrix;
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
	public int recieveBeamDamage(Shield shield, int damageAmount) {

		int overflow = damageAmount;

		if (shield.isActivated()) {
			logger.debug("shields taking {} damage", damageAmount);
			// determines from where the shield will be stripper
			for (List<Integer> level : shield.getMatrix()) {
				if (overflow > 0) {
					// first if the damage taken is greater then the strength
					// left
					// in the level, deplete the level completely.
					// total strength minus damage taken.
					int levelStrength = countActive(level);
					if (levelStrength <= overflow) {
						Collections.replaceAll(level, 1, 0);
						overflow = overflow - levelStrength;
					} else {
						// here we know that the amount of damage left is
						// smaller then the shield strength of the level.
						for (int i = 0; i < level.size();  i++ ) {

							// if the shield is up damage it
							if (level.get(i) == 1) {
								level.set(i, 0);
								overflow--;
								if (overflow == 0) {
									logger.debug(shieldStatus(shield));
									return overflow;
								}
							}
						}
					}
				}
			}
		}
		logger.debug(shieldStatus(shield));
		return overflow;
	}

	// number of 1 values
	private int countActive(List<Integer> level) {
		int count = 0;
		for (int i : level) {
			if (i == 1)
				count++;
		}
		return count;
	}

	public String shieldStatus(Shield shield) {
		if (shield == null) {
			return "Shield is null";
		}
		
		if (!shield.isActivated()) {
			return "Status: inactive";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("X means shield present");
		
		for (List<Integer> strengthLevel : shield.getMatrix()) {
			builder.append("\n\t" + shield.getMatrix().indexOf(strengthLevel) + "\t");

			int powerLeft = 0;
			for (int shieldPoint : strengthLevel)
				if (shieldPoint == 1) {
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

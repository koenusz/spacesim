package com.space.spacesim.model.ship.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

public class ArmorSystem extends EntitySystem {

	
	private static final Logger logger = LoggerFactory.getLogger(ArmorSystem.class);

	
	
	/**
	 * Damages the shield. Has an array of int as parameter so the weapon can
	 * determine how the shield will be damaged. There are a few restrictions
	 * for the input int[].
	 * <ul>
	 * <li>There should be 1 or more intarrays.</li>
	 * <li>int[0] is the layer of the shield, int[1] is the location along the
	 * hul;l that got damaged.</li>
	 * </ul>
	 * 
	 * 
	 * @param entity
	 * @param shieldmatrixcoordinate
	 * 
	 * @return Returns the amount of damage the shield could not absorb;
	 */
	public int recieveBeamDamage(Entity entity, int damageAmount) {
		logger.debug("shields taking damage");
		int overflow = damageAmount;
//		Shield shield = shields.get(entity);
//
//		int impactPoint = (int) (Math.random() * shield.shieldwidth);
//		for (BitSet level : shield.matrix) {
//			
//			//the impactpoint is the nect 
//			impactPoint = level.nextClearBit(impactPoint);
//
//			if (overflow > 0) {
//				// if the shieldpoint is charged, damage it
//				if (!level.get(impactPoint)) {
//					level.set(impactPoint);
//				}
//
//				impactPoint++;
//				overflow--;
//			}
//
//		}
//
//		logger.debug(shieldStatus(entity));

		return overflow;
	}
	
}

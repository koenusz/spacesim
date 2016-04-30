package com.space.spacesim.model.ship.component;

import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;

import lombok.Data;

@Data
public class Armor implements Component {
	
	private static final Logger logger = LoggerFactory.getLogger(Armor.class);

	@Id
	private ORID id;
	
	private int armorClass = 5;
	
	public void setArmorClass(int armorClass)
	{
		logger.debug("setting armorclass to {}", armorClass);
		this.armorClass = armorClass;
	}

}

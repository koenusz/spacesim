package com.space.spacesim.model.common.component;

import javax.persistence.Id;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;
import com.space.spacesim.model.ship.component.Hull;
import com.space.spacesim.model.ship.component.Shield;

import lombok.Data;

@Data
public class Target implements Component{
	
	@Id
	private ORID id;
	
	private Shield shield;
	
	private Hull hull;
	
	

}

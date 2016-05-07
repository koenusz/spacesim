package com.space.spacesim.model.ship.component;

import javax.persistence.Id;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;
import com.space.spacesim.model.common.component.Target;

import lombok.Data;

@Data
public class BeamWeapon implements Component {
	
	@Id
	private ORID id;
	
	private int power = 3 ;
	
	private Target target;

}

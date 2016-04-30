package com.space.spacesim.model.ship.component;

import javax.persistence.Id;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.orientechnologies.orient.core.id.ORID;

import lombok.Data;

@Data

public class BeamWeapon implements Component {
	
	@Id
	private ORID id;
	
	private int power = 3 ;
	
	private Entity target;

}

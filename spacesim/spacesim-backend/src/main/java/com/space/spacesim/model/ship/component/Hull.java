package com.space.spacesim.model.ship.component;

import javax.persistence.Id;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;

import lombok.Data;

@Data

public class Hull implements Component {
	
	@Id
	private ORID id;
	
	private int size = 5;

}

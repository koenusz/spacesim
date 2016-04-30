package com.space.spacesim.model.ship.component;

import javax.persistence.Id;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;

import lombok.Data;

@Data

public class Shield implements Component {
	
	@Id
	private ORID id;
	
	private int shieldstrength = 2;
	
	//equal to hullsize
	private int shieldwidth = 0;
	
	private int rechargerate = 1;
	
	private int[][] matrix;
	
	private boolean activated;

}

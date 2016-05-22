package com.space.spacesim.model.ship.component;

import java.util.List;

import com.badlogic.ashley.core.Component;

import lombok.Data;

@Data

public class Shield implements Component {
	
	
	private int shieldstrength = 2;
	
	//equal to hullsize
	private int shieldwidth = 0;
	
	private int rechargerate = 1;
	
	private List<List<Integer>> matrix;
	
	private boolean activated;

}

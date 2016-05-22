package com.space.spacesim.model.util.component;

import com.badlogic.ashley.core.Component;

import lombok.Data; 

@Data
public class NameComponent implements Component { 
	
	private String type = "";
	
	private String name = ""; 
	
	private String classification = "";
 
}
package com.space.spacesim.model.util.component;

import javax.persistence.Id;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;

import lombok.Data; 

@Data
public class NameComponent implements Component { 
	
	@Id
	private ORID id;
    
	private String type = "";
	
	private String name = ""; 
	
	private String classification = "";
 
}
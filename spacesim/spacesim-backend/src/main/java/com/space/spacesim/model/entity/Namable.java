package com.space.spacesim.model.entity;

public interface Namable {

	public void setName(String name);

	public String getName();

	/**
	 * not the java class this property is to allow for a custom grouping. The entitytype is set by the java simple class name.
	 * @return 
	 */
	public void setClassification(String classification);

	public String getClassification();

}

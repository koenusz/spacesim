package com.space.spacesim.store;

import com.badlogic.ashley.core.Component;
import com.orientechnologies.orient.core.id.ORID;
import com.space.spacesim.model.entity.AbstractEntity;

public interface Storable<A extends AbstractEntity<A>> {
	
	public ORID getId() ;

	public void reload() ;

	public void load(ORID id);
	
	public void save();
	
	public <C extends Component> void addComponent(Class<C> componentType);
	//TODO: maybe add remove and delete from store.
	public <C extends Component> void removeComponent(Class<C> componentType);
	
	public void setPersistentEntity(PersistentEntity<A> entity);
}

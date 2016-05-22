package com.guicemodel;

import com.badlogic.ashley.core.Component;

public interface ComponentFactory {
	
	public <C extends Component> void registerComponent(Class<C> componentType);
	
	public Component create(String componentType);

}

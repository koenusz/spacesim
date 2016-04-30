package com.guicemodel;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.google.inject.Provider;
import com.space.spacesim.store.ComponentTypeMapper;

public class ComponentMapperProvider<C extends Component> implements Provider<ComponentMapper<C>> {

	private Class<C> mapperType;
	
	ComponentTypeMapper componentTypeMapper;
	
	public ComponentMapperProvider(Class<C> type) {
		
		mapperType = type; //(Class<C>) componentTypeMapper.getProxyType(type);
	}

	@Override
	public ComponentMapper<C> get() {
		
		return ComponentMapper.getFor(mapperType);
	}
	
	
}

package com.space.spacesim.store;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.google.inject.Inject;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;




/**
 * Since orientDB works with proxy object we have to keep track of the mapping between components and stored components. This is because ashley resolves the components base
 * 
 * @author Koen van Dijk
 *
 * @param <C>
 * @param <CP>
 */
public class ComponentTypeMapper {
	
	@Inject private OObjectDatabaseTx db;

	private final Logger logger = LoggerFactory.getLogger(ComponentTypeMapper.class);

	private Map<Class<? extends Component>, Class<? extends Component>> typeMap = new HashMap<>();

	public void mapType(Class<? extends Component> componentType, Class<? extends Component> componentProxyType) {
		if (componentType != null && componentProxyType != null && !typeMap.containsKey(componentType)) {
			typeMap.put(componentType, componentProxyType);
		} else if (typeMap.containsKey(componentType)) {
			logger.error("Map already contains a {} component", componentType);
		} else {
			logger.error("failed adding {} an {}", componentType, componentProxyType);
		}
	}

	public Class<? extends Component> getProxyType(Class<? extends Component> componentType) {
		if (componentType == null) {
			return null;
		}

		Class<? extends Component> componentProxyType = typeMap.get(componentType);
		if (componentProxyType == null) {
			//if there is no mapping get one from the database/javaassist.
			componentProxyType = db.newInstance(componentType).getClass();
			typeMap.put(componentType, componentProxyType);
		}
		return componentProxyType;
	}

}

package com.guicemodel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;

public class ComponentFactoryImpl implements ComponentFactory {

	private static final Logger logger = LoggerFactory.getLogger(ComponentFactoryImpl.class);

	private Map<String, Class<? extends Component>> mapping = new HashMap<>();

	public <C extends Component> void registerComponent(Class<C> componentType) {
		mapping.put(componentType.getSimpleName(), componentType);
	}

	@Override
	public Component create(String componentType) {
		if (componentType == null) {
			return null;
		} else if (mapping.get(componentType) == null) {
			logger.error("Component of type {} is not properly registered.", componentType);
		}
		try {
			return mapping.get(componentType).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {

			logger.error("error creating component of type {} {} ", componentType, e.getMessage());
		}
		return null;
	}

}

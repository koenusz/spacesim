package com.vaadin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.badlogic.ashley.core.Component;
import com.vaadin.data.util.BeanItemContainer;

public class BeanContainerBuilder {

	public static <C extends Component> BeanItemContainer<? extends Component> createBeanItemContainer(C editableComponent, Class<C> componenttype) {
		if (editableComponent == null) {
			return null;
		}
		List<C> list = new ArrayList<>();

		return createBeanItemContainer(list, componenttype);
	}

	public static <C extends Component> BeanItemContainer<? extends Component> createBeanItemContainer(Collection<C> beans, Class<C> componenttype) {
		if (CollectionUtils.isEmpty(beans)) {
			return null;
		}

		BeanItemContainer<C> beanContainer = new BeanItemContainer<>(componenttype);
		for (C bean : beans) {
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}

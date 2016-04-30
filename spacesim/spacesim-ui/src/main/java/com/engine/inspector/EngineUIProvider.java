package com.engine.inspector;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;

public class EngineUIProvider extends UIProvider {
	private static final long serialVersionUID = 5821024521446127058L;
	@Inject
	private Injector injector;

	@Override
	public EngineInspectorUI createInstance(UICreateEvent event) {
		return injector.getInstance(EngineInspectorUI.class);
	}

	@Override
	public Class<EngineInspectorUI> getUIClass(UIClassSelectionEvent event) {
		return EngineInspectorUI.class;
	}
}
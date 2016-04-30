package com.engine.inspector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

@Singleton
@VaadinServletConfiguration(productionMode = false, ui = EngineInspectorUI.class, widgetset = "com.vaadin.DefaultWidgetSet")
public class EngineInspectorServlet extends VaadinServlet implements SessionInitListener {

	private static final long serialVersionUID = 4514824431070027087L;
	protected final EngineUIProvider applicationProvider;

	@Inject
	public EngineInspectorServlet(EngineUIProvider applicationProvider) {
		this.applicationProvider = applicationProvider;
	}

	@Override
	protected void servletInitialized() {
		getService().addSessionInitListener(this);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		event.getSession().addUIProvider(applicationProvider);
	}
}
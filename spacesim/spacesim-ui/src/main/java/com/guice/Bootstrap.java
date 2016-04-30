package com.guice;

import javax.servlet.annotation.WebListener;

import com.engine.inspector.EngineInspectorServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.guicemodel.AshleyModule;
import com.guicemodel.OrientDBModule;
import com.guicemodel.PersistentEntitiesModule;
import com.space.spacesim.SpaceApplicationServlet;

@WebListener
public class Bootstrap extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/engine*").with(EngineInspectorServlet.class);

				serve("/space/*", "/*").with(SpaceApplicationServlet.class);
			}
		}, new AshleyModule(), new OrientDBModule("plocal:spacesim", "admin", "admin"), new PersistentEntitiesModule());
	}
}
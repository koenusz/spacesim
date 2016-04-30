package com.space.spacesim;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;


public class SpaceUIProvider extends UIProvider {  
	private static final long serialVersionUID = -7825186441942810497L;
	@Inject  
    private Injector injector;  
    @Override  
    public SpaceSimUI createInstance(UICreateEvent event) {  
      return injector.getInstance(SpaceSimUI.class);  
    }  
    @Override  
    public Class<SpaceSimUI> getUIClass(UIClassSelectionEvent event) {  
      return SpaceSimUI.class;  
    }  
  }  
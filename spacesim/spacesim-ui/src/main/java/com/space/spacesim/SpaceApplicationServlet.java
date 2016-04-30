package com.space.spacesim;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;


@Singleton  
@VaadinServletConfiguration(productionMode = false, ui = SpaceSimUI.class, widgetset = "com.vaadin.DefaultWidgetSet")  
public class SpaceApplicationServlet extends VaadinServlet implements SessionInitListener {  

	
	private static final long serialVersionUID = 5902492073891865515L;
protected final SpaceUIProvider applicationProvider;  
  @Inject  
  public SpaceApplicationServlet(SpaceUIProvider applicationProvider) {  
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
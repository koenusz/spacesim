package com.space.spacesim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("spacetheme")
@Widgetset("com.space.spacesim.SpaceSimWidgetset")
public class SpaceSimUI extends UI {

	private static final long serialVersionUID = -6501487842751132254L;
	private static final Logger logger = LoggerFactory.getLogger(SpaceSimUI.class);

//	@Inject
//	Ship aplha;
//	
//	@Inject
//	Ship beta;
//	
	ShipComponent shipView;
	
	Button button;
			

	
		@Override
		protected void init(VaadinRequest request){
			logger.debug("init spaceSim");
					
//			aplha.setName("alpha");
//			beta.setName("beta");
			
			VerticalLayout mainLayout = new VerticalLayout();
			setContent(mainLayout);
			
			Label lbl = new Label("Initiating ship combat");
			
			mainLayout.addComponent(lbl);
			
			button = new Button("Shoot");
		//	button.addClickListener( e-> { aplha.shoot(beta);
		//									shipView.updateHitpoints("" + beta.getHealth().getHitpoints());});
			mainLayout.addComponent(button);
		//	shipView = new ShipComponent(beta);
			
			
		}
}

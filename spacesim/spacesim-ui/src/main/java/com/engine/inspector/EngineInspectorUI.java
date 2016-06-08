package com.engine.inspector;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.inject.Inject;
import com.space.spacesim.Application;
import com.space.spacesim.model.entity.Ship;
import com.space.spacesim.proxy.EngineProxy;
import com.space.spacesim.proxy.StorageProxy;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.util.VaadinUtils;

/**
 *
 */
@Theme("spacetheme")
@Widgetset("com.space.spacesim.SpaceSimWidgetset")
public class EngineInspectorUI extends UI {

	private static final long serialVersionUID = -6759322987489785715L;

	private static final Logger logger = LoggerFactory.getLogger(EngineInspectorUI.class);

	@Inject
	EngineProxy engine;

	@Inject
	Application app;
	
	@Inject
	private StorageProxy storage;
	
	private static final String MAIN = "main";
	private static final String HEADER = "header";
	private static final String BODY = "body";
	private static final String ENGINETAB = "engineTab";
	private static final String STORAGETAB = "storageTab";
	private static final String ENTITIES = "entities";
	private static final String COMPONENTS = "components";
	private static final String COMPONENTINSPECTOR = "componentInspector";
	private static final String EDITOR = "editor";
	
	@Override
	protected void init(VaadinRequest request) {
		logger.debug("init engine inspector");

		// FIXME: remove this
		//app.init();

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setId(MAIN);
		setContent(mainLayout);

		HorizontalLayout header = new HorizontalLayout();
		header.setId(HEADER);
		Label headerLabel = new Label("Ashley Engine Inspector");
		
		

		HorizontalLayout body = new HorizontalLayout();
		body.setId(BODY);
		
		VerticalLayout leftMenu = new VerticalLayout();
		leftMenu.addComponent(new Label("Manage Entities"));
		leftMenu.addComponent(new Label());
		leftMenu.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		Button create = new Button("Create Ship");
		Button delete = new Button("Delete Ship");
		Button loadall = new Button("LoadAll Ship");
		loadall.addClickListener(c -> {
			logger.debug("clicked loadall");
			storage.loadAllIntoEngine(Ship.class);
		
		});
		leftMenu.addComponent(create);
		leftMenu.addComponent(delete);
		leftMenu.addComponent(loadall);
		
		
		TabSheet tabsheet = new TabSheet();
		
		
		HorizontalLayout storageTab = new HorizontalLayout();
		storageTab.setId(STORAGETAB);
		
		VerticalLayout storedEntities = new VerticalLayout();
		storedEntities.addComponent(new Label("Entities"));
	
		
		VerticalLayout storedComponents = new VerticalLayout();
		storedComponents.addComponent(new Label("Components"));
		
		VerticalLayout storedComponentInspector = new VerticalLayout();
		storedComponentInspector.addComponent(new Label("ComponentInspector"));
		
		storageTab.addComponent(storedEntities);
		storageTab.addComponent(storedComponents);
		storageTab.addComponent(storedComponentInspector);

		//Engine tab

		HorizontalLayout engineTab = new HorizontalLayout();
		engineTab.setId(ENGINETAB);
		VerticalLayout entities = new VerticalLayout();
		
		entities.setId(ENTITIES);
		VerticalLayout components = new VerticalLayout();
	
		components.setId(COMPONENTS);
		VerticalLayout componentInspector = new VerticalLayout();
		componentInspector.setId(COMPONENTINSPECTOR);
		
		VerticalLayout editor = new VerticalLayout();
	
		editor.setId(EDITOR);

		mainLayout.addComponent(header);
		header.addComponent(headerLabel);
		mainLayout.addComponent(body);
		body.addComponent(leftMenu);
		body.addComponent(tabsheet);
		tabsheet.addTab(engineTab, "Engine");
		tabsheet.addTab(storageTab, "Storage");
		engineTab.addComponent(entities);
		engineTab.addComponent(components);
		engineTab.addComponent(componentInspector);
		engineTab.addComponent(editor);


		addEntities();
		addComponents(null);
		addComponentInspector(null);

		
		

	}

	@SuppressWarnings("unchecked")
	public <T extends Component> void editable(T component) {

		Table entities = VaadinUtils.findById(this, "entityTable", Table.class);
		Entity selectedEntity = (Entity) entities.getValue();

		Class<T> clazz;
		if (component instanceof Component) {
			clazz = (Class<T>) component.getClass();
		} else {
			return;
		}
		T editableComponent = selectedEntity.getComponent(clazz);
		BeanItemContainer<T> components = new BeanItemContainer<>(clazz);
		components.addBean(editableComponent);

		Table table = createOrFindTable("The Editor", "editorTable");
		VerticalLayout vl = VaadinUtils.findById(this, EDITOR, VerticalLayout.class);
		vl.addComponent(table);
		table.setContainerDataSource(components);
		table.setEditable(true);

	}

	@SuppressWarnings("unchecked")
	private void addEntities() {
		VerticalLayout entities = VaadinUtils.findById(this, ENTITIES, VerticalLayout.class);
		Table table = createOrFindTable("The Entities", "entityTable");
		table.addContainerProperty("entityName", String.class, "No Name");
		table.addValueChangeListener(e -> this.addComponents((Entity) table.getValue()));
		entities.addComponent(table);

		ImmutableArray<Entity> ashleyEntities = engine.getEntities();

		for (Entity e : ashleyEntities) {
			table.addItem(e);
			Item row = table.getItem(e);
			row.getItemProperty("entityName").setValue(e.toString());

		}

	}

	@SuppressWarnings("unchecked")
	private void addComponents(Entity entity) {
		VerticalLayout components = VaadinUtils.findById(this, COMPONENTS, VerticalLayout.class);
		Table table = createOrFindTable("The components", "componentTable");
		table.removeAllItems();
		table.addValueChangeListener(e -> {
			this.addComponentInspector((Component) table.getValue());
			this.editable((Component) table.getValue());
		});
		table.addContainerProperty("componentName", String.class, "No Name");
		components.addComponent(table);
		if (entity != null) {
			for (Component c : entity.getComponents()) {
				table.addItem(c);
				Item row = table.getItem(c);
				row.getItemProperty("componentName").setValue(c.getClass().getSimpleName());

			}
		}

	}

	@SuppressWarnings("unchecked")
	private void addComponentInspector(Component component) {
		VerticalLayout components = VaadinUtils.findById(this, "componentInspector", VerticalLayout.class);
		Table table = createOrFindTable("The component's stored data", "inspectortTable");
		table.removeAllItems();
		table.addContainerProperty("fieldName", String.class, "No) Fieldname");
		table.addContainerProperty("value", String.class, "No Value");
		components.addComponent(table);

		if (component != null) {
			for (Field field : component.getClass().getDeclaredFields()) {
				boolean changedAccesibility = false;
				if (!field.isAccessible()) {
					field.setAccessible(true);
					changedAccesibility = true;
				}

				table.addItem(field);
				Item row = table.getItem(field);
				row.getItemProperty("fieldName").setValue(field.getName());
				try {
					row.getItemProperty("value").setValue(field.get(component).toString());
				} catch (ReadOnlyException | IllegalArgumentException | IllegalAccessException e) {
					row.getItemProperty("value").setValue("error getting field");
					e.printStackTrace();
				} catch (NullPointerException n) {
					// do nothing the default value is being set
				}
				if (changedAccesibility) {
					field.setAccessible(false);
				}
			}
		}
	}

	private Table createOrFindTable(String caption, String id) {
		Table table = VaadinUtils.findById(this, id, Table.class);

		if (table == null) {
			table = new Table(caption);
			table.setId(id);
			table.setSelectable(true);
			table.setImmediate(true);
		}
		return table;
	}

}

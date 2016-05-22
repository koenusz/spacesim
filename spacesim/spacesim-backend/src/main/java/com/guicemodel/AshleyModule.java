package com.guicemodel;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.space.spacesim.model.ship.component.Armor;
import com.space.spacesim.model.ship.component.BeamWeapon;
import com.space.spacesim.model.ship.component.Hull;
import com.space.spacesim.model.ship.component.Shield;
import com.space.spacesim.model.util.component.NameComponent;
import com.space.spacesim.proxy.EngineProxy;
import com.space.spacesim.proxy.EngineProxyImpl;

public class AshleyModule extends AbstractModule{
	
	private ComponentFactory componentFactory = new ComponentFactoryImpl();

	@Override
	protected void configure() {
		
		addComponentBinding(new TypeLiteral<ComponentMapper<Hull>>() {}, Hull.class);
		addComponentBinding(new TypeLiteral<ComponentMapper<BeamWeapon>>() {}, BeamWeapon.class);
		addComponentBinding(new TypeLiteral<ComponentMapper<Shield>>() {}, Shield.class);
		addComponentBinding(new TypeLiteral<ComponentMapper<Armor>>() {}, Armor.class);
		addComponentBinding(new TypeLiteral<ComponentMapper<NameComponent>>() {}, NameComponent.class);
		
		//bind(new TypeLiteral<ComponentMapper<Hull>>() {}).toProvider(new ComponentMapperProvider<>(Hull.class));
//		bind(new TypeLiteral<ComponentMapper<BeamWeapon>>() {}).toProvider(new ComponentMapperProvider<>(BeamWeapon.class));
//		bind(new TypeLiteral<ComponentMapper<Shield>>() {}).toProvider(new ComponentMapperProvider<>(Shield.class));
//		bind(new TypeLiteral<ComponentMapper<Armor>>() {}).toProvider(new ComponentMapperProvider<>(Armor.class));
//		bind(new TypeLiteral<ComponentMapper<NameComponent>>() {}).toProvider(new ComponentMapperProvider<>(NameComponent.class));
		bind(EngineProxy.class).to(EngineProxyImpl.class);
	}
	
	private <C extends Component> void addComponentBinding(TypeLiteral<ComponentMapper<C>> lit, Class<C> componentType)	{
		bind(lit).toProvider(new ComponentMapperProvider<>(componentType));
		componentFactory.registerComponent(componentType);
	}
	
	
	@Provides
	@Singleton
	public ComponentFactory provideComponentFactory()
	{
		return componentFactory;
	}
	
	
	@Provides 
	//@SessionScoped 
	@Singleton
	public Engine provideEngine()
	{
		//TODO: is there really 1 ?
		
		return new Engine();
	}
	


}

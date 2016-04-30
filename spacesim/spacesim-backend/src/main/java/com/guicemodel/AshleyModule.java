package com.guicemodel;

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

	@Override
	protected void configure() {
		
		bind(new TypeLiteral<ComponentMapper<Hull>>() {}).toProvider(new ComponentMapperProvider<>(Hull.class));
		bind(new TypeLiteral<ComponentMapper<BeamWeapon>>() {}).toProvider(new ComponentMapperProvider<>(BeamWeapon.class));
		bind(new TypeLiteral<ComponentMapper<Shield>>() {}).toProvider(new ComponentMapperProvider<>(Shield.class));
		bind(new TypeLiteral<ComponentMapper<Armor>>() {}).toProvider(new ComponentMapperProvider<>(Armor.class));
		bind(new TypeLiteral<ComponentMapper<NameComponent>>() {}).toProvider(new ComponentMapperProvider<>(NameComponent.class));
		bind(EngineProxy.class).to(EngineProxyImpl.class);
	}
	
	@Provides 
	//@SessionScoped 
	@Singleton
	public Engine provideEngine()
	{
		return new Engine();
	}
	


}

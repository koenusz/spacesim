package com.space.spacesim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.guicemodel.AshleyModule;
import com.guicemodel.OrientDBModule;
import com.guicemodel.PersistentEntitiesModule;
import com.space.spacesim.model.entity.Ship;
import com.space.spacesim.model.ship.system.BeamWeaponSystem;
import com.space.spacesim.model.ship.system.ShieldSystem;
import com.space.spacesim.model.util.system.NameSystem;
import com.space.spacesim.proxy.EngineProxy;
import com.space.spacesim.proxy.StorageProxy;

public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private static Injector sInjector;

	@Inject
	private EngineProxy engine;
	@Inject
	private Injector injector;
	@Inject
	private ShieldSystem shields;
	@Inject
	private BeamWeaponSystem beamWeapons;
	@Inject
	private StorageProxy storage;

	// private final static String LOCALURL = "plocal:spacesim";

	private final static String DOCKERURL = "remote:192.168.99.100:32769/spacesim";

	public static void main(String... args) {
		/*
		 * Guice.createInjector() takes your Modules, and returns a new Injector
		 * instance. Most applications will call this method exactly once, in
		 * their main() method.
		 */
		sInjector = Guice.createInjector(new AshleyModule(), new OrientDBModule(DOCKERURL, "admin", "admin"),
				new PersistentEntitiesModule());

		Application a = sInjector.getInstance(Application.class);

		a.init();
	}

	public void init() {

//		storage.loadAllIntoEngine(Ship.class);
//
//		ImmutableArray<Entity> loadedShips = engine.getEntities();
//		logger.debug("Printing loaded ships");
//		for (Entity ship : loadedShips) {
//			logger.debug("loaded: " + ship.toString());
//		}

		Ship ship1 = injector.getInstance(Ship.class);
		ship1.initDefaultComnponentSet();
		ship1.setName("ship1");
		Ship ship2 = injector.getInstance(Ship.class);
		
		ship2.initDefaultComnponentSet();
		

		ship2.setName("ship2");

		engine.addEntity(ship1);

		engine.addEntity(ship2);

		engine.addSystem(injector.getInstance(BeamWeaponSystem.class));
		engine.addSystem(injector.getInstance(ShieldSystem.class));
		engine.addSystem(injector.getInstance(NameSystem.class));

		
		beamWeapons.target(ship2, ship1);
		beamWeapons.target(ship1, ship2);

		ship1.save();
		ship2.save();
	
		
		
		logger.debug("after save: " + ship2.toString());

		// logger.debug(shields.shieldStatus(ship2));

		// shields.powerUp(ship2);

		// logger.debug(shields.shieldStatus(ship2));

		engine.update(1);

		engine.update(1);

		engine.update(1);

		// Set<Component> components2 = ship2.getPersistedComponents();
		//// ship2.reload();
		// Set<Component> reloaded = ship2.getPersistedComponents();
		//
		// Ship ship3 = injector.getInstance(Ship.class);
		// ship3.load(new ORecordId("#19:0"));
		// engine.addEntity(ship3);
		//
		// for (PersistentEntity ship :
		// obdb.browseClass(PersistentEntity.class)) {
		// logger.debug("found ship {}", ship.toString());
		// Component com = (Component) ship.getLinkbag().iterator().next();
		// //ODocument doc = ship.getLinkbag().iterator().next();
		// //Component component = (Component) obdb.getUserObjectByRecord(doc,
		// null);
		// //Component component2 = obdb.load(doc);
		//
		// PersistentEntity pp = obdb.detach(ship);
		//
		// logger.debug("found ship {}", pp.toString());
		//// if (ship.getId().equals("Ship 2")) {
		//// ship2 = application.db.detach(ship);
		//// }
		// }
		//
		//
		// Set<Component> loaded = ship3.getPersistedComponents();

	}

}

package cz.muni.fi.pv243.musicmanager.dao.impl;



import java.io.File;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;

import org.infinispan.commons.api.BasicCacheContainer;
import org.infinispan.configuration.cache.AsyncStoreConfigurationBuilder;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.SingleFileStoreConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.persistence.leveldb.configuration.LevelDBStoreConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.slf4j.Logger;

@ApplicationScoped
public class CacheContainerProvider {
    private BasicCacheContainer manager;

    @Inject
    private Logger logger;
    
    @Produces
    public BasicCacheContainer getCacheContainer() {
        if (manager == null) {
        	logger.info("Initialization of BasicCacheContainer...");
        	GlobalConfiguration glob = new GlobalConfigurationBuilder()
        	                          .nonClusteredDefault().globalJmxStatistics().enable()
        	                          .jmxDomain("cz.muni.fi.pv243.musicmanager")  //prevent collision with non-transactional musicmanager
        	                          .build();
        	
        	Configuration defaultConfig = new ConfigurationBuilder()
        	               .transaction().transactionMode(TransactionMode.TRANSACTIONAL)
        	               .build();  //default config
        	
        	Configuration musicCacheConfig = new ConfigurationBuilder().persistence()
        																.passivation(false)
        																.addSingleFileStore()
        																.preload(true)
        																.shared(false)
        																.fetchPersistentState(true)
        																.ignoreModifications(false)
        																.purgeOnStartup(false)
        																.location(System.getProperty("user.home") + File.separator + "music-manager" + File.separator + "data")
        																//.location("C:\\music-manager" + File.separator + "data")
        																.async().enabled(true).threadPoolSize(5)
        																	//TODO: find non-depricated equivalent
        																.indexing().enable().addProperty("default.directory_provider", "ram")
        															.build();
        	

        	manager = new DefaultCacheManager(glob, defaultConfig);
        	//defining cache for each entity
        	((DefaultCacheManager) manager).defineConfiguration("usercache", musicCacheConfig);
        	((DefaultCacheManager) manager).defineConfiguration("songcache", musicCacheConfig);
        	((DefaultCacheManager) manager).defineConfiguration("commentcache", musicCacheConfig);
        	((DefaultCacheManager) manager).defineConfiguration("interpretcache", musicCacheConfig);
        	manager.start();
        	logger.info("Using DefaultCacheManager (library mode)");
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }
}

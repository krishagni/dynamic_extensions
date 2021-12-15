package edu.common.dynamicextensions.query.cachestore;

import java.io.InputStream;
import java.util.UUID;

import edu.common.dynamicextensions.nutility.DeConfiguration;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

public class EhCacheManager {
	private static EhCacheManager instance;
	
	private static final String QUERY_EHCACHE_CONFIG = "query-ehcache.xml";
	
	private CacheManager manager;
	
	private EhCacheManager() {
		InputStream cfg = EhCacheManager.class.getResourceAsStream(QUERY_EHCACHE_CONFIG);
		manager = new CacheManager(cfg);
	}
	
	public static synchronized EhCacheManager getInstance() {
		if (instance == null) {
			instance = new EhCacheManager();
		}
		
		return instance;
	}
	
	public Ehcache newCache() {
		return newCache(DeConfiguration.getInstance().maxCacheElementsInMemory());
	}

	public Ehcache newCache(int maxElementsInMemory) {
		String cacheId = UUID.randomUUID().toString();
		Cache cache = new Cache(
			cacheId,               // cache name
			maxElementsInMemory,   // max elements in cache. any addition will cause eviction
			true,                  // overflow to disk. evicted element to be written to disk
			true,                  // do not expire disk elements
			0L,                    // time to live in seconds. live forever until explicitly removed
			0L,                    // time to idle in seconds. can be idle forever
			false,                 // disk persistent. need to be persistent across multiple starts
			0L);                   // disk thread expiry interval

		manager.addCache(cache);
		return manager.getCache(cacheId);
	}
	
	public void removeCache(Ehcache cache) {
		manager.removeCache(cache.getName());
	}
}
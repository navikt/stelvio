package no.stelvio.common.codestable;

//import org.springmodules.cache.annotations.*;

/**
 * TODO: javadoc
 */
public interface CodesTableItemRepository {
	
	//@Cacheable(modelId="persistent")
	public <T extends CodesTable> T findCodesTable(Class<T> codesTable);
	
}
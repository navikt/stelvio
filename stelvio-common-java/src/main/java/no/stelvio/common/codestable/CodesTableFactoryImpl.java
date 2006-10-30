package no.stelvio.common.codestable;

/**
 * Implementation of CodesTableFactory used to retrieve a codestable from the database.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableFactoryImpl implements CodesTableFactory {
	
	private CodesTableItemRepository codesTableItemRepository;
	
	/**
	 * TODO: DOCUMENT ME!
	 * @param codesTableItemRepository
	 */
	public void setCodesTableItemRepository(CodesTableItemRepository codesTableItemRepository){
		this.codesTableItemRepository = codesTableItemRepository;
	}
	
	/**
	 * TODO: document me!
	 * @param codesTable a <code>CodesTable</code> or <CodesTablePeriodic</> 
	 * @return T <code>CodesTable</code> or <CodesTablePeriodic</>
	 * @throws CodesTableException
	 */
	public <T extends CodesTable> T retrieveCodesTable(Class<T> codesTable) {
		
		T ctable = null;
		
		/*
		try{
			codesTableItemRepository.findCodesTable(codesTable);
		}
		catch(Exception err){
			//TODO: un-comment exception
			//throw new CodesTableException();throw new SystemException(FrameworkError.CODES_TABLE_INIT_ERROR, sfe);
		}
		
		if(ctable == null){
			//TODO: un-comment exception
			//throw new CodesTableException(); throw new SystemException(FrameworkError.CODES_TABLE_NOT_FOUND, codesTableClass.getName());
		}
		
		return ctable;	
		*/
		return null;
	}
}
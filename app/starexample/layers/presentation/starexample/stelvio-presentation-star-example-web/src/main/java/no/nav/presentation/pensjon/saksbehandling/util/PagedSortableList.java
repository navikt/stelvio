package no.nav.presentation.pensjon.saksbehandling.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;

public class PagedSortableList<T> {

	private List<T> list = new ArrayList<T>();
	
    private String sort;
    private boolean ascending;
	
    String lastColumn;
    
	private long numberOfPages;
	
	private long numberOfEntries;
	private int showStart;
	private int showEnd;
	
	private int numberOfRows;
	
	private int rowIndex;
	
	private int pageIndex;
	
	int listempty;
	
	public int getListempty() {
		if( list.isEmpty() )
			return 1;
		else
			return 0;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public PagedSortableList(	List<T> list, int numberOfRows, String defaultSort ) 
	{
		this.list = list;
		this.numberOfRows = numberOfRows;
		ascending = true;
		
		sort = defaultSort;
		sort( sort, ascending );
		lastColumn = sort;
		
		rowIndex = 0;
		pageIndex = 1;
		numberOfPages = (long) Math.ceil( (double) list.size() / (double) numberOfRows );
		
		numberOfEntries = list.size();
	}
	
	public long getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(long numberOfPages) {
		this.numberOfPages = numberOfPages;
	}


	public void pageing( ActionEvent event )
	{
		String way = event.getComponent().getId();
		
		System.out.println( "way = " + way );
		
		if( way.equals( "next" ) )
		{
			rowIndex += numberOfRows;
			pageIndex++;
		}
		else if( way.equals( "previous" ) )
		{
			rowIndex -= numberOfRows;
			pageIndex--;
		}
		else if( way.equals( "last" ) )
		{
			rowIndex = (int) ( numberOfPages -1 ) * numberOfRows;
			pageIndex = (int) numberOfPages;
		}
		else if( way.equals( "first" ) )
		{
			rowIndex = 0;
			pageIndex = 1;
		}
		
	}
	

    
	public void sorting(ActionEvent event)
    {
    	System.out.println( "event = " + event );
    	System.out.println( "id = " +  event.getComponent().getId() );
    	
    	String column = event.getComponent().getId();
    	
    	if( column.equals( lastColumn ))
    	{
    		ascending = !ascending;
    	}
    	else
    	{
    		ascending = true;
    	}
    	
    	lastColumn = column;
    	
    	setSort( column );
    	
    	sort( column, ascending );
    	
    }
	
	
	
	
	private void sort(final String column, final boolean ascending)
	{
		Comparator comparator = new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				Object c1 = (T) o1;
				Object c2 = (T) o2;
				if (column == null)
				{
					return 0;
				}
				
				String methodName = "get" + column.substring(0, 1).toUpperCase() + column.substring(1);
				try
				{
		            Method m = c1.getClass().getMethod(methodName, null);
		            Method m2 = c2.getClass().getMethod(methodName, null);
		            
		            String res1 = (String) m.invoke(c1);
		            
		            String res2 = (String) m2.invoke(c2);
		            
		            return ascending ? res1.compareTo(res2) : res2
							.compareTo(res1);
		            
					
		        } catch (IllegalAccessException illegalAccess)
		        {
		        	System.out.println(illegalAccess);
		        } catch (InvocationTargetException invocTarget)
		        {
		        	System.out.println(invocTarget);
		        } catch (Exception general)
		        {
		        	System.out.println(general);
		        }
				return 0;
			}
		};
		Collections.sort(list, comparator);
		
	}
	
	
	
	
	
	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}


	public List<T> getList() {
		System.out.println( "**henter liste......" );
		return list;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public long getNumberOfEntries() {
		return numberOfEntries;
	}

	public int getShowEnd() {
		if( (pageIndex+1) == numberOfRows)
		{
			return (int) numberOfEntries;
		}
		else
		{
			return pageIndex * rowIndex + numberOfRows;
		}
		
	}

	public int getShowStart() {
		return (pageIndex+1) * (rowIndex+1);
	}
	
	
	
	
	
}

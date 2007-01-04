/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.nav.presentation.pensjon.saksbehandling.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



/**
 * Convenient base class for sortable lists.
 * @author personc7e15cb5f7a6 (latest modification by $Author: matzew $)
 * @version $Revision: 167718 $ $Date: 2005-03-24 16:47:11 +0000 (to, 24 mar 2005) $
 */
public class PagedSortableList<T>
{
	
	private List<T> list = new ArrayList<T>();
    private String _sort;
    private boolean _ascending;

	
    public PagedSortableList(List<T> list, String defaultSortColumn)
    {
    	this.list = list;
        _sort = defaultSortColumn;
        _ascending = isDefaultAscending(defaultSortColumn);
    }


	protected boolean isDefaultAscending(String sortColumn)
	{
		return true;
	}

	protected void sort(final String column, final boolean ascending)
	{
		Comparator comparator = new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				Object c1 = o1;
				Object c2 = o2;
				
				String method = "get" + 
									column.substring(0, 1).toUpperCase() + 
									column.substring(1);
				
				try
				{
					Class[] o = new Class[0];
					Method m = c1.getClass().getMethod(method, o);
					Method m2 = c2.getClass().getMethod(method, o);
					String streng1 = (String) m.invoke(c1);
					String streng2 = (String) m2.invoke(c2);
					
					return ascending ? streng1.compareTo(streng2) : streng2
							.compareTo(streng1);
				}
				catch( NoSuchMethodException e )
				{
					System.out.println( "1 __-- " + e );
				}
				catch( InvocationTargetException e )
				{
					System.out.println( "2 __-- " + e );
				}
				catch( IllegalAccessException e )
				{
					System.out.println( "3 __-- " + e );
				}
				
				return 0;
			}
		};
		Collections.sort(list, comparator);
	}
	
	
	

    public void sort(String sortColumn)
    {
    	System.out.println( "sortColumn = " + sortColumn );
        if (sortColumn == null)
        {
            throw new IllegalArgumentException("Argument sortColumn must not be null.");
        }

        if (_sort.equals(sortColumn))
        {
            //current sort equals new sortColumn -> reverse sort order
            _ascending = !_ascending;
        }
        else
        {
            //sort new column in default direction
            _sort = sortColumn;
            _ascending = isDefaultAscending(_sort);
        }

        sort(_sort, _ascending);
    }

	public List<T> getList()
	{
		sort(getSort(), isAscending());
		return list;
	}

	public void setList(List<T> list)
	{
		this.list = list;
	}

    public String getSort()
    {
        return _sort;
    }

    public void setSort(String sort)
    {
        _sort = sort;
    }

    public boolean isAscending()
    {
        return _ascending;
    }

    public void setAscending(boolean ascending)
    {
        _ascending = ascending;
    }
}

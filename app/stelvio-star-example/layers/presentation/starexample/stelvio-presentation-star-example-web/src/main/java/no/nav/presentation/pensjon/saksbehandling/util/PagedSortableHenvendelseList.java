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

package no.nav.presentation.pensjon.saksbehandling.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.HenvendelserDO;


public class PagedSortableHenvendelseList extends SortableList
{
	private List<HenvendelserDO> henvendelser = new ArrayList<HenvendelserDO>();

	public PagedSortableHenvendelseList(List<HenvendelserDO> henvendelser)
	{
		super("fagomrode");
		this.henvendelser = henvendelser;
	}

	public List<HenvendelserDO> getHenvendelser()
	{
		sort(getSort(), isAscending());
		return henvendelser;
	}

	public void setHenvendelser(List<HenvendelserDO> henvendelser)
	{
		this.henvendelser = henvendelser;
	}

	protected boolean isDefaultAscending(String sortColumn)
	{
		return true;
	}

	public void sort(final String column, final boolean ascending)
	{
		Comparator comparator = new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				HenvendelserDO c1 = (HenvendelserDO) o1;
				HenvendelserDO c2 = (HenvendelserDO) o2;
				if (column == null)
				{
					return 0;
				}
				if (column.equals("type"))
				{
					return ascending ? c1.getType().compareTo(c2.getType()) : c2.getType()
									.compareTo(c1.getType());
				}
				else if (column.equals("fagomrode"))
				{
					return ascending ? c1.getFagomrode().compareTo(c2.getFagomrode()) : c2.getFagomrode()
									.compareTo(c1.getFagomrode());
				}
				else
					return 0;
			}
		};
		Collections.sort(henvendelser, comparator);
	}
	@Override
	public String getSort() {
		// TODO Auto-generated method stub
		return super.getSort();
	}
}

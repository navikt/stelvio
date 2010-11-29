/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package no.stelvio.presentation.jsf.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.StateHolder;

/**
 * This class is a copy of the HtmlDataTable class found in Tomahawk v 1.1.6 It is needed by
 * no.nav.presentation.pensjon.common.jsf.componentHtmlDataTableHack which has been applied a patch for the bug reported in
 * TOMAHAWK-961.
 * 
 * @author person6045563b8dec
 * 
 * 
 * @author person055361994206 (latest modification by $Author: grantsmith $)
 * @version $Revision: 472630 $ $Date: 2006-11-08 21:40:03 +0100 (Mi, 08 Nov 2006) $ %Log$
 */
class SerializableListDataModel extends SerializableDataModel {
	private static final long serialVersionUID = 2579712878688635627L;

	/**
	 * Creates a new instance of SerializableListDataModel.
	 *
	 * @param first first
	 * @param rows rows
	 * @param list list
	 */
	public SerializableListDataModel(int first, int rows, List list) {
		super.first = first;
		super.rows = rows;
		super.rowCount = list.size();
		if (super.rows <= 0) {
			super.rows = super.rowCount - first;
		}

		if (super.rows == super.rowCount) {
			// whole list must be saved
			if (list instanceof Serializable || list instanceof StateHolder) {
				super.list = list;
			} else {
				// copy list
				super.list = new ArrayList(list);
			}
		} else {
			int size = super.rows > 0 && super.rows < super.rowCount ? super.rows : super.rowCount;
			super.list = new ArrayList(size);
			if (size > super.rowCount - super.first) {
				size = super.rowCount - super.first;
			}
			for (int i = 0; i < size; i++) {
				super.list.add(list.get(super.first + i));
			}
		}
	}
}

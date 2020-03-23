package no.stelvio.presentation.util;

import java.io.Serializable;
import java.util.List;

import javax.faces.event.FacesEvent;

/**
 * This interface indicates requirements for drawing a standard h:dataTable with nav:pageingFooter for navigation.
 * 
 * @version $Id$
 * @param <T>
 *            The type for the list
 */
public interface PagedList<T> extends Serializable {

	/**
	 * Handles the paging of the list.
	 * 
	 * @param event
	 *            JSF FacesEvent
	 */
	void pageing(FacesEvent event);

	/**
	 * The list held by the object. This is input to h:dataTable value.
	 * 
	 * @return list the list held by the object
	 */
	List<T> getList();

	/**
	 * The number of rows to be shown on each page. This is input to h:dataTable rows. This number could be fixed or vary from
	 * page to page.
	 * 
	 * @return numberOfRowsOnEachPage the number of rows to be shown on each page
	 */
	int getNumberOfRowsOnEachPage();

	/**
	 * The index of the first row in the table. This is input to h:dataTable first.
	 * 
	 * @return rowIndex
	 */
	int getRowIndex();

	/**
	 * The number of elements in list.
	 * 
	 * @return numberOfItems the number of elements in list
	 */
	long getNumberOfItems();

	/**
	 * Index of the last of the shown elements on a page.
	 * 
	 * @return showEnd index of last shown element
	 */
	int getShowEnd();

	/**
	 * Index of currenly selected page.
	 * 
	 * @return pageIndex index of currenly selected page
	 */
	int getPageIndex();

	/**
	 * Number of pages the list is divided into.
	 * 
	 * @return numberOfPages number of pages
	 */
	long getNumberOfPages();

}
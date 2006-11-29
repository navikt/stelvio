package no.stelvio.web.taglib.breadcrumb;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class Breadcrumb implements Comparable {
	private int index;
	private String id;
	private String caption;
	private boolean hideCrumbDivider;
	
	/**
	 * TODO: Document me
	 * 
	 * @param id
	 * @param caption
	 */
	public Breadcrumb(int index, String id, String caption) {
		this.index = index;
		this.id = id;
		this.caption = caption;
	}
	
	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the hideCrumbDivider
	 */
	public boolean isHideCrumbDivider() {
		return hideCrumbDivider;
	}

	/**
	 * @param hideCrumbDivider the hideCrumbDivider to set
	 */
	public void setHideCrumbDivider(boolean lastCrumb) {
		this.hideCrumbDivider = lastCrumb;
	}
	
	/**
	 * @param arg0
	 * @return
	 */
	public int compareTo(Object object) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if (this.equals(object)) {
	    	return EQUAL;
	    }
	    
	    if (object instanceof Breadcrumb) {
	    	Breadcrumb compareCrumb = (Breadcrumb) object;
	    	if (compareCrumb.getIndex() < this.getIndex()) {
	    		return BEFORE;
	    	}
	    	else if (compareCrumb.getIndex() > this.getIndex()) {
	    		return AFTER;
	    	}
	    	else {
	    		return EQUAL;
	    	}
	    }
	    else {
	    	return AFTER;
	    }
	}
}
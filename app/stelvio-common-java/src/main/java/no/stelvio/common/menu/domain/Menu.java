package no.stelvio.common.menu.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Class represent a Menu.
 * Instances of this class are Java Persistence API Entities,
 * and may be persisted by an JPA EntityManager.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @todo should not be necessary with setters at all?
 */
@NamedQueries({
	@NamedQuery(name="Menu.findParents", query="SELECT m FROM Menu m WHERE m.parent is null")
})
@Entity(name="Menu")
@Table(name="MENU")
public class Menu implements Serializable {
	// TODO: Remove or create generated value
	private static final long serialVersionUID = 1L;

	/**
	 * The primary key.
	 */
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MENU_ID")
	private int menuId;
	
	/**
	 * Parent menu object, <code>null</code> if it's a root menuitem.
	 */
	@ManyToOne
	@JoinColumn(name="PARENT_ID")
	private Menu parent;
	
	/**
	 * List of child Menu objects.
	 */
	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
	private List<Menu> children;
	
	/**
	 * List of permissions this menu is a part of.
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="PERMISSION_MENU",
		joinColumns=@JoinColumn(name="MENU_ID"),
		inverseJoinColumns=@JoinColumn(name="PERMISSION_ID")
	)
	private List<Permission> permissions;
	
	/**
	 * List of screens this menu is a part of.
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SCREEN_MENU",
		joinColumns=@JoinColumn(name="MENU_ID"),
		inverseJoinColumns=@JoinColumn(name="SCREEN_ID")
	)
	private List<Screen> screens;
	
	/**
	 * In order to prevent "lost updates" all entities need to include a version field.
	 * This field is used by the persistence provider to implement optimistic locking.
	 */ 
	// There shall be no setter-method for the version-field.
	@Version
	@Column(name="VERSION")
	private long version;
	
	/**
	 * The name of the flow to execute when clicking the menuitem.
	 */
	@Column(name="FLOW_NAME")
	private String flowName;
	
	/**
	 * The sortingorder of the menuitem.
	 */
	@Column(name="SORTING")
	private int sorting;
	
	/**
	 * The text to display.
	 */
	@Column(name="LEAD_TEXT")
	private String leadtext;
	
	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	public Menu() {}
	
	/**
	 * @return the list of children Menu objects.
	 */
	public List<Menu> getChildren() {
		return children;
	}
	
	/**
	 * @param children the child Menu objects.
	 */
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	
	/**
	 * @return the flowName.
	 */
	public String getFlowName() {
		return flowName;
	}
	
	/**
	 * @param flowName the flowName to set.
	 */
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	
	/**
	 * @return the menuId.
	 */
	public int getMenuId() {
		return menuId;
	}
	
	/**
	 * @param menuId the menuId to set.
	 */
	public void setMenuId(int id) {
		this.menuId = id;
	}
	
	/**
	 * @return the leadtext.
	 */
	public String getLeadtext() {
		return leadtext;
	}
	
	/**
	 * @param leadtext the leadtext to set.
	 */
	public void setLeadtext(String leadtext) {
		this.leadtext = leadtext;
	}
	
	/**
	 * @return the parent Menu object.
	 */
	public Menu getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent Menu object to set.
	 */
	public void setParent(Menu parent_id) {
		this.parent = parent_id;
	}
	
	/**
	 * @return the sort order.
	 */
	public int getSorting() {
		return sorting;
	}
	
	/**
	 * @param sorting the sort order to set
	 */
	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	/**
	 * @return a list of Permission objects this menu is a part of.
	 */
	public List<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions sets the Permission objects this menu is a part of
	 */
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return a list of Screen objects this menu is a part of.
	 */
	public List<Screen> getScreens() {
		return screens;
	}

	/**
	 * @param screens sets the Screen objects this menu is a part of.
	 */
	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}

	/**
	 * In order to prevent "lost updates" all entities need to include a version field.
	 * This field is used by the persistence provider to implement optimistic locking.
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}
}
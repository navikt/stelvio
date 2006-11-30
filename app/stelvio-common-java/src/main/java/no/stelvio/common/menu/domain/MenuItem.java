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

/**
 * Class represent a MenuItem.
 * Instances of this class are Java Persistence API Entities,
 * and may be persisted by an JPA EntityManager.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
@NamedQueries({
	@NamedQuery(name="MenuItem.findParents", query="SELECT m FROM MenuItem m WHERE m.parent is null")
})
@Entity(name="MenuItem")
@Table(name="MENU_ITEM")
public class MenuItem implements Serializable {
	// TODO: Remove or create generated value
	private static final long serialVersionUID = 1L;

	/**
	 * The primary key.
	 */
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MENU_ITEM_ID")
	private int menuId;
	
	/**
	 * Parent menu object, <code>null</code> if it's a root menuitem.
	 */
	@ManyToOne
	@JoinColumn(name="PARENT_ID")
	private MenuItem parent;
	
	/**
	 * List of child MenuItem objects.
	 */
	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
	private List<MenuItem> children;
	
	/**
	 * List of menuItemPermissions this menu is a part of.
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="PERMISSION_MENU",
		joinColumns=@JoinColumn(name="MENU_ITEM_ID"),
		inverseJoinColumns=@JoinColumn(name="MENU_PERMISSION_ID")
	)
	private List<MenuItemPermission> menuItemPermissions;
	
	/**
	 * List of menuItemScreens this menu is a part of.
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SCREEN_MENU",
		joinColumns=@JoinColumn(name="MENU_ITEM_ID"),
		inverseJoinColumns=@JoinColumn(name="MENU_SCREEN_ID")
	)
	private List<MenuItemScreen> menuItemScreens;
	
	/**
	 * The name of the flow to execute when clicking the menuitem.
	 */
	@Column(name="FLOW_NAME")
	private String flowName;
	
	/**
	 * The sortingorder of the menuitem.
	 */
	@Column(name="ORDER")
	private int order;
	
	/**
	 * The text to display.
	 */
	@Column(name="LEAD_TEXT")
	private String leadtext;
	
	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	public MenuItem() {}
	
	/**
	 * @return the list of children MenuItem objects.
	 */
	public List<MenuItem> getChildren() {
		return children;
	}
	
	/**
	 * @param children the child MenuItem objects.
	 */
	public void setChildren(List<MenuItem> children) {
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
	 * @return the parent MenuItem object.
	 */
	public MenuItem getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent MenuItem object to set.
	 */
	public void setParent(MenuItem parent_id) {
		this.parent = parent_id;
	}
	
	/**
	 * @return the sort order.
	 */
	public int getOrder() {
		return order;
	}
	
	/**
	 * @param order the sort order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return a list of MenuItemPermission objects this menu is a part of.
	 */
	public List<MenuItemPermission> getPermissions() {
		return menuItemPermissions;
	}

	/**
	 * @param menuItemPermissions sets the MenuItemPermission objects this menu is a part of
	 */
	public void setPermissions(List<MenuItemPermission> menuItemPermissions) {
		this.menuItemPermissions = menuItemPermissions;
	}

	/**
	 * @return a list of MenuItemScreen objects this menu is a part of.
	 */
	public List<MenuItemScreen> getScreens() {
		return menuItemScreens;
	}

	/**
	 * @param menuItemScreens sets the MenuItemScreen objects this menu is a part of.
	 */
	public void setScreens(List<MenuItemScreen> menuItemScreens) {
		this.menuItemScreens = menuItemScreens;
	}
}
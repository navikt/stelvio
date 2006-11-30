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
 * Class represent a Menu.
 * Instances of this class are Java Persistence API Entities,
 * and may be persisted by an JPA EntityManager.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
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
	 * List of menuPermissions this menu is a part of.
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="PERMISSION_MENU",
		joinColumns=@JoinColumn(name="MENU_ID"),
		inverseJoinColumns=@JoinColumn(name="PERMISSION_ID")
	)
	private List<MenuPermission> menuPermissions;
	
	/**
	 * List of menuScreens this menu is a part of.
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SCREEN_MENU",
		joinColumns=@JoinColumn(name="MENU_ID"),
		inverseJoinColumns=@JoinColumn(name="SCREEN_ID")
	)
	private List<MenuScreen> menuScreens;
	
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
	 * @return a list of MenuPermission objects this menu is a part of.
	 */
	public List<MenuPermission> getPermissions() {
		return menuPermissions;
	}

	/**
	 * @param menuPermissions sets the MenuPermission objects this menu is a part of
	 */
	public void setPermissions(List<MenuPermission> menuPermissions) {
		this.menuPermissions = menuPermissions;
	}

	/**
	 * @return a list of MenuScreen objects this menu is a part of.
	 */
	public List<MenuScreen> getScreens() {
		return menuScreens;
	}

	/**
	 * @param menuScreens sets the MenuScreen objects this menu is a part of.
	 */
	public void setScreens(List<MenuScreen> menuScreens) {
		this.menuScreens = menuScreens;
	}
}
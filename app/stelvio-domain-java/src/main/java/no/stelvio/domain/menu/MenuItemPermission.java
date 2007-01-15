package no.stelvio.domain.menu;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Class represent a MenuItemPermission.
 * Instances of this class are Java Persistence API Entities,
 * and may be persisted by an JPA EntityManager.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @todo should not be necessary with setters at all?
 */
@Entity(name="MenuItemPermission")
@Table(name="MENU_PERMISSION")
public class MenuItemPermission implements Serializable {

	private static final long serialVersionUID = -7769356462360967819L;

	/**
	 * The primary key.
	 */
	@Column(name="MENU_PERMISSION_ID")
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int permissionId;
	
	/**
	 * A list of MenuItem objects that are a part of this permission.
	 */
	@ManyToMany(mappedBy="permissions")
	private List<MenuItem> menuItems;
	
	/**
	 * In order to prevent "lost updates" all entities need to include a version field.
	 * This field is used by the persistence provider to implement optimistic locking.
	 */
	// There shall be no setter-method for the version-field.
	@Column(name="VERSION")
	@Version
	private long version;
	
	/**
	 * The name of the role.
	 */
	@Column(name="ROLE")
	private String role;
	
	/**
	 * Specifies wether or not the permission has descretion.
	 */
	@Column(name="DISCRETION")
	private boolean discretion;
	
	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	public MenuItemPermission() {}
	
	/**
	 * @return the discretion.
	 */
	public boolean isDiscretion() {
		return discretion;
	}
	
	/**
	 * @param discretion the discretion to set.
	 */
	public void setDiscretion(boolean discretion) {
		this.discretion = discretion;
	}
	
	/**
	 * @return the name of the role.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * @param role the name of the role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the permissionId.
	 */
	public int getPermissionId() {
		return permissionId;
	}

	/**
	 * @param permissionId the permissionId to set.
	 */
	public void setPermissionId(int id) {
		this.permissionId = id;
	}

	/**
	 * In order to prevent "lost updates" all entities need to include a version field.
	 * This field is used by the persistence provider to implement optimistic locking.
	 * @return the version.
	 */ 
	public long getVersion() {
		return version;
	}

	/**
	 * @return a list of MenuItem object that are a part of this permission.
	 */
	public List<MenuItem> getMenus() {
		return menuItems;
	}

	/**
	 * @param menuItems the MenuItem objects that are a part of this permission.
	 */
	public void setMenus(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}
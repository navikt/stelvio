package no.stelvio.domain.menu;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Class represent a MenuItemPermission. Instances of this class are Java Persistence API Entities, and may be persisted by any
 * ORM product that support JPA Annotations table mapping.
 * 
 */
@Entity
@Table(name = "T_TILGANG")
public class MenuItemPermission implements Serializable {

	private static final long serialVersionUID = -7769356462360967819L;

	/**
	 * The primary key.
	 */
	@Column(name = "TILGANG_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long permissionId;

	/**
	 * A list of MenuItem objects that are a part of this permission.
	 */
	@ManyToMany(mappedBy = "menuItemPermissions")
	private Set<MenuItem> menuItems = new TreeSet<MenuItem>();

	/**
	 * The name of the role.
	 */
	@Column(name = "ROLLE")
	private String role;

	/**
	 * Specifies wether or not the permission has descretion.
	 */
	@Column(name = "DISKRESJON")
	private boolean discretion;

	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	protected MenuItemPermission() {
	}

	/**
	 * Constructs a new MenuItemPermission.
	 * 
	 * @param role
	 *            the role with access to this menu item
	 * @param isDiscretion
	 *            <true> if discretion applies to this item
	 */
	public MenuItemPermission(String role, boolean isDiscretion) {
		this.role = role;
		this.discretion = isDiscretion;
	}

	/**
	 * Gets the boolean value of discretion.
	 * 
	 * @return discretion <code>true</code> if menu item is subject to discretion, otherwise <code>false</code>
	 */
	public boolean isDiscretion() {
		return discretion;
	}

	/**
	 * Sets the discretion.
	 * 
	 * @param discretion
	 *            <code>true</code> if discretion applies, otherwise <code>false</code>
	 */
	public void setDiscretion(boolean discretion) {
		this.discretion = discretion;
	}

	/**
	 * Gets the role.
	 * 
	 * @return the name of the role.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the name of the role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the permission id.
	 * 
	 * @return the permissionId.
	 */
	public long getPermissionId() {
		return permissionId;
	}

	/**
	 * Sets the permission id.
	 * 
	 * @param permissionId
	 *            the permissionId to set.
	 */
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	/**
	 * Gets a list of menu items.
	 * 
	 * @return a list of MenuItem object that are a part of this permission.
	 */
	public Set<MenuItem> getMenus() {
		return menuItems;
	}

	/**
	 * Sets a list of menu items.
	 * 
	 * @param menuItems
	 *            list of MenuItem objects that are a part of this permission.
	 */
	public void setMenus(Set<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}
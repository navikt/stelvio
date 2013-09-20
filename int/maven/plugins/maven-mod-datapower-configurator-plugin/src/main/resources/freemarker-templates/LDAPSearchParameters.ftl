<#macro LDAPSearchParameters name ldapBaseDN ldapReturnedAttribute ldapFilterPrefix ldapScope>
	<LDAPSearchParameters name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<LDAPBaseDN>${ldapBaseDN}</LDAPBaseDN>
		<LDAPReturnedAttribute>${ldapReturnedAttribute}</LDAPReturnedAttribute>
		<LDAPFilterPrefix>${ldapFilterPrefix}</LDAPFilterPrefix>
		<LDAPScope>${ldapScope}</LDAPScope>
	</LDAPSearchParameters>
</#macro>
package no.stelvio.common.security.support;

import no.stelvio.common.security.RoleName;

public enum RolesEnum implements RoleName {
	
	VEILEDER,
	UTVIDET,
	KODE_6,
	KODE_7,
	SPESIAL;
	public boolean isValid(){
		return true;
	}
}

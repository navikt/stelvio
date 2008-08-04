package no.nav.datapower.deployer;

import java.util.List;

import no.nav.datapower.util.DPCollectionUtils;


public class CompoundDeploymentUnit implements DeploymentUnit {

	private List<DeploymentUnit> units;

	public void addUnit(DeploymentUnit unit) {
		if (units == null)
			units = DPCollectionUtils.newArrayList();
		units.add(unit);
	}
	
	public void deploy() {
		// TODO Auto-generated method stub

	}
}

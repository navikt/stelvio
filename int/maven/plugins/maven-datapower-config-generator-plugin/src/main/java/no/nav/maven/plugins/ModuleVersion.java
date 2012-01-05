package no.nav.maven.plugins;

public class ModuleVersion {

	private String variableName;
	private String exposedAs;
	
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getExposedAs() {
		return exposedAs;
	}
	public void setExposedAs(String exposedAs) {
		this.exposedAs = exposedAs;
	}

	public String toString(){
		return variableName + ":" + exposedAs;
	}
}

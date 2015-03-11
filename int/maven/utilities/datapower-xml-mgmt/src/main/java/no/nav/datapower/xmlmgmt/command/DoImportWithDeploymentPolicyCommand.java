package no.nav.datapower.xmlmgmt.command;

import no.nav.datapower.xmlmgmt.ImportFormat;

;

public class DoImportWithDeploymentPolicyCommand implements XMLMgmtCommand {

	private final ImportFormat sourceType;
	private final boolean dryRun;
	private final boolean overwriteFiles;
	private final boolean overwriteObjects;
	private final String configData;
    private final String deploymentPolicyName;

	private DoImportWithDeploymentPolicyCommand(Builder builder) {
		this.sourceType = builder.sourceType;
		this.configData = builder.configData;
		this.dryRun = builder.dryRun;
		this.overwriteFiles = builder.overwriteFiles;
        this.overwriteObjects = builder.overwriteObjects;
        this.deploymentPolicyName = builder.deploymentPolicyName;
	}
	
	public static class Builder {
        private ImportFormat sourceType;
		private boolean dryRun;
		private boolean overwriteFiles;
		private boolean overwriteObjects;
		private String configData;
        private final String deploymentPolicyName;
		
		public Builder(ImportFormat sourceType, String configData, String deploymentPolicyName) {
			this.sourceType = sourceType;
			this.configData = configData;
            this.deploymentPolicyName = deploymentPolicyName;
			dryRun = false;
			overwriteFiles = true;
			overwriteObjects = true;
		}
		public Builder dryRyn(boolean dryRun) { this.dryRun = dryRun; return this; }
		public Builder overwriteFiles(boolean overwriteFiles) { this.overwriteFiles = overwriteFiles; return this; }
		public Builder overwriteObjects(boolean overwriteObjects) { this.overwriteObjects = overwriteObjects; return this; }
		public DoImportWithDeploymentPolicyCommand build() { return new DoImportWithDeploymentPolicyCommand(this); }
	}	
	
	public String format() {

        /*<man:do-import source-type="?" dry-run="?" overwrite-files="?" overwrite-objects="?">
           <man:input-file>?</man:input-file>
           <!--Zero or more repetitions:-->
           <man:object class="?" name="?" overwrite="?" import-debug="?"/>
           <!--Zero or more repetitions:-->
           <man:file name="?" overwrite="?"/>
        </man:do-import>*/
		StringBuffer req = new StringBuffer();
		req.append("<dp:do-import source-type='");
		req.append(sourceType);
        req.append("' deployment-policy='");
        req.append(deploymentPolicyName);
        req.append("' dry-run='");
        req.append(dryRun);
		req.append("' overwrite-files='");
		req.append(overwriteFiles);
		req.append("' overwrite-objects='");
		req.append(overwriteObjects);
		req.append("'>\r\n");
		req.append("<dp:input-file>");
		req.append(configData);
		req.append("</dp:input-file>\r\n");
		req.append("</dp:do-import>\r\n");
		return req.toString();
	}

}

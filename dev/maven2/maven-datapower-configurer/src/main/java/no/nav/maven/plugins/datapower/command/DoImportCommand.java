package no.nav.maven.plugins.datapower.command;

import no.nav.maven.plugins.datapower.config.*;
;

public class DoImportCommand implements XMLMgmtCommand {

	private final ImportFormat sourceType;
	private final boolean dryRun;
	private final boolean overwriteFiles;
	private final boolean overwriteObjects;
	private final String configData;
	
	private DoImportCommand(Builder builder) {
		this.sourceType = builder.sourceType;
		this.configData = builder.configData;
		this.dryRun = builder.dryRun;
		this.overwriteFiles = builder.overwriteFiles;
		this.overwriteObjects = builder.overwriteObjects;
	}
	
	public static class Builder {
		private ImportFormat sourceType;
		private boolean dryRun;
		private boolean overwriteFiles;
		private boolean overwriteObjects;
		private String configData;
		
		public Builder(ImportFormat sourceType, String configData) {
			this.sourceType = sourceType;
			this.configData = configData;
			dryRun = false;
			overwriteFiles = true;
			overwriteObjects = true;
		}
		public Builder dryRyn(boolean dryRun) { this.dryRun = dryRun; return this; }
		public Builder overwriteFiles(boolean overwriteFiles) { this.overwriteFiles = overwriteFiles; return this; }
		public Builder overwriteObjects(boolean overwriteObjects) { this.overwriteObjects = overwriteObjects; return this; }
		public DoImportCommand build() { return new DoImportCommand(this); }
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

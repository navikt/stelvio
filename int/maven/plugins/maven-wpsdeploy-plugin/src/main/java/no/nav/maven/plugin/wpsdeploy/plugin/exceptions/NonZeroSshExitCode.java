package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class NonZeroSshExitCode extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public Integer getExitCode() {
		return exitCode;
	}

	public void setExitCode(Integer exitCode) {
		this.exitCode = exitCode;
	}

	private Integer exitCode;

	public NonZeroSshExitCode() {
		super();
	}

	public NonZeroSshExitCode(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NonZeroSshExitCode(String arg0) {
		super(arg0);
	}

	public NonZeroSshExitCode(Throwable arg0) {
		super(arg0);
	}


}

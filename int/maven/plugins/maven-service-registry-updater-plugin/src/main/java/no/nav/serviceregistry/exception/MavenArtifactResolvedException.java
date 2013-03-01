package no.nav.serviceregistry.exception;

public class MavenArtifactResolvedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MavenArtifactResolvedException() {}

	public MavenArtifactResolvedException(String paramString) {
		super(paramString);
	}

	public MavenArtifactResolvedException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public MavenArtifactResolvedException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

}

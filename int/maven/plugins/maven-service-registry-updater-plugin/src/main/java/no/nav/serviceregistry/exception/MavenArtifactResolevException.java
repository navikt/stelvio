package no.nav.serviceregistry.exception;

public class MavenArtifactResolevException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MavenArtifactResolevException() {}

	public MavenArtifactResolevException(String paramString) {
		super(paramString);
	}

	public MavenArtifactResolevException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public MavenArtifactResolevException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

}

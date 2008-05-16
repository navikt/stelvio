package no.stelvio.dto.exception.mapping;

public class MappingDtoException extends AbstractMappingDtoException {

	private static final long serialVersionUID = -1855978243125358226L;

	public MappingDtoException(String fromClass, String toClass) {
		super(new String[]{fromClass, toClass});
	}

	public MappingDtoException(Throwable cause, String fromClass, String toClass) {
		super(cause, new String[]{fromClass, toClass});
	}
	
	public String messageTemplate(){
		return "Exception occured while mapping from {0} to {1}";
	}

}

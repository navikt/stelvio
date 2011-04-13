package ejbs;

import java.io.Serializable;

public class LGResponse implements Serializable {

	private static final long serialVersionUID = 471528611045869006L;

	private String output;

	public LGResponse() {
		super();
	}

	public LGResponse(String output) {
		this();
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}

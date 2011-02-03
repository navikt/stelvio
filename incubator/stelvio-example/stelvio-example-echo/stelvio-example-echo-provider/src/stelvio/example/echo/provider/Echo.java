package stelvio.example.echo.provider;

import javax.jws.WebService;

@WebService
public class Echo {

	public String echo(String input) {
		return input;
	}
}

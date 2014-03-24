package no.stelvio.consumer.virgo;

public class SpringVirgoConsumer implements VirgoConsumer {

	private VirgoConsumer virgoService;
	
	public void setVirgoService(VirgoConsumer service) {
		this.virgoService = service;
	}

	@Override
	public String echo(String echoRequest) {
		// TODO Auto-generated method stub
		return virgoService.echo(echoRequest);
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub

	}

}

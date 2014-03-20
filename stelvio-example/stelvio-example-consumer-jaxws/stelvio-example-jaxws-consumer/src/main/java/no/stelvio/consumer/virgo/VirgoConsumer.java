package no.stelvio.consumer.virgo;

/**
 * Interface for VirgoConsumer
 */
public interface VirgoConsumer {
    String echo(String echoRequest);
    void ping();
}

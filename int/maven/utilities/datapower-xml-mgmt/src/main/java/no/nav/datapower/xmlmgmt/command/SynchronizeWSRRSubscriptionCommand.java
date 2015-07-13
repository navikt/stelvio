package no.nav.datapower.xmlmgmt.command;

public class SynchronizeWSRRSubscriptionCommand extends AbstractDoActionCommand{

    
    private String subscription;
    
    
    public SynchronizeWSRRSubscriptionCommand(String subscription) {
        this.subscription = subscription;
    }
    @Override
    protected void addCommandBody(StringBuffer builder) {
        builder.append("<WsrrSynchronize>\r\n");
        builder.append("<WSRRSubscription>");
        builder.append(subscription);
        builder.append("</WSRRSubscription>\r\n");
        builder.append("</WsrrSynchronize>\r\n");
    }

}

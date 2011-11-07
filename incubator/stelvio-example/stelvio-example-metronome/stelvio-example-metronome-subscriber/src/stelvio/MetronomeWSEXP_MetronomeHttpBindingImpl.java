package stelvio;

import javax.xml.datatype.XMLGregorianCalendar;


@javax.jws.WebService (endpointInterface="stelvio.Metronome", targetNamespace="http://stelvio", serviceName="Metronome", portName="MetronomeWSEXP_MetronomeHttpPort")
public class MetronomeWSEXP_MetronomeHttpBindingImpl{

    public void tick(XMLGregorianCalendar time) {
    	System.out.println("Tick: " + time);
    }

}
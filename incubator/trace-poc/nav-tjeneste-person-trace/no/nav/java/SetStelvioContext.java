package no.nav.java;

import no.stelvio.common.bus.util.StelvioContextHelper;

public class SetStelvioContext {
	
	public static String setKilde() {
	    //StelvioContextHelper stelvioCtx = new StelvioContextHelper();
	    //return stelvioCtx.getApplicationId();
	    return "PP01";
	}

	public static String setBrukerID() {
	    StelvioContextHelper stelvioCtx = new StelvioContextHelper();
	    return stelvioCtx.getNavUserId();
	}
}

package no.nav.maven.plugin.confluence.util;

import java.util.HashMap;
import java.util.Map;

public interface Constants {

	/* This is so sad, I have tears in my eyes */
	public static final Map<String, String> ENVMAPPING = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("S2", "Sandbox 2");
			put("S3", "Sandbox 3");
			put("S4", "Sandbox 4");
			put("S6", "Sandbox 6");
			put("S7", "Sandbox 7");
			put("S8", "Sandbox 8");
			put("K1", "Komponenttest 1");
			put("K2", "Komponenttest 2");
			put("K3", "Komponenttest 3");
			put("K4", "Komponenttest 4");
			put("K5", "Komponenttest 5");
			put("K6", "Komponenttest 6");
			put("K7", "Komponenttest 7");
			put("K10", "Komponenttest 10");
			put("I1", "Integrasjonstest 1");
			put("I2", "Integrasjonstest 2");
			put("I3", "Integrasjonstest 3");
			put("T1", "Systemtest 1");
			put("T2", "Systemtest 2");
			put("T3", "Systemtest 3");
			put("T4", "Systemtest 4");
			put("T5", "Systemtest 5");
			put("T6", "Systemtest 6");
			put("T7", "Systemtest 7");
			put("T8", "Systemtest 8");
			put("T9", "Systemtest 9");
			put("T10", "Systemtest 10");
			put("T11", "Systemtest 11");
			put("T12", "Systemtest 12");
			put("T13", "Systemtest 13");
			put("Q1", "Q1");
			put("Q2", "Q2");
			put("Q3", "Q3");
			put("Q5", "Q5");
			put("Q6", "Q6");
			put("P", "Produksjon");
		}
	};
}

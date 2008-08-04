package no.nav.datapower.deployer;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;

import no.nav.datapower.util.DPPropertiesUtils;


public abstract class Config {

	public static enum Key {
		MANDATORY, OPTIONAL
	}
	
	public static enum Node {
		ELEMENT, ATTRIBUTE
	}
	
	public static enum Property {
		TEMPLATE_CONFIG("template.config", Node.ELEMENT, Key.MANDATORY),
		TEMPLATE_AAAINFO("template.aaainfo", Node.ELEMENT, Key.MANDATORY),
		
		CFG_DOMAIN("configuration@domain", Node.ATTRIBUTE, Key.MANDATORY),
		
		/****** AAAPolicy variables ******/
		// AAAPolicy#Authenticate step. LTPA keys to verify inbound LTPAToken
		CFG_AAA_AU_LTPAKEY("configuration.AAAPolicy.Authenticate.AULTPAKeyFile", Node.ELEMENT, Key.MANDATORY),
		CFG_AAA_AU_LTPAPWD("configuration.AAAPolicy.Authenticate.AULTPAKeyFilePassword", Node.ELEMENT, Key.MANDATORY),

		// AAAPolicy#MapCredentials step. Mapping file
		CFG_AAA_MC_MAPURL("configuration.AAAPolicy.MapCredentials.MCMapURL", Node.ELEMENT, Key.MANDATORY),	

		// AAAPolicy#Authorize step. Authorization file.
		CFG_AAA_AZ_MAPURL("configuration.AAAPolicy.Authorize.AZMapURL", Node.ELEMENT,Key.MANDATORY),

		// AAAPolicy#PostProcess step. LTPA keys to generate outbound LTPAToken
		CFG_AAA_PP_LTPAKEY("configuration.AAAPolicy.PostProcess.PPLTPAKeyFile", Node.ELEMENT, Key.MANDATORY),
		CFG_AAA_PP_LTPAPWD("configuration.AAAPolicy.PostProcess.PPLTPAKeyFilePassword", Node.ELEMENT, Key.MANDATORY),

		// Crypto Certificate
		CFG_CERT_NAME("configuration.CryptoCertificate@name", Node.ATTRIBUTE, Key.MANDATORY),
		CFG_CERT_FILENAME("configuration.CryptoCertificate.Filename", Node.ATTRIBUTE, Key.MANDATORY),

		// HTTPS Protocol Handler
		CFG_HTTPS_ADDRESS("configuration.HTTPSSourceProtocolHandler.LocalAddress", Node.ELEMENT, Key.MANDATORY),
		CFG_HTTPS_PORT("configuration.HTTPSSourceProtocolHandler.LocalPort", Node.ELEMENT, Key.MANDATORY),

		// CryptIdentCred
		CFG_CRYPTOID_KEY("configuration.CryptoIdentCred.Key", Node.ELEMENT, Key.MANDATORY),
		CFG_CRYPTOID_CERT("configuration.CryptoIdentCred.Certificate", Node.ELEMENT, Key.MANDATORY),

		// CryptoKey
		CFG_CRYPTOKEY_NAME("configuration.CryptoKey@name", Node.ATTRIBUTE, Key.MANDATORY),
		CFG_CRYPTOKEY_FILENAME("configuration.CryptoKey.Filename", Node.ELEMENT, Key.MANDATORY),

		// WSEndpointRemoteRewriteRule
		CFG_WSENDRRR_PROTOCOL("configuration.WSEndpointRemoteRewriteRule.RemoteEndpointProtocol", Node.ELEMENT, Key.MANDATORY),
		CFG_WSENDRRR_HOST("configuration.WSEndpointRemoteRewriteRule.RemoteEndpointHostname", Node.ELEMENT, Key.MANDATORY),
		CFG_WSENDRRR_PORT("configuration.WSEndpointRemoteRewriteRule.RemoteEndpointPort", Node.ELEMENT, Key.MANDATORY),

		CFG_WSGW_USERSUMMARY("configuration.WSGateway.UserSummary", Node.ELEMENT, Key.MANDATORY),
		
		/****** AAAInfo variables ******/
		AAA_FILENAME("AAAInfo.Filename", Node.ELEMENT, Key.MANDATORY),
		AAA_AU_DN("AAAInfo.Authenticate.DN", Node.ELEMENT, Key.MANDATORY),
		AAA_MC_INCRED("AAAInfo.MapCredentials.InputCredential", Node.ELEMENT, Key.MANDATORY),
		AAA_MC_OUTCRED("AAAInfo.MapCredentials.OutputCredential", Node.ELEMENT, Key.MANDATORY),
		AAA_AZ_INCRED("AAAInfo.Authorize.InputCredential", Node.ELEMENT, Key.MANDATORY);

		
		private String name;
		private Node nodeType;
		private Key requirement;
				
		Property(String name, Node nodeType, Key requirement) {
			this.name = name;
			this.nodeType = nodeType;
			this.requirement = requirement;
		}
				
		@Override
		public String toString() {
			return name;
		}
		
		public boolean isMandatory() {
			return requirement.equals(Key.MANDATORY);
		}
		
		public Node getType() {
			return nodeType;
		}
		
		public boolean isElement() {
			return nodeType.equals(Node.ELEMENT);
		}
		
		public boolean isAttribute() {
			return nodeType.equals(Node.ATTRIBUTE);
		}
		
		public boolean isOptional() {
			return requirement.equals(Key.OPTIONAL);
		}
		
		public static Property fromString(String name) {
			Property[] keys = Property.values();
			for (Property key : keys) {
				if (name.equals(key.name))
					return key;
			}
			throw new IllegalArgumentException("Unknown key name '" + name + "'");
		}
		
		public static boolean isValidKey(String name) {
			try {
				Property.fromString(name);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
	}
	
	public static class Parameters extends Properties {

		private static final long serialVersionUID = -5120228634966744051L;

		public String getProperty(Property key) {
			return getProperty(key.toString());
		}

		@Override
		public Object put(Object key, Object value) {
			return super.put(Property.fromString((String)key).toString(), (String)value);
		}
		
		@Override
		public Object setProperty(String key, String value) {
			return setProperty(Property.fromString(key), value);
		}

		public Object setProperty(Property key, String value) {
			return super.setProperty(key.toString(), value);
		}
		
		

		public boolean containsKey(Property key) {
			return containsKey(key.toString());
		}
		
		public Set<Property> getMissingProperties() {
			Set<Property> missingProperties = EnumSet.noneOf(Property.class);
			Property[] keys = Property.values();
			for (Property key : keys) {			
				if (!containsKey(key) && key.isMandatory())
					missingProperties.add(key);
			}		
			return missingProperties;
		}

		@Override
		public void load(InputStream inStream) throws IOException {
			Properties props = new Properties();
			props.load(inStream);
			props = DPPropertiesUtils.stripWhiteSpaces(props);
			super.putAll(props);
		}

		public String getMissingPropertiesMessage() {
			StringBuffer builder = new StringBuffer();
			builder.append("\r\n");
			for (Property key : getMissingProperties()) {
				builder.append("Missing property '" + key.toString() + "'\r\n");
			}
			builder.append("\r\n");
			return builder.toString();
		}
	}
}

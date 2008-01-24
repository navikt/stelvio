package no.nav.maven.plugins;

import java.io.ByteArrayInputStream;

public class Base64Codec extends java.util.prefs.AbstractPreferences {
	//keep the key state
	private java.util.Hashtable encodedStore = new java.util.Hashtable();

	/** Creates a new instance of Base64Codec*/
	public Base64Codec(java.util.prefs.AbstractPreferences prefs, java.lang.String string) {
		super(prefs, string);
	}
	//main acts as a driver i.e. used to test this class not required by this class
	public static void main(java.lang.String[] args ){
		java.util.prefs.AbstractPreferences myprefs = new no.nav.maven.plugins.Base64Codec(null, "");

		java.lang.String stringToEncode = "Aladdin:open sesame";
		java.lang.String key = "Aladdin:open sesame";
		java.lang.String key_ = "KEYisNOTtheSAMEasTHEstring";
		try{
			java.lang.String encoded = ((no.nav.maven.plugins.Base64Codec)myprefs).encodeBase64(stringToEncode);
			java.lang.System.out.println("ENCODED STRING TO: " + encoded);
			java.lang.String base64 = (java.lang.String)((no.nav.maven.plugins.Base64Codec)myprefs).encodedStore.get(key);
			java.lang.String decoded = ((no.nav.maven.plugins.Base64Codec)myprefs).decodeBase64("newkey",base64);
			java.lang.System.out.println("DECODED STRING TO: " + decoded);

			java.lang.String encoded_ = ((no.nav.maven.plugins.Base64Codec)myprefs).encodeBase64(key_,"ALONGSTRANGESTRINGTHATMAKESNOSENCEATALLBUTCANBEENCODEDANYWAYREGARDLESS");
			java.lang.System.out.println("ENCODED STRING TO: " + encoded_);
			java.lang.String base64_ = (java.lang.String)((no.nav.maven.plugins.Base64Codec)myprefs).encodedStore.get(key_);
			java.lang.String decoded_ = ((no.nav.maven.plugins.Base64Codec)myprefs).decodeBase64(key_,base64_);
			java.lang.System.out.println("DECODED STRING TO: " + decoded_);
			java.lang.System.out.println("\n");
			java.util.Enumeration enum = ((no.nav.maven.plugins.Base64Codec)myprefs).encodedStore.keys();
			java.lang.String enumKey = null;
			while(enum.hasMoreElements()){
				enumKey = (java.lang.String)enum.nextElement();
				java.lang.System.out.print(enumKey);
				java.lang.System.out.println(" : " +((no.nav.maven.plugins.Base64Codec)myprefs).encodedStore.get(enumKey).toString());
			}
		}catch(java.io.UnsupportedEncodingException uee){
			uee.printStackTrace();
		}catch(java.io.IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public java.lang.String encodeBase64(java.lang.String key, java.lang.String raw)throws java.io.UnsupportedEncodingException{
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		java.io.PrintWriter pw = new java.io.PrintWriter(
		new java.io.OutputStreamWriter(baos,"UTF8"));
		pw.write(raw.toCharArray());
		pw.flush();//ya know
		byte[] rawUTF8 = baos.toByteArray();
		this.putByteArray(key, rawUTF8);

		return (java.lang.String)this.encodedStore.get(key);
	}
	
	public java.lang.String encodeBase64(java.lang.String raw)throws java.io.UnsupportedEncodingException{

		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		java.io.PrintWriter pw = new java.io.PrintWriter(
		new java.io.OutputStreamWriter(baos,"UTF8"));
		pw.write(raw.toCharArray());
		pw.flush();//ya know
		byte[] rawUTF8 = baos.toByteArray();
		this.putByteArray(raw, rawUTF8);

		return (java.lang.String)this.encodedStore.get(raw);
	}
	
	public java.lang.String decodeBase64(java.lang.String key, java.lang.String base64String)
	throws java.io.UnsupportedEncodingException, java.io.IOException{
		byte[] def = {(byte)'D',(byte)'E',(byte)'F'};//not used at any point
		this.encodedStore.put(key,base64String);
		char[] platformChars = null;
		byte[] byteResults = null;
		byteResults = this.getByteArray(key, def);
		java.io.InputStreamReader isr = new java.io.InputStreamReader(
		new java.io.ByteArrayInputStream(byteResults),"UTF8");
		platformChars = new char[byteResults.length];
		isr.read(platformChars);

		return new java.lang.String(platformChars);
	}
	
	//intercept key lookup and return our own base64 encoded string to super
	public String get(String key, String def) {
		return (java.lang.String)this.encodedStore.get(key);
	}
	
	//intercepts put captures the base64 encoded string and returns it
	public void put(String key, String value){
		this.encodedStore.put(key, value);//save the encoded string
	}
	
	//dummy implementation as AbstractPreferences is extended to get acces to protected
	//methods and to overide put(String,String) and get(String,String)
	protected java.util.prefs.AbstractPreferences childSpi(String name) {return null;}
	protected String[] childrenNamesSpi() throws java.util.prefs.BackingStoreException {return null;}
	protected void flushSpi() throws java.util.prefs.BackingStoreException {}
	protected String getSpi(String key) {return null;}
	protected String[] keysSpi() throws java.util.prefs.BackingStoreException {return null;}
	protected void putSpi(String key, String value) {}
	protected void removeNodeSpi() throws java.util.prefs.BackingStoreException {}
	protected void removeSpi(String key) {}
	protected void syncSpi() throws java.util.prefs.BackingStoreException {}
} 
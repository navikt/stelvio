<#-- 
    Incoming ldap object is supposed to have following fields:
    * host
    * port
    * dnprefix
    * dnsuffix
 -->
<#macro AAAPolicyUsernameToken2NoneLDAPS name aaaFileName ldap ldapSSLProfile>
	<AAAPolicy name="${name}" xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:dp="http://www.datapower.com/schemas/management">
		<mAdminState>enabled</mAdminState>
		<ExtractIdentity>
			<EIBitmap>
				<http-basic-auth>off</http-basic-auth>
				<wssec-username>on</wssec-username>
				<wssec-derived-key>off</wssec-derived-key>
				<wssec-binary-token>off</wssec-binary-token>
				<ws-secure-conversation>off</ws-secure-conversation>
				<ws-trust>off</ws-trust>
				<kerberos>off</kerberos>
				<kerberos-spnego>off</kerberos-spnego>
				<client-ssl>off</client-ssl>
				<saml-attr-name>off</saml-attr-name>
				<saml-authen-name>off</saml-authen-name>
				<saml-artifact>off</saml-artifact>
				<client-ip-address>off</client-ip-address>
				<signer-dn>off</signer-dn>
				<token>off</token>
				<cookie-token>off</cookie-token>
				<ltpa>off</ltpa>
				<metadata>off</metadata>
				<custom>off</custom>
			</EIBitmap>
			<EICustomURL/>
			<EIXPath/>
			<EISignerDNValcred/>
			<EICookieName/>
			<EIBasicAuthRealm>login</EIBasicAuthRealm>
			<EIUseWSSec>off</EIUseWSSec>
			<EIMetadata/>
			<EIAllowRemoteTokenReference>off</EIAllowRemoteTokenReference>
			<EIRemoteTokenProcessService/>
			<EIPasswordRetrievalMechanism>xmlfile</EIPasswordRetrievalMechanism>
			<EIPasswordRetrievalCustomURL/>
			<EIPasswordRetrievalAAAInfoURL/>
			<EISSLProxyProfile/>
		</ExtractIdentity>
		        <Authenticate>
            <AUMethod>ldap</AUMethod>
            <AUCustomURL/>
            <AUMapURL/>
            <AUHost>${ldap.host}</AUHost>
            <AUPort>${ldap.port}</AUPort>
            <AUSSLValcred/>
            <AUCacheAllow>absolute</AUCacheAllow>
            <AUCacheTTL>3</AUCacheTTL>
            <AUKerberosPrincipal/>
            <AUKerberosPassword/>
            <AUClearTrustServerURL/>
            <AUClearTrustApplication/>
            <AUSAMLArtifactResponder/>
            <AUKerberosVerifySignature>on</AUKerberosVerifySignature>
            <AUNetegrityBaseURI/>
            <AUSAMLAuthQueryServer/>
            <AUSAMLVersion>1.1</AUSAMLVersion>
            <AULDAPPrefix>${ldap.dnprefix}</AULDAPPrefix>
			<AULDAPSuffix>${ldap.dnsuffix}</AULDAPSuffix>
            <AULDAPLoadBalanceGroup/>
            <AUKerberosKeytab/>
            <AUWSTrustURL/>
            <AUSAML2Issuer/>
            <AUSignerValcred/>
            <AUSignedXPath/>
            <AUSSLProxyProfile class="SSLProxyProfile">${ldapSSLProfile}</AUSSLProxyProfile>
            <AUNetegrityConfig/>
            <AULDAPBindDN/>
            <AULDAPBindPassword/>
            <AULDAPSearchAttribute>userPassword</AULDAPSearchAttribute>
            <AULTPATokenVersionsBitmap>
                <LTPA>off</LTPA>
                <LTPA2>on</LTPA2>
                <LTPADomino>off</LTPADomino>
            </AULTPATokenVersionsBitmap>
            <AULTPAKeyFile/>
            <AULTPAKeyFilePassword/>
            <AULTPAStashFile/>
            <AUBinaryTokenX509Valcred/>
            <AUTAMServer/>
            <AUAllowRemoteTokenReference>off</AUAllowRemoteTokenReference>
            <AURemoteTokenProcessService/>
            <AUWSTrustVersion>1.2</AUWSTrustVersion>
            <AULDAPSearchForDN>off</AULDAPSearchForDN>
			<AULDAPSearchParameters/>
            <AUWSTrustRequireClientEntropy>off</AUWSTrustRequireClientEntropy>
            <AUWSTrustClientEntropySize>32</AUWSTrustClientEntropySize>
            <AUWSTrustRequireServerEntropy>off</AUWSTrustRequireServerEntropy>
            <AUWSTrustServerEntropySize>32</AUWSTrustServerEntropySize>
            <AUWSTrustRequireRSTC>off</AUWSTrustRequireRSTC>
            <AUWSTrustRequireAppliesToHeader>off</AUWSTrustRequireAppliesToHeader>
            <AUWSTrustAppliesToHeader/>
            <AUWSTrustEncryptionCertificate/>
            <AUZOSNSSConfig/>
            <AULDAPAttributes/>
            <AUSkewTime>0</AUSkewTime>
            <AUTAMPACReturn>off</AUTAMPACReturn>
        </Authenticate>
		<MapCredentials>
			<MCMethod>none</MCMethod>
			<MCCustomURL/>
			<MCMapURL/>
			<MCMapXPath/>
			<MCTFIMEndpoint/>
		</MapCredentials>
		<ExtractResource>
			<ERBitmap>
				<target-url>off</target-url>
				<original-url>on</original-url>
				<request-uri>off</request-uri>
				<request-opname>off</request-opname>
				<http-method>off</http-method>
				<XPath>off</XPath>
				<metadata>off</metadata>
			</ERBitmap>
			<ERXPath/>
			<ERMetadata/>
		</ExtractResource>
		<MapResource>
			<MRMethod>none</MRMethod>
			<MRCustomURL/>
			<MRMapURL/>
			<MRMapXPath/>
			<MRTAMMap>WebSEAL</MRTAMMap>
			<MRTAMInstancePrefix/>
			<MRTAMWebSEALDynURLFile/>
		</MapResource>
		<Authorize>
			<AZMethod>xmlfile</AZMethod>
			<AZCustomURL/>
			<AZMapURL>${aaaFileName}</AZMapURL>
			<AZHost/>
			<AZPort/>
			<AZLDAPGroup/>
			<AZValcred/>
			<AZSAMLURL/>
			<AZSAMLType>any</AZSAMLType>
			<AZSAMLXPath/>
			<AZSAMLNameQualifier/>
			<AZCacheAllow>absolute</AZCacheAllow>
			<AZCacheTTL>3</AZCacheTTL>
			<AZNetegrityBaseURI/>
			<AZNetegrityOpNameExtension/>
			<AZClearTrustServerURL/>
			<AZSAMLVersion>1.1</AZSAMLVersion>
			<AZLDAPLoadBalanceGroup/>
			<AZLDAPBindDN/>
			<AZLDAPBindPassword/>
			<AZLDAPGroupAttribute>member</AZLDAPGroupAttribute>
			<AZSSLProxyProfile/>
			<AZNetegrityConfig/>
			<AZLDAPSearchScope>subtree</AZLDAPSearchScope>
			<AZLDAPSearchFilter>(objectClass=*)</AZLDAPSearchFilter>
			<AZXACMLVersion>2.0</AZXACMLVersion>
			<AZXACMLPEPType>deny-biased</AZXACMLPEPType>
			<AZXACMLUseOnBoxPDP>on</AZXACMLUseOnBoxPDP>
			<AZXACMLPDP/>
			<AZXACMLExternalPDPUrl/>
			<AZXACMLBindingMethod>custom</AZXACMLBindingMethod>
			<AZXACMLBindingObject/>
			<AZXACMLBindingXSL/>
			<AZXACMLCustomObligation/>
			<AZXACMLUseSAML2>off</AZXACMLUseSAML2>
			<AZTAMServer/>
			<AZTAMDefaultAction>T</AZTAMDefaultAction>
			<AZTAMActionResourceMap/>
			<AZXACMLUseSOAP>off</AZXACMLUseSOAP>
			<AZZOSNSSConfig/>
			<AZSAFDefaultAction>r</AZSAFDefaultAction>
		</Authorize>
		<PostProcess>
			<PPEnabled>off</PPEnabled>
			<PPCustomURL/>
			<PPSAMLAuthAssertion>off</PPSAMLAuthAssertion>
			<PPSAMLServerName>XS</PPSAMLServerName>
			<PPSAMLNameQualifier/>
			<PPKerberosTicket>off</PPKerberosTicket>
			<PPKerberosClient/>
			<PPKerberosClientPassword/>
			<PPKerberosServer/>
			<PPWSTrust>off</PPWSTrust>
			<PPTimestamp>on</PPTimestamp>
			<PPTimestampExpiry>0</PPTimestampExpiry>
			<PPAllowRenewal>off</PPAllowRenewal>
			<PPSAMLVersion>1.0</PPSAMLVersion>
			<PPSAMLSendSLO>off</PPSAMLSendSLO>
			<PPSAMLSLOEndpoint/>
			<PPSSLProxyProfile/>
			<PPWSUsernameToken>off</PPWSUsernameToken>
			<PPWSUsernameTokenPasswordType>Digest</PPWSUsernameTokenPasswordType>
			<PPSAMLValidity>0</PPSAMLValidity>
			<PPSAMLSkew>0</PPSAMLSkew>
			<PPWSUsernameTokenIncludePwd>on</PPWSUsernameTokenIncludePwd>
			<PPLTPA>off</PPLTPA>
			<PPLTPAVersion>LTPA2</PPLTPAVersion>
			<PPLTPAExpiry>600</PPLTPAExpiry>
			<PPLTPAKeyFile/>
			<PPLTPAKeyFilePassword/>
			<PPLTPAStashFile/>
			<PPKerberosSPNEGOToken>off</PPKerberosSPNEGOToken>
			<PPKerberosBstValueType>http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1#GSS_Kerberosv5_AP_REQ</PPKerberosBstValueType>
			<PPSAMLUseWSSec>off</PPSAMLUseWSSec>
			<PPKerberosClientKeytab/>
			<PPUseWSSec>off</PPUseWSSec>
			<PPActorRoleID/>
			<PPTFIMTokenMapping>off</PPTFIMTokenMapping>
			<PPTFIMEndpoint/>
			<PPWSDerivedKeyUsernameToken>off</PPWSDerivedKeyUsernameToken>
			<PPWSDerivedKeyUsernameTokenIterations>1000</PPWSDerivedKeyUsernameTokenIterations>
			<PPWSUsernameTokenAllowReplacement>off</PPWSUsernameTokenAllowReplacement>
			<PPTFIMReplaceMethod>all</PPTFIMReplaceMethod>
			<PPTFIMRetrieveMode>CallTFIM</PPTFIMRetrieveMode>
			<PPHMACSigningAlg>hmac-sha1</PPHMACSigningAlg>
			<PPSigningHashAlg>sha1</PPSigningHashAlg>
			<PPWSTrustHeader>off</PPWSTrustHeader>
			<PPWSSCKeySource>random</PPWSSCKeySource>
			<PPSharedSecretKey/>
			<PPWSTrustRenewalWait>0</PPWSTrustRenewalWait>
			<PPWSTrustNewInstance>off</PPWSTrustNewInstance>
			<PPWSTrustNewKey>off</PPWSTrustNewKey>
			<PPWSTrustNeverExpire>off</PPWSTrustNeverExpire>
		</PostProcess>
		<SAMLSigningHashAlg>sha1</SAMLSigningHashAlg>
		<SAMLSigningAlg>rsa</SAMLSigningAlg>
		<LDAPsuffix/>
		<LogAllowed>on</LogAllowed>
		<LogAllowedLevel>info</LogAllowedLevel>
		<LogRejected>on</LogRejected>
		<LogRejectedLevel>warn</LogRejectedLevel>
		<PingIdentityCompatibility>off</PingIdentityCompatibility>
		<DoSValve>1</DoSValve>
		<LDAPVersion>v2</LDAPVersion>
		<EnforceSOAPActor>on</EnforceSOAPActor>
		<WSSecActorRoleID/>
	</AAAPolicy>
</#macro>
package no.stelvio.presentation.security.sso;

public final class ConfigPropertyKeys {

    public static final String ACCESS_MANAGER_USERNAME = "ACCESS_MANAGER_USERNAME";
    public static final String REQUEST_VALUE_TYPE = "REQUEST_VALUE_TYPE";
    // Used in the stelvio-tai-ldap-groups.properties file
    public static final String AUTHORIZED_AS_SELF_GROUPS_KEY = "AUTHORIZED_AS_SELF_GROUPS";
    // Keys to look for in the HttpServletRequest
    public static final String ACCESS_MANAGER_USER_REQUEST_VALUE_KEY = "ACCESS_MANAGER_USER_REQUEST_VALUE_KEY";
    public static final String ORIGINAL_USER_NAME_REQUEST_VALUE_KEY = "ORIGINAL_USER_NAME_REQUEST_VALUE_KEY";
    public static final String AUTHENTICATION_LEVEL_REQUEST_VALUE_KEY = "AUTHENTICATION_LEVEL_REQUEST_VALUE_KEY";
    public static final String AUTHORIZED_AS_REQUEST_VALUE_KEY = "AUTHORIZED_AS_REQUEST_VALUE_KEY";
    public static final String AUTHORIZATION_TYPE_REQUEST_VALUE_KEY = "AUTHORIZATION_TYPE_REQUEST_VALUE_KEY";
    public static final String COOKIE_REQUEST_VALUE_KEY = "COOKIE_REQUEST_VALUE_KEY";
    // OpenAM
    public static final String OPENAM_QUERY_TEMPLATE = "OPENAM_QUERY_TEMPLATE";

    // TAI custom property
    public static final String OPENAM_URL = "openam.url";
    public static final String AUTHTYPE_AUTHMETHOD_CHECK_DISABLED = "authtypeauthmetod.check.disabled";
    public static final String VALID_AUTHTYPES = "valid.authtypes";
    public static final String VALID_AUTHMETHODS = "valid.authmethods";
}

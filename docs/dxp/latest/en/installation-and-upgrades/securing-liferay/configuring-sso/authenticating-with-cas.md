# Authenticating with CAS (Central Authentication Service)

```{important}
CAS was deprecated as of Liferay DXP 7.2 and removed as of Liferay DXP 7.4. Please use [SAML](./authenticating-with-saml.md) instead. 
```

CAS is a widely used open source single sign-on solution and was the first SSO product to be supported by Liferay DXP. Liferay DXP's CAS module includes the CAS client, so there's no need to install it separately.

```{note}
Liferay DXP supports CAS 3.3.x. If you use a later version of CAS, it is best to use CAS's support for standards such as OpenID Connect or SAML to interface with Liferay DXP.
```

There are three steps to getting Liferay running with CAS:

1. Generate an SSL certificate
1. Install CAS
1. Configure Liferay to use CAS

## Generating an SSL Certificate

The CAS Server application requires your server to have a properly configured Secure Socket Layer (SSL) certificate. To generate one yourself, use the `keytool` utility that comes with the JDK. First generate the key, then export the key into a file. Finally, import the key into your local Java key store. For public, Internet-based production environments, you must purchase a signed key from a recognized certificate authority, from Let's Encrypt, or have your key signed by a recognized certificate authority. For Intranets, you should have your IT department pre-configure users' browsers to accept the certificate so they don't get warning messages about the certificate.

To generate a key, use the following command:

```bash
keytool -genkey -alias tomcat -keypass changeit -keyalg RSA
```

Instead of the password in the example (`changeit`), use a password you can remember. If you are not using Tomcat, you may want to use a different alias as well. For first and last names, enter `localhost` or the host name of your server. It cannot be an IP address.

To export the key to a file, use the following command:

```bash
keytool -export -alias tomcat -keypass changeit -file server.cert
```

Finally, to import the key into your Java key store, use the following command:

```bash
keytool -import -alias tomcat -file server.cert -keypass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts
```

If you are on a Windows system, replace `$JAVA_HOME` above with `%JAVA_HOME%`. Of course, all of this must be done on the system where CAS is running.

## Configure Liferay DXP to use CAS

Once your CAS server is up and running, configure Liferay DXP to use it. CAS configuration can be applied either at the system scope or at the scope of a portal instance. To configure the CAS SSO module at the system or instance scope, navigate to the Control Panel, click on *Configuration* &rarr; *System Settings* (or *Instance Settings*) &rarr; *Security* &rarr; *SSO*. The values configured in System Settings provide the default values for all portal instances. Enable CAS authentication and then modify the URL properties to point to your CAS server.

**Enabled:** Check this box to enable CAS single sign-on.

**Import from LDAP:** A user may be authenticated from CAS and not yet exist in Liferay DXP. Select this to automatically import users from LDAP if they do not exist in Liferay DXP. For this to work, LDAP must be enabled.

The rest of the settings are various URLs with defaults included. Change *localhost* in the default values to point to your CAS server. When you are finished, click *Save*. After this, when users click the *Sign In* link, they are directed to the CAS server to sign in to Liferay DXP.

For some situations, it might be more convenient to specify the system configuration via files on the disk. To do so, create the following file:

```bash
[Liferay Home]/osgi/configs/com.liferay.portal.security.sso.cas.configuration.CASConfiguration.config
```

The format of this file is the same as any properties file. The key to use for each property that can be configured is shown below. Enter values in the same format as you would when initializing a Java primitive type with a literal value.

| Property Label | Property Key | Description | Type |
| ----- | ----- | ----- | ----- |
| **Enabled** | `enabled` | Check this box to enable CAS SSO authentication. | `boolean` |
| **Import from LDAP** | `importFromLDAP` | Users authenticated from CAS that do not exist in Liferay DXP are imported from LDAP. LDAP must be enabled separately. | `boolean` |
| **Login URL** | `loginURL` | Set the CAS server login URL. | `String` |
| **Logout on session expiration** | `logoutOnSessionExpiration` | If checked, browsers with expired sessions are redirected to the CAS logout URL. | `boolean` |
| **Logout URL** | `logoutURL` | The CAS server logout URL. Set this if you want Liferay DXP's logout function to trigger a CAS logout | `String` |
| **Server Name** | `serverName` | The name of the Liferay DXP instance (e.g., `liferay.com`). If the provided name includes the protocol (`https://`, for example) then this will be used together with the path `/c/portal/login` to construct the URL to which the CAS server will provide tickets. If no scheme is provided, the scheme normally used to access the Liferay DXP login page is used. | `String` |
| **Server URL** | `serviceURL` | If provided, this is used as the URL to which the CAS server provides tickets. This overrides any URL constructed based on the Server Name as above. | `String` |
| **No Such User Redirect URL** | `noSuchUserRedirectURL` | Set the URL to which to redirect the user if the user can authenticate with CAS but cannot be found in Liferay DXP. If import from LDAP is enabled, the user is redirected if the user could not be found or could not be imported from LDAP. | `String` |

To override system defaults for a particular portal instance, navigate to the Control Panel, click on *Configuration* &rarr; *Instance Settings*, click on *Authentication* on the right and then on *CAS* at the top.

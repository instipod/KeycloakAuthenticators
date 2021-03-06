package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import com.instipod.keycloakauthenticators.utils.IPAddressMatcher;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class ConditionalIPAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalIPAuthenticator SINGLETON = new ConditionalIPAuthenticator();
    private static Logger logger = Logger.getLogger(ConditionalIPAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            boolean not = AuthenticatorUtils.getConfigBoolean(context, ConditionalIPAuthenticatorFactory.CONDITIONAL_NOT);
            String ipCIDR = authConfig.getConfig().get(ConditionalIPAuthenticatorFactory.CONDITIONAL_IP_FILTER);
            String[] cidrs = ipCIDR.split(",");
            boolean inRange = inRange(cidrs, context.getConnection().getRemoteAddr());

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("Conditional IP Authenticator result: " + context.getConnection().getRemoteAddr() + " is " + inRange);

            if (not) {
                return (!inRange);
            } else {
                return inRange;
            }
        } else {
            logger.error("No configuration data was found for the authenticator!");
        }

        if (AuthenticatorUtils.debuggingBuild)
            logger.info("Conditional IP Authenticator returning false");

        return false;
    }

    public boolean inRange(String[] ranges, String ip) {
        for (String cidr : ranges) {
            IPAddressMatcher matcher = new IPAddressMatcher(cidr);
            if (matcher.matches(ip)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Not used
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // Not used
    }

    @Override
    public void close() {
        // Does nothing
    }
}

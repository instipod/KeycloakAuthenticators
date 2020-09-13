package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import com.instipod.keycloakauthenticators.utils.IPAddressMatcher;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class ConditionalUserAgentAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalUserAgentAuthenticator SINGLETON = new ConditionalUserAgentAuthenticator();
    private static Logger logger = Logger.getLogger(ConditionalUserAgentAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            String regexToMatch = authConfig.getConfig().get(ConditionalUserAgentAuthenticatorFactory.CONDITIONAL_USER_AGENT);
            String userAgent = context.getHttpRequest().getHttpHeaders().getHeaderString("User-Agent");
            boolean check = (userAgent.matches(regexToMatch));

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("User Agent is " + userAgent + ", regex is " + regexToMatch + ", result is " + check);

            return check;
        } else {
            logger.error("No config data found for authenticator!");
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

package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class ConditionalRoleEnhancedAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalRoleEnhancedAuthenticator SINGLETON = new ConditionalRoleEnhancedAuthenticator();
    private static Logger logger = Logger.getLogger(ConditionalRoleEnhancedAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            boolean not = AuthenticatorUtils.getConfigBoolean(context, ConditionalRoleEnhancedAuthenticatorFactory.CONDITIONAL_NOT);
            String role = authConfig.getConfig().get(ConditionalRoleEnhancedAuthenticatorFactory.CONDITIONAL_ROLE);
            role = AuthenticatorUtils.variableReplace(context, role);

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("ConditionalRoleEhncd: Checking " + context.getUser().getUsername() + " for role " + role);

            boolean check;
            if (not) {
                //does not have role
                check = !(AuthenticatorUtils.hasRole(context, role));
            } else {
                //has role
                check = (AuthenticatorUtils.hasRole(context, role));
            }

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("ConditionalRoleEhncd: Result of check: " + check);

            return check;
        } else {
            logger.error("No config data found for this authenticator!");
        }

        return false;
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Not used
    }

    @Override
    public boolean requiresUser() {
        return true;
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

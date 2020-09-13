package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.IPAddressMatcher;
import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class ConditionalRoleEnhancedAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalRoleEnhancedAuthenticator SINGLETON = new ConditionalRoleEnhancedAuthenticator();

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            String not = authConfig.getConfig().get(ConditionalRoleEnhancedAuthenticatorFactory.CONDITIONAL_NOT);
            String role = authConfig.getConfig().get(ConditionalRoleEnhancedAuthenticatorFactory.CONDITIONAL_ROLE);
            role = AuthenticatorUtils.variableReplace(context, role);

            if (not.equalsIgnoreCase("true")) {
                //does not have role
                return !(AuthenticatorUtils.hasRole(context.getUser(), role));
            } else {
                //has role
                return (AuthenticatorUtils.hasRole(context.getUser(), role));
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

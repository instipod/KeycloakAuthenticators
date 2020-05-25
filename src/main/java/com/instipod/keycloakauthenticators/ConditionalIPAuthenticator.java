package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.IPAddressMatcher;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class ConditionalIPAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalIPAuthenticator SINGLETON = new ConditionalIPAuthenticator();

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            String ipCIDR = authConfig.getConfig().get(ConditionalIPAuthenticatorFactory.CONDITIONAL_IP_FILTER);
            IPAddressMatcher matcher = new IPAddressMatcher(ipCIDR);
            return (matcher.matches(context.getConnection().getRemoteAddr()));
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

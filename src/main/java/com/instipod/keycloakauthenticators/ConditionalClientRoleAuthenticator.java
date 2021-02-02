package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.*;

public class ConditionalClientRoleAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalClientRoleAuthenticator SINGLETON = new ConditionalClientRoleAuthenticator();
    private static Logger logger = Logger.getLogger(ConditionalClientRoleAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            boolean not = AuthenticatorUtils.getConfigBoolean(context, ConditionalClientRoleAuthenticatorFactory.CONDITIONAL_NOT);
            String role = authConfig.getConfig().get(ConditionalClientRoleAuthenticatorFactory.CONDITIONAL_ROLE);
            role = AuthenticatorUtils.variableReplace(context, role);

            ClientModel client = context.getAuthenticationSession().getClient();
            if (client == null) {
                //if no client, no match
                if (AuthenticatorUtils.debuggingBuild)
                    logger.info("ConditionalClientRoleEhncd: There is no client, so passing the user");
                return AuthenticatorUtils.getConfigBoolean(context, ConditionalClientRoleAuthenticatorFactory.CONDITIONAL_PASS);
            }

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("ConditionalClientRoleEhncd: Checking " + context.getUser().getUsername() + " for role " + role + " on client " + client.getClientId());

            RoleModel clientRole = client.getRole(role);
            if (clientRole == null) {
                //No such client role, pass the user
                if (AuthenticatorUtils.debuggingBuild)
                    logger.info("ConditionalClientRoleEhncd: The role specified did not exist on the client, passing the user...");
                return AuthenticatorUtils.getConfigBoolean(context, ConditionalClientRoleAuthenticatorFactory.CONDITIONAL_PASS);
            }

            boolean hasRole = context.getUser().hasRole(clientRole);

            boolean check;
            if (not) {
                //does not have role
                check = (hasRole == false);
            } else {
                //has role
                check = (hasRole == true);
            }

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("ConditionalClientRoleEhncd: Result of check: " + check);

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

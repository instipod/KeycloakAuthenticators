package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlow;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;

public class DenyLoginAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final DenyLoginAuthenticator SINGLETON = new DenyLoginAuthenticator();
    private static Logger logger = Logger.getLogger(DenyLoginAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //make sure the authenticator is configured
            String errorMessage = authConfig.getConfig().get(DenyLoginAuthenticatorFactory.DENIAL_MESSAGE);

            if (errorMessage.length() == 0) {
                logger.error("Denial message is not configured!");
                showDenial(authenticationFlowContext, "Authentication flow is not configured!");
            } else {
                showDenial(authenticationFlowContext, errorMessage);
            }
        } else {
            logger.error("Authenticator is not configured!");
            showDenial(authenticationFlowContext, "Authentication flow is not configured!");
        }
    }

    public void showDenial(AuthenticationFlowContext context, String message) {
        if (AuthenticatorUtils.debuggingBuild)
            logger.info("Serving denial for reason: " + message);

        LoginFormsProvider form = context.form();
        form.setError(message);
        Response response = form.createForm(DenyLoginAuthenticatorFactory.DENIAL_FILE);
        context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, response);
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        showDenial(authenticationFlowContext, "Invalid action requested!");
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }
}

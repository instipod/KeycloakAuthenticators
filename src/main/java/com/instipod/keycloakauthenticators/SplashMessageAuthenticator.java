package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
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

public class SplashMessageAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final SplashMessageAuthenticator SINGLETON = new SplashMessageAuthenticator();
    private static Logger logger = Logger.getLogger(SplashMessageAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();

        String templateFile = null;
        if (authConfig!=null && authConfig.getConfig()!=null) {
            //make sure the authenticator is configured
            templateFile = authConfig.getConfig().get(SplashMessageAuthenticatorFactory.TEMPLATE_FILE);

            if (templateFile.length() == 0) {
                logger.error("No template was specified in the configuration!");
                authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
                return;
            }
        } else {
            logger.error("No configuration was specified for Splash Message!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        Response challenge = generateForm(authenticationFlowContext).createForm(templateFile);
        authenticationFlowContext.challenge(challenge);
    }

    public LoginFormsProvider generateForm(AuthenticationFlowContext authenticationFlowContext) {
        LoginFormsProvider form = authenticationFlowContext.form();
        return form;
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        authenticationFlowContext.success();
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

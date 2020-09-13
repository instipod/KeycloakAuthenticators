package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class SetNoteAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final SetNoteAuthenticator SINGLETON = new SetNoteAuthenticator();
    private static Logger logger = Logger.getLogger(SetNoteAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //make sure the authenticator is configured
            String noteName = authConfig.getConfig().get(SetNoteAuthenticatorFactory.NOTE_NAME);
            String noteValue = authConfig.getConfig().get(SetNoteAuthenticatorFactory.NOTE_VALUE);

            noteValue = AuthenticatorUtils.variableReplace(authenticationFlowContext, noteValue);

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("SetNote for note " + noteName + " with value " + noteValue);

            authenticationFlowContext.getAuthenticationSession().setAuthNote(noteName, noteValue);
            authenticationFlowContext.success();
        } else {
            logger.error("Authenticator is not configured!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
        }
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {

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

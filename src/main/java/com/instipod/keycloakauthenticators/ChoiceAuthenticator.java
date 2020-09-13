package com.instipod.keycloakauthenticators;

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

public class ChoiceAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final ChoiceAuthenticator SINGLETON = new ChoiceAuthenticator();

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        LoginFormsProvider form = authenticationFlowContext.form();
        Response response = form.createForm(ChoiceAuthenticatorFactory.CHOICE_FILE);
        authenticationFlowContext.challenge(response);
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();
        MultivaluedMap<String, String> formData = authenticationFlowContext.getHttpRequest().getDecodedFormParameters();

        if (authConfig==null || authConfig.getConfig()==null) {
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        String noteName = authConfig.getConfig().get(ChoiceAuthenticatorFactory.NOTE_NAME);

        if (noteName.length() == 0) {
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        if (!formData.containsKey("choice")) {
            //no choice returned
            LoginFormsProvider form = authenticationFlowContext.form();
            form.setError("You must make a selection to continue.");
            Response response = form.createForm(ChoiceAuthenticatorFactory.CHOICE_FILE);
            authenticationFlowContext.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, response);
        } else {
            //choice returned
            String choice = formData.getFirst("choice");
            //remove any nonalphanumeric characters
            choice = choice.replaceAll("[^a-zA-Z0-9]", "");

            authenticationFlowContext.getAuthenticationSession().setAuthNote(noteName, choice);
            authenticationFlowContext.success();
        }
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

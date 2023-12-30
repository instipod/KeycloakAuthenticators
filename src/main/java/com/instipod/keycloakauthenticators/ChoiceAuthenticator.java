package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class ChoiceAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final ChoiceAuthenticator SINGLETON = new ChoiceAuthenticator();
    private static Logger logger = Logger.getLogger(ChoiceAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        if (AuthenticatorUtils.debuggingBuild)
            logger.info("Now showing choice authenticator selection screen");

        LoginFormsProvider form = authenticationFlowContext.form();
        Response response = form.createForm(ChoiceAuthenticatorFactory.CHOICE_FILE);
        authenticationFlowContext.challenge(response);
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();
        MultivaluedMap<String, String> formData = authenticationFlowContext.getHttpRequest().getDecodedFormParameters();

        if (authConfig==null || authConfig.getConfig()==null) {
            logger.error("No configuration data was found for authenticator!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        String noteName = authConfig.getConfig().get(ChoiceAuthenticatorFactory.NOTE_NAME);

        if (noteName.length() == 0) {
            logger.error("No note name was found for authenticator!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        if (!formData.containsKey("choice")) {
            //no choice returned
            logger.warn("No choice field was found in the returned form data!");
            LoginFormsProvider form = authenticationFlowContext.form();
            form.setError("You must make a selection to continue.");
            Response response = form.createForm(ChoiceAuthenticatorFactory.CHOICE_FILE);
            authenticationFlowContext.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, response);
        } else {
            //choice returned
            String choice = formData.getFirst("choice");
            //remove any nonalphanumeric characters
            choice = choice.replaceAll("[^a-zA-Z0-9]", "");

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("Choice selected:  setting " + noteName + " to value " + choice);

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

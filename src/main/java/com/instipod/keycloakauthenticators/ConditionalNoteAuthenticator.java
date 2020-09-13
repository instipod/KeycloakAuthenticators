package com.instipod.keycloakauthenticators;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class ConditionalNoteAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalNoteAuthenticator SINGLETON = new ConditionalNoteAuthenticator();

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate

            String noteName = authConfig.getConfig().get(ConditionalNoteAuthenticatorFactory.CONDITIONAL_NOTE_NAME);
            String noteValue = authConfig.getConfig().get(ConditionalNoteAuthenticatorFactory.CONDITIONAL_NOTE_VALUE);

            return (context.getAuthenticationSession().getAuthNote(noteName).equals(noteValue));
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

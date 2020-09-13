package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class ConditionalNoteAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalNoteAuthenticator SINGLETON = new ConditionalNoteAuthenticator();
    private static Logger logger = Logger.getLogger(ConditionalNoteAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate

            String noteName = authConfig.getConfig().get(ConditionalNoteAuthenticatorFactory.CONDITIONAL_NOTE_NAME);
            String noteValue = authConfig.getConfig().get(ConditionalNoteAuthenticatorFactory.CONDITIONAL_NOTE_VALUE);
            String not = authConfig.getConfig().get(ConditionalNoteAuthenticatorFactory.CONDITIONAL_NOTE_NOT);

            boolean check;
            if (not.equalsIgnoreCase("true")) {
                //note should not have value
                check = !(context.getAuthenticationSession().getAuthNote(noteName).equals(noteValue));
            } else {
                //note should have value
                check = (context.getAuthenticationSession().getAuthNote(noteName).equals(noteValue));
            }

            if (AuthenticatorUtils.debuggingBuild)
                logger.info("Conditional Note Result: " + noteName + " is " + noteValue + " result " + check);

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

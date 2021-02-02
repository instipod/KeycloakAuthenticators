package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.List;

public class ConditionalAttributeAuthenticator implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator {
    public static final ConditionalAttributeAuthenticator SINGLETON = new ConditionalAttributeAuthenticator();
    private static Logger logger = Logger.getLogger(ConditionalAttributeAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //evaluate
            boolean not = AuthenticatorUtils.getConfigBoolean(context, ConditionalAttributeAuthenticatorFactory.CONDITIONAL_NOT);
            String attributeName = authConfig.getConfig().get(ConditionalAttributeAuthenticatorFactory.CONDITIONAL_ATTRIBUTE_NAME);
            String attributeValue = authConfig.getConfig().get(ConditionalAttributeAuthenticatorFactory.CONDITIONAL_ATTRIBUTE_VALUE);
            attributeName = AuthenticatorUtils.variableReplace(context, attributeName);
            attributeValue = AuthenticatorUtils.variableReplace(context, attributeValue);

            List<String> values = context.getUser().getAttribute(attributeName);
            boolean hasAttribute;
            if (attributeValue.isBlank()) {
                //check presence only
                hasAttribute = (values.size() > 0);
            } else {
                hasAttribute = values.contains(attributeValue);
            }

            boolean check;
            if (not) {
                //does not have attribute
                check = !(hasAttribute);
            } else {
                //has attribute
                check = hasAttribute;
            }

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

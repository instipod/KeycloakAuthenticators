package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.io.IOException;
import org.jboss.logging.Logger;

public class SlackMessageAuthenticator implements org.keycloak.authentication.Authenticator {
    private static Logger _log = Logger.getLogger(SlackMessageAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        UserModel user = authenticationFlowContext.getUser();
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            String message = authConfig.getConfig().get(SlackMessageAuthenticatorFactory.SLACK_MESSAGE);
            String isCritical = authConfig.getConfig().get(SlackMessageAuthenticatorFactory.SLACK_IS_CRITICAL);
            String webhook = authConfig.getConfig().get(SlackMessageAuthenticatorFactory.SLACK_WEBHOOK_URL);

            message = AuthenticatorUtils.variableReplace(authenticationFlowContext, message);

            SlackMessage slackMessage = new SlackMessage(message);

            try {
                _log.info("Sent slack message: " + message);
                sendMessage(webhook, slackMessage);
            } catch (IOException exception) {
                //fail only if critical
                _log.warn("Slack Message Send was unsuccessful!");
                if (isCritical.equalsIgnoreCase("true")) {
                    authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
                }
            }

            authenticationFlowContext.success();
        } else {
            _log.error("Slack Message Executor is not configured!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
        }
    }

    public static void sendMessage(String webhookUrl, SlackMessage message) throws IOException {
        new Slack(webhookUrl).push(message);
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
    }

    @Override
    public boolean requiresUser() {
        return true;
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

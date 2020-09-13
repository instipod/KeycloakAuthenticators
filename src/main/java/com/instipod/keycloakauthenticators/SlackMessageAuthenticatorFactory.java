package com.instipod.keycloakauthenticators;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Collections;
import java.util.List;

public class SlackMessageAuthenticatorFactory implements org.keycloak.authentication.AuthenticatorFactory {
    public static final String PROVIDER_ID = "slack-message";
    protected static final String SLACK_WEBHOOK_URL = "slackWebhookURL";
    protected static final String SLACK_MESSAGE = "slackMessage";
    protected static final String SLACK_IS_CRITICAL = "slackIsCritical";

    private static List<ProviderConfigProperty> commonConfig;

    private static final SlackMessageAuthenticator SINGLETON = new SlackMessageAuthenticator();

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(SLACK_WEBHOOK_URL).label("Webhook URL").helpText("Webhook URL from Slack to post message to").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(SLACK_MESSAGE).label("Message Template").helpText("Message to send").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(SLACK_IS_CRITICAL).label("Is Critical").helpText("If selected, the authentication will fail if Slack is unreachable").type(ProviderConfigProperty.BOOLEAN_TYPE).add()
                .build()
        );
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope config) {
        // no-op
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Slack Message";
    }

    @Override
    public String getReferenceCategory() {
        return "notification";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Flow sends a message to specified slack webhook, and returns pass.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }
}

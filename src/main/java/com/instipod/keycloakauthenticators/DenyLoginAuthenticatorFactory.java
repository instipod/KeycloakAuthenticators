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

public class DenyLoginAuthenticatorFactory implements org.keycloak.authentication.AuthenticatorFactory {
    public static final String PROVIDER_ID = "deny-login";
    protected static final String DENIAL_MESSAGE = "denialMessage";
    private static List<ProviderConfigProperty> commonConfig;

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(DENIAL_MESSAGE).label("Denial Message").helpText("Message to show as an error").type(ProviderConfigProperty.STRING_TYPE).add()
                .build()
        );
    }

    @Override
    public String getDisplayType() {
        return "Deny Login";
    }

    @Override
    public String getReferenceCategory() {
        return "generic";
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
        return "Shows a predefined denial message to user, never returns success";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return DenyLoginAuthenticator.SINGLETON;
    }

    @Override
    public void init(Config.Scope scope) {
        //noop
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        //noop
    }

    @Override
    public void close() {
        //noop
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}

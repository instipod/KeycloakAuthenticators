package com.instipod.keycloakauthenticators;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

public class SuccessAuthenticatorFactory implements org.keycloak.authentication.AuthenticatorFactory {
    public static final String PROVIDER_ID = "always-success";

    @Override
    public String getDisplayType() {
        return "Success";
    }

    @Override
    public String getReferenceCategory() {
        return "generic";
    }

    @Override
    public boolean isConfigurable() {
        return false;
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
        return "Always returns success";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SuccessAuthenticator.SINGLETON;
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

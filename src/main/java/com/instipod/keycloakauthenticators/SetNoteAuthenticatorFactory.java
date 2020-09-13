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

public class SetNoteAuthenticatorFactory implements org.keycloak.authentication.AuthenticatorFactory {
    public static final String PROVIDER_ID = "set-note";
    protected static final String NOTE_NAME = "setNoteName";
    protected static final String NOTE_VALUE = "setNoteValue";
    private static List<ProviderConfigProperty> commonConfig;

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(NOTE_NAME).label("Note Name").helpText("Name of the note to set").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(NOTE_VALUE).label("Note Value").helpText("Value of the note to set (variables are supported)").type(ProviderConfigProperty.STRING_TYPE).add()
                .build()
        );
    }

    @Override
    public String getDisplayType() {
        return "Set Auth Note";
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
        return "Sets a predefined value for an authentication note";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SetNoteAuthenticator.SINGLETON;
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

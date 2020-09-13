package com.instipod.keycloakauthenticators;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Collections;
import java.util.List;

public class ConditionalNoteAuthenticatorFactory implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory {
    public static final String PROVIDER_ID = "conditional-note";
    protected static final String CONDITIONAL_NOTE_NAME = "condNoteName";
    protected static final String CONDITIONAL_NOTE_VALUE = "condNoteValue";

    private static List<ProviderConfigProperty> commonConfig;

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(CONDITIONAL_NOTE_NAME).label("Auth Note Name").helpText("Name of the auth note to check").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(CONDITIONAL_NOTE_VALUE).label("Auth Note Value").helpText("Value that the auth note should have").type(ProviderConfigProperty.BOOLEAN_TYPE).add()
                .build()
        );
    }

    @Override
    public void init(Scope config) {
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
        return "Condition - Auth Note";
    }

    @Override
    public String getReferenceCategory() {
        return "condition";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    private static final Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Flow is executed only if authentication session has a specified note.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalNoteAuthenticator.SINGLETON;
    }
}

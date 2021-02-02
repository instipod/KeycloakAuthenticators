package com.instipod.keycloakauthenticators;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Collections;
import java.util.List;

public class ConditionalAttributeAuthenticatorFactory implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory {
    public static final String PROVIDER_ID = "conditional-attribute";
    protected static final String CONDITIONAL_ATTRIBUTE_NAME = "condAttributeName";
    protected static final String CONDITIONAL_ATTRIBUTE_VALUE = "condAttributeValue";
    protected static final String CONDITIONAL_NOT = "condNot";

    private static List<ProviderConfigProperty> commonConfig;

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(CONDITIONAL_ATTRIBUTE_NAME).label("User Attribute Name").helpText("Attribute name to check (supports variables)").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(CONDITIONAL_ATTRIBUTE_VALUE).label("User Attribute Value").helpText("Attribute value (supports variables) (leave blank to only check if present)").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(CONDITIONAL_NOT).label("Not").helpText("If we should match on NOT having this attribute").type(ProviderConfigProperty.BOOLEAN_TYPE).add()
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
        return "Condition - Attribute";
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
            Requirement.REQUIRED, Requirement.DISABLED
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
        return "Flow is executed only if user has specified attribute.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalAttributeAuthenticator.SINGLETON;
    }
}

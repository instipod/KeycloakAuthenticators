package com.instipod.keycloakauthenticators;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Collections;
import java.util.List;

public class ConditionalClientRoleAuthenticatorFactory implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory {
    public static final String PROVIDER_ID = "conditional-client-role";
    protected static final String CONDITIONAL_ROLE = "condRole";
    protected static final String CONDITIONAL_NOT = "condNot";

    private static List<ProviderConfigProperty> commonConfig;

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(CONDITIONAL_ROLE).label("Role Id").helpText("Role name to check for on the client (supports variables)").type(ProviderConfigProperty.STRING_TYPE).add()
                .property().name(CONDITIONAL_NOT).label("Not").helpText("If we should match on NOT having this role").type(ProviderConfigProperty.BOOLEAN_TYPE).add()
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
        return "Condition - Client Role";
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
        return "Flow is executed only if user has specified role (client role).";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalClientRoleAuthenticator.SINGLETON;
    }
}

package com.instipod.keycloakauthenticators;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConditionalIPAuthenticatorFactory implements org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory, org.keycloak.provider.ServerInfoAwareProviderFactory {
    public static final String PROVIDER_ID = "conditional-ip-filter";
    protected static final String CONDITIONAL_IP_FILTER = "condIpFilter";

    private static List<ProviderConfigProperty> commonConfig;

    static {
        commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
                .property().name(CONDITIONAL_IP_FILTER).label("IP Filter").helpText("CIDR-Formatted IP Range to Filter").type(ProviderConfigProperty.STRING_TYPE).add()
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
        return "Condition - IP Filter";
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
        return "Flow is executed only if user is connecting from specified IP range.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return commonConfig;
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalIPAuthenticator.SINGLETON;
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("ip-authenticator-installed", "yes");
        return ret;
    }
}

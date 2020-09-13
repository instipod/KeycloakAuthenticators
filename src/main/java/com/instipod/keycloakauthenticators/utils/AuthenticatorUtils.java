package com.instipod.keycloakauthenticators.utils;

import com.instipod.keycloakauthenticators.ConditionalNoteAuthenticatorFactory;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.Set;

public class AuthenticatorUtils {
    public static final boolean debuggingBuild = false;

    public static String variableReplace(AuthenticationFlowContext context, String message) {
        String original = message;

        UserModel user = null;
        try {
            user = context.getUser();
        } catch (Exception ex) { }

        try {
            message = message.replace("%userid%", user.getId());
        } catch (Exception ex) { }
        try {
            message = message.replace("%username%", user.getUsername());
        } catch (Exception ex) { }
        try {
            message = message.replace("%email%", user.getEmail());
        } catch (Exception ex) { }
        try {
            message = message.replace("%firstname%", user.getFirstName());
        } catch (Exception ex) { }
        try {
            message = message.replace("%lastname%", user.getLastName());
        } catch (Exception ex) { }
        try {
            message = message.replace("%ipaddress%", context.getConnection().getRemoteAddr());
        } catch (Exception ex) { }
        try {
            message = message.replace("%clientid%", context.getAuthenticationSession().getClient().getId());
        } catch (Exception ex) { }
        try {
            message = message.replace("%clientname%", context.getAuthenticationSession().getClient().getName());
        } catch (Exception ex) { }
        try {
            message = message.replace("%clientdesc%", context.getAuthenticationSession().getClient().getDescription());
        } catch (Exception ex) { }

        if (debuggingBuild)
            Logger.getLogger(AuthenticatorUtils.class).info("Variable Replace: Original " + original + " replaced with " + message);

        return message;
    }

    public static boolean hasRole(AuthenticationFlowContext context, String roleId) {
        RoleModel role = context.getRealm().getRoleById(roleId);

        if (role == null) {
            Logger.getLogger(AuthenticatorUtils.class).warn("Could not find role by id " + roleId);
            return false;
        }

        if (context.getUser() == null) {
            return false;
        }

        return (context.getUser().hasRole(role));
    }

    public static boolean getConfigBoolean(AuthenticationFlowContext context, String configName) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig!=null && authConfig.getConfig()!=null) {
            String booleanValue = authConfig.getConfig().get(configName);

            if (booleanValue == null) {
                return false;
            }
            if (booleanValue.equalsIgnoreCase("true")) {
                return true;
            }

            //otherwise
            return false;
        } else {
            return false;
        }
    }
}

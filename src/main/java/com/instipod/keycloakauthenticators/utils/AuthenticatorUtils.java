package com.instipod.keycloakauthenticators.utils;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.Set;

public class AuthenticatorUtils {
    public static String variableReplace(AuthenticationFlowContext context, String message) {
        UserModel user = null;
        try {
            user = context.getUser();
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
            message = message.replace("%clientname%", context.getAuthenticationSession().getClient().getName());
        } catch (Exception ex) { }
        try {
            message = message.replace("%clientdesc%", context.getAuthenticationSession().getClient().getDescription());
        } catch (Exception ex) { }

        return message;
    }

    public static boolean hasRole(UserModel user, String roleName) {
        Set<RoleModel> roles = user.getRoleMappings();

        for (RoleModel role : roles) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return true;
            }
        }

        return false;
    }
}

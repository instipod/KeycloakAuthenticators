package com.instipod.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.AuthenticatorUtils;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QRCodeAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final QRCodeAuthenticator SINGLETON = new QRCodeAuthenticator();
    private static Logger logger = Logger.getLogger(QRCodeAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();
        if (AuthenticatorUtils.debuggingBuild)
            logger.info("Serving a new QR Code authenticator session");

        if (authConfig!=null && authConfig.getConfig()!=null) {
            //make sure the authenticator is configured
            String attributeName = authConfig.getConfig().get(QRCodeAuthenticatorFactory.QR_KEY_ATTRIBUTE);
            if (attributeName.length() == 0) {
                logger.error("No attribute name to search was specified in QR Code Authenticator!");
                authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
                return;
            }
        } else {
            logger.error("No configuration was specified in QR Code Authenticator!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        Response challenge = generateForm(authenticationFlowContext).createForm(QRCodeAuthenticatorFactory.QR_SCANNER_FILE);
        authenticationFlowContext.challenge(challenge);
    }

    public LoginFormsProvider generateForm(AuthenticationFlowContext authenticationFlowContext) {
        LoginFormsProvider form = authenticationFlowContext.form();
        return form;
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        AuthenticatorConfigModel authConfig = authenticationFlowContext.getAuthenticatorConfig();
        MultivaluedMap<String, String> formData = authenticationFlowContext.getHttpRequest().getDecodedFormParameters();

        if (formData.containsKey("cancel")) {
            if (AuthenticatorUtils.debuggingBuild)
                logger.info("Received request to cancel");

            authenticationFlowContext.failure(AuthenticationFlowError.INVALID_CREDENTIALS);
            return;
        }

        if (authConfig==null || authConfig.getConfig()==null) {
            //configuration does not exist
            logger.error("No configuration was specified in QR Code Authenticator!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        String attributeName = authConfig.getConfig().get(QRCodeAuthenticatorFactory.QR_KEY_ATTRIBUTE);
        String qrCodeData = formData.getFirst("qrCodeData");
        //remove all nonalphanumeric characters from data
        qrCodeData = qrCodeData.replaceAll("[^a-zA-Z0-9]", "");

        if (attributeName.length() == 0) {
            logger.error("No attribute name to search was specified in QR Code Authenticator!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        if (AuthenticatorUtils.debuggingBuild)
            logger.info("QR Code value submitted, looking in " + attributeName + " for value " + qrCodeData);

        boolean isValid = false;
        Stream<UserModel> usersStream = authenticationFlowContext.getSession().users().searchForUserByUserAttributeStream(authenticationFlowContext.getRealm(), attributeName, qrCodeData);
        List<UserModel> users = usersStream.collect(Collectors.toUnmodifiableList());

        if (users.size() == 1 && qrCodeData.length() > 0) {
            //qr is valid, returned exactly one result
            UserModel user = users.get(0);
            if (AuthenticatorUtils.debuggingBuild)
                logger.info("User " + user.getUsername() + " authenticated successfully using QR Code.");

            authenticationFlowContext.clearUser();
            authenticationFlowContext.setUser(user);
            authenticationFlowContext.getAuthenticationSession().setAuthNote("qr-code-authenticated", "true");
            authenticationFlowContext.success();
        } else {
            //qr is invalid, either returned zero or multiple users
            if (AuthenticatorUtils.debuggingBuild)
                logger.warn("Invalid QR Code data was provided.  Data was " + qrCodeData);

            LoginFormsProvider form = generateForm(authenticationFlowContext);
            form.setError("That QR code is not valid.");
            Response challenge = form.createForm(QRCodeAuthenticatorFactory.QR_SCANNER_FILE);
            authenticationFlowContext.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
        }
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }
}

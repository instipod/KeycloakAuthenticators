package com.instipod.keycloakauthenticators;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;

public class QRCodeAuthenticator implements org.keycloak.authentication.Authenticator {
    public static final QRCodeAuthenticator SINGLETON = new QRCodeAuthenticator();
    private static Logger logger = Logger.getLogger(QRCodeAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        logger.debug("Starting a new QR Code authenticator session");

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
            authenticationFlowContext.failure(AuthenticationFlowError.INVALID_USER);
            return;
        }

        String attributeName = authConfig.getConfig().get(QRCodeAuthenticatorFactory.QR_KEY_ATTRIBUTE);
        String qrCodeData = formData.getFirst("qrCodeData");

        if (attributeName.length() == 0) {
            logger.error("No attribute name to search was specified in QR Code Authenticator!");
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
        }

        logger.debug("QR Code value submitted, looking in " + attributeName + " for value " + qrCodeData);

        boolean isValid = false;
        List<UserModel> users = authenticationFlowContext.getSession().users().searchForUserByUserAttribute(attributeName, qrCodeData, authenticationFlowContext.getRealm());

        if (users.size() == 1 && qrCodeData.length() > 0) {
            //qr is valid, returned exactly one result
            UserModel user = users.get(0);
            logger.info("User " + user.getUsername() + " authenticated successfully using QR Code.");

            authenticationFlowContext.clearUser();
            authenticationFlowContext.setUser(user);
            authenticationFlowContext.success();
        } else {
            //qr is invalid, either returned zero or multiple users

            logger.warn("Invalid QR Code data was provided.  Data was " + qrCodeData);

            LoginFormsProvider form = generateForm(authenticationFlowContext);
            form.setError("The QR code that was scanned is not valid.");
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

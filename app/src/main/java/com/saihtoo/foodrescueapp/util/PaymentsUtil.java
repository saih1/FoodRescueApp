package com.saihtoo.foodrescueapp.util;

import android.app.Activity;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class PaymentsUtil {
    public static final int REQUEST_PAYMENT = 10120;

    //Define Google Pay API version
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    //Request a payment token for the payment provider
    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject()
            {{
                put("gateway", "example");
                put("gatewayMerchantId", "exampleGatewayMerchantId");
            }});
        }};
    }

    //Define supported payment card networks
    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray()
                .put("AMEX").put("DISCOVER")
                .put("INTERAC").put("JCB")
                .put("MASTERCARD").put("VISA");
    }

    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS");
    }

    //Describe the allowed payment methods
    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");
        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
        parameters.put("allowedCardNetworks", getAllowedCardNetworks());
        // Optional to add billing address/phone number associated with a CARD payment method.
        parameters.put("billingAddressRequired", true);
        JSONObject billingAddressParameters = new JSONObject();
        billingAddressParameters.put("format", "FULL");
        parameters.put("billingAddressParameters", billingAddressParameters);
        cardPaymentMethod.put("parameters", parameters);
        return cardPaymentMethod;
    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());
        return cardPaymentMethod;
    }

    // Create a PaymentsClient instance
    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    //Determine readiness to pay with the Google Pay API
    public static Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
            return Optional.of(isReadyToPayRequest);
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    //Create a PaymentDataRequest object
    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", "AU");
        transactionInfo.put("currencyCode", "AUD");
        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");
        return transactionInfo;
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject().put("merchantName", "Example Merchant");
    }

    public static Optional<JSONObject> getPaymentDataRequest(Integer intPrice) {
        final String strPrice = intPrice.toString();
        try {
            JSONObject paymentDataRequest = PaymentsUtil.getBaseRequest();
            paymentDataRequest.put("allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", PaymentsUtil.getTransactionInfo(strPrice));
            paymentDataRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo());
            return Optional.of(paymentDataRequest);
        } catch (JSONException e) {
            return Optional.empty();
        }
    }
}

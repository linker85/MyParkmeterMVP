package uk.co.ribot.androidboilerplate.ui.forgot_password;

import android.content.SharedPreferences;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by raul on 09/12/2016.
 */
public interface ForgotPasswordMvpView extends MvpView {
    void setValidationEmailError(boolean valid);
    void setValidationPasswordError(boolean valid);
    void setValidationNameError(boolean valid);
    void setValidationPlateError(boolean valid);
    boolean isValidEmail(String email);

    void loadPreferences(SharedPreferences sharedPreferences);
}
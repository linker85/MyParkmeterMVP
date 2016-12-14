package uk.co.ribot.androidboilerplate.ui.login;

import android.content.SharedPreferences;

import uk.co.ribot.androidboilerplate.data.model.LoginResponse;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by raul on 09/12/2016.
 */
public interface LoginMvpView extends MvpView {

    void doSaveInitPrefAndLogin(SharedPreferences sharedPref, LoginResponse response);

    void showProgressDialog();

    void hideProgressDialog();

    boolean isShowing();

    void setLoginError(String msg);

    void setValidationEmailError(boolean valid);
    void setValidationPasswordError(boolean valid);

    boolean isValidEmail(String email);

    void rememberMe(SharedPreferences sharedPref);

}
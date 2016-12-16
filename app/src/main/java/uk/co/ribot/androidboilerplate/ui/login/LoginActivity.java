package uk.co.ribot.androidboilerplate.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.LoginResponse;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.forgot_password.ForgotPasswordActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    private ProgressDialog progressDialog;

    private static final String TAG = "LoginTAG_";
    @BindView(R.id.input_email_sign_in)
    public TextView        emailSignInTxt;
    @BindView(R.id.input_password_sign_in)
    public TextView        passwordSignInTxt;
    @BindView(R.id.sign_in_layout_error)
    public TextView        errorMessageSignIn;
    public String          defaultValue;
    @BindView(R.id.remember)
    public CheckBox        rememberMe;
    @BindView(R.id.input_layout_email_sign_in)
    public TextInputLayout inputLayoutMail;
    @BindView(R.id.input_layout_password_sign_in)
    public TextInputLayout inputLayoutPassword;

    private String userId;

    @Inject
    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        //Stetho.initializeWithDefaults(this);

        // Bind with butterknife
        ButterKnife.bind(this);
        // Attach presenter
        mLoginPresenter.attachView(this);
        // Add listeners
        emailSignInTxt   .addTextChangedListener(new MyTextWatcherLogin(emailSignInTxt));
        passwordSignInTxt.addTextChangedListener(new MyTextWatcherLogin(passwordSignInTxt));
        // Remember me
        mLoginPresenter.rememberMe();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }

    @Override
    public void doSaveInitPrefAndLogin(SharedPreferences sharedPref, LoginResponse response) {
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", response.getUser().getEmail());
        editor.putString("name", response.getUser().getName());
        editor.putString("plate", response.getUser().getPlate());
        editor.putInt("card", response.getUser().getCard());
        editor.putString("userId", response.getUser().getUserid());
        if (rememberMe.isChecked()) {
            editor.putString("rem", "1");
            editor.putString("emailR", emailSignInTxt.getText().toString());
        } else {
            editor.putString("rem", "0");
            editor.remove("emailR");
        }
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("email", "email");
        startActivity(intent);
    }

    @Override
    public void rememberMe(SharedPreferences sharedPref) {
        try {
            defaultValue = sharedPref.getString("rem", "0");
            userId       = sharedPref.getString("userId", "");
            if (defaultValue != null && defaultValue.equals("1")) {
                // Do the login
                emailSignInTxt.setText(sharedPref.getString("emailR", ""));
                rememberMe.setChecked(true);
            } else {
                emailSignInTxt.setText("");
                rememberMe.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return progressDialog.isShowing();
    }

    @Override
    public void setLoginError(String msg) {
        Log.d(TAG, "setLoginError: " + Thread.currentThread());
        errorMessageSignIn.setText(msg);
    }

    @Override
    public void setValidationEmailError(boolean valid) {
        if (valid) {
            inputLayoutMail.setError(getString(R.string.err_msg_email));
            requestFocus(emailSignInTxt);
        } else {
            inputLayoutMail.setErrorEnabled(false);
        }
    }

    @Override
    public void setValidationPasswordError(boolean valid) {
        if (valid) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(passwordSignInTxt);
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void doSignIn(View view) {
        boolean isValid = mLoginPresenter.validateInput(emailSignInTxt.getText().toString(), 1) &&
                mLoginPresenter.validateInput(passwordSignInTxt.getText().toString(), 2);
        if (isValid) {
            mLoginPresenter.loadFromModel(emailSignInTxt.getText().toString(),
                    passwordSignInTxt.getText().toString(), userId);
        }

    }

    public void doForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private class MyTextWatcherLogin implements TextWatcher {

        private View view;

        private MyTextWatcherLogin(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.hasFocus()) {
                switch (view.getId()) {
                    case R.id.input_email_sign_in:
                        mLoginPresenter.validateInput(emailSignInTxt.getText().toString(), 1);
                        break;
                    case R.id.input_password_sign_in:
                        mLoginPresenter.validateInput(passwordSignInTxt.getText().toString(), 2);
                        break;
                }
            }
        }
    }

}
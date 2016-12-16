package uk.co.ribot.androidboilerplate.ui.forgot_password;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements ForgotPasswordMvpView {

    private static final String TAG = "SettingsFragmentTAG_";
    @BindView(R.id.input_name)
    public EditText inputName;
    @BindView(R.id.input_email)
    public EditText inputEmail;
    @BindView(R.id.input_password)
    public EditText inputPassword;
    @BindView(R.id.input_plate)
    public EditText inputPlate;
    @BindView(R.id.input_layout_name)
    public TextInputLayout inputLayoutName;
    @BindView(R.id.input_layout_email)
    public TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_layout_password)
    public TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_register)
    public Button btn_register;
    @BindView(R.id.id_settings_status)
    public TextView settingStatus;
    @BindView(R.id.input_layout_plate)
    public TextInputLayout inputLayoutPlate;
    private int settingsFragment;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Inject
    ForgotPasswordPresenter mForgotPasswordPresenter;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingStatus.setVisibility(getView().INVISIBLE);

        // From where is the fragment coming from
        if (getArguments() != null) {
            settingsFragment = getArguments().getInt("settingsFragment");
        } else {
            settingsFragment = 3;
        }

        if (settingsFragment == 1) {
            // Is signup (option 1)
            inputLayoutPlate.setVisibility(View.GONE);

            inputName    .addTextChangedListener(new MyTextWatcher(inputName));
            inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        } else if (settingsFragment == 2) {
            // Is forgot password (option 2)
            inputLayoutPlate.setVisibility(View.GONE);
            inputLayoutPassword.setVisibility(View.GONE);
            inputLayoutName.setVisibility(View.GONE);
            btn_register.setText(R.string.get_new_password);
        } else if (settingsFragment == 3) {
            // Is settings (option 3)
            try {
                String email      = sharedPreferences.getString("email", "");
                String name       = sharedPreferences.getString("name", "");
                String plate      = sharedPreferences.getString("plate", "");
                if (email != null) {
                    inputEmail.setText(email);
                }
                if (name != null) {
                    inputName.setText(name);
                }
                if (plate != null) {
                    inputPlate.setText(plate);
                }
                inputEmail.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            inputPlate   .addTextChangedListener(new MyTextWatcher(inputPlate));
            inputName    .addTextChangedListener(new MyTextWatcher(inputName));
            inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        }
        inputEmail   .addTextChangedListener(new MyTextWatcher(inputEmail));
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (settingsFragment == 1 || settingsFragment == 3) {
            if (!mForgotPasswordPresenter.validateInput(inputName.getText().toString(), 1)) {
                return;
            }
        }

        if (!mForgotPasswordPresenter.validateInput(inputEmail.getText().toString(), 2)) {
            return;
        }

        if (settingsFragment == 1 || settingsFragment == 3) {
            if (!mForgotPasswordPresenter.validateInput(inputPassword.getText().toString(), 3)) {
                return;
            }
        }

        if (settingsFragment == 3) {
            if (!mForgotPasswordPresenter.validateInput(inputPlate.getText().toString(), 4)) {
                return;
            }
        }

        progressDialog = new ProgressDialog(getActivity());

        String name  = inputName.getText().toString();
        String pass  = inputPassword.getText().toString();
        String email = inputEmail.getText().toString();
        String plate = null;
        if (settingsFragment == 3) {
            plate = inputPlate.getText().toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentComponent().inject(this);

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        Log.d(TAG, "mForgotPasswordPresenter: " + (mForgotPasswordPresenter == null));
        // Attach presenter
        mForgotPasswordPresenter.attachView(this);

        // Load preferences
        mForgotPasswordPresenter.loadSharedPreferences();

        return view;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    mForgotPasswordPresenter.validateInput(inputName.getText().toString(), 1);
                    break;
                case R.id.input_email:
                    mForgotPasswordPresenter.validateInput(inputEmail.getText().toString(), 2);
                    break;
                case R.id.input_password:
                    mForgotPasswordPresenter.validateInput(inputPassword.getText().toString(), 3);
                    break;
                case R.id.input_plate:
                    mForgotPasswordPresenter.validateInput(inputPlate.getText().toString(), 4);
                    break;
            }
        }
    }

    @Override
    public void setValidationEmailError(boolean valid) {
        if (valid) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
    }

    @Override
    public void setValidationPasswordError(boolean valid) {
        if (valid) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
    }

    @Override
    public void setValidationNameError(boolean valid) {
        if (valid) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
    }

    @Override
    public void setValidationPlateError(boolean valid) {
        if (valid) {
            inputLayoutPlate.setError(getString(R.string.err_msg_plate));
            requestFocus(inputPlate);
        } else {
            inputLayoutPlate.setErrorEnabled(false);
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void loadPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
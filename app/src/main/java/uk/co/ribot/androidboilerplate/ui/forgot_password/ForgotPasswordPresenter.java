package uk.co.ribot.androidboilerplate.ui.forgot_password;

import android.content.SharedPreferences;

import javax.inject.Inject;

import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;

/**
 * Created by raul on 09/12/2016.
 */
@ConfigPersistent
public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordMvpView> {
    private static final String TAG = "LoginPresenterTAG_";

    @Inject
    DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public ForgotPasswordPresenter() {}

    @Override
    public void attachView(ForgotPasswordMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.detachView();
    }

    // 1. name, 2. email, 3. password, 4. plate
    public boolean validateInput(String input, int type) {
        if (type == 1) {
            getMvpView().setValidationNameError(input.trim().isEmpty() || input.equals(""));
            return !(input.trim().isEmpty() && input.equals("") && !getMvpView().isValidEmail(input));
        } else if (type == 2) {
            getMvpView().setValidationEmailError(input.trim().isEmpty() || input.equals("") || !getMvpView().isValidEmail(input));
            return !(input.trim().isEmpty() && input.equals(""));
        } else if (type == 3) {
            getMvpView().setValidationPasswordError(input.trim().isEmpty() || input.equals(""));
            return !(input.trim().isEmpty() && input.equals(""));
        } else {
            getMvpView().setValidationPlateError(input.trim().isEmpty() || input.equals(""));
            return !(input.trim().isEmpty() && input.equals(""));
        }
    }

    public void loadSharedPreferences() {
        SharedPreferences sharedPref = mDataManager.getPreferencesHelper().
                getSharedPreferences();
        getMvpView().loadPreferences(sharedPref);
    }

}
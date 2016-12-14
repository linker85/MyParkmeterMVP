package uk.co.ribot.androidboilerplate.ui.login;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.LoginResponse;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;

/**
 * Created by raul on 09/12/2016.
 */
@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMvpView> {
    private static final String TAG = "LoginPresenterTAG_";

    @Inject
    DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public LoginPresenter() {}

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.detachView();
    }

    // 1. email, 2. password
    public boolean validateInput(String input, int type) {
        if (type == 1) {
            getMvpView().setValidationEmailError(input.trim().isEmpty() || input.equals("") || !getMvpView().isValidEmail(input));
            return !(input.trim().isEmpty() && input.equals("") && !getMvpView().isValidEmail(input));
        } else {
            getMvpView().setValidationPasswordError(input.trim().isEmpty() || input.equals(""));
            return !(input.trim().isEmpty() && input.equals(""));
        }
    }

    public void rememberMe() {
        SharedPreferences sharedPref = mDataManager.getPreferencesHelper().
                getSharedPreferences();

        getMvpView().rememberMe(sharedPref);
    }

    public void loadFromModel(String email, String password, String userId) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.doLogin(email, password, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    String  message = "";
                    boolean success = false;

                    @Override
                    public void onStart() {
                        getMvpView().showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                        Timber.i("sign in successfully!");
                        Log.d(TAG, "onCompleted: ");
                        getMvpView().hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e, "Error syncing.");
                        Log.d(TAG, "onError: ");
                        getMvpView().hideProgressDialog();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Log.d(TAG, "onNext: " + loginResponse.toString());
                        if (getMvpView().isShowing()) {
                            getMvpView().hideProgressDialog();
                        }
                        if (loginResponse != null && loginResponse.getSuccess()) {
                            Log.d(TAG, "loadFromModel: ");
                            success = true;
                            SharedPreferences sharedPref = mDataManager.getPreferencesHelper().
                                    getSharedPreferences();

                            getMvpView().doSaveInitPrefAndLogin(sharedPref, loginResponse);
                        } else {
                            success = false;
                            Log.d(TAG, "onNext: " + Thread.currentThread());
                            message = "Invalid email or password";
                            getMvpView().setLoginError(message);
                            getMvpView().hideProgressDialog();
                        }
                    }
                });
    }

}
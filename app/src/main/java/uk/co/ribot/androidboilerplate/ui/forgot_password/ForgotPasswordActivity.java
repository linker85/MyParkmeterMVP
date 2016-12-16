package uk.co.ribot.androidboilerplate.ui.forgot_password;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.login.LoginActivity;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordMvpView {

    private static final String TAG = "ForgotTAG_";
    @Inject
    ForgotPasswordPresenter mForgotPasswordPresenter;
    @Inject
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_forgot_password);
        // Bind with butterknife
        ButterKnife.bind(this);

        // Attach presenter
        mForgotPasswordPresenter.attachView(this);
        mForgotPasswordPresenter.testMe();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.forgot_password);

        Log.d(TAG, "handler: " + (handler == null));
        loadSignUpFragment();
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu.
     * Loads the fragment returned from getHomeFragment() function into FrameLayout.
     * It also takes care of other things like changing the toolbar title, hiding / showing fab,
     * invalidating the options menu so that new menu can be loaded for different fragment.
     */
    private void loadSignUpFragment() {
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                // 1. Signup, 2. Remember, 3. settings
                bundle.putInt("settingsFragment", 2);

                // update the main content by replacing fragments
                Fragment fragment = new SettingsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_forgot_password, fragment, "id_forgot_password");
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            handler.post(mPendingRunnable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void test() {
        Log.d(TAG, "test: ");
    }
}

package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.forgot_password.ForgotPasswordActivity;
import uk.co.ribot.androidboilerplate.ui.forgot_password.SettingsFragment;
import uk.co.ribot.androidboilerplate.ui.login.LoginActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(ForgotPasswordActivity forgotPasswordActivity);
    void inject(SettingsFragment settingsFragment);
}
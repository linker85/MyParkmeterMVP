package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerFragment;
import uk.co.ribot.androidboilerplate.injection.module.FragmentModule;
import uk.co.ribot.androidboilerplate.ui.forgot_password.SettingsFragment;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(SettingsFragment settingsFragment);
}
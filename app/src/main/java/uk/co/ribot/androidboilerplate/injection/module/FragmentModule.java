package uk.co.ribot.androidboilerplate.injection.module;

import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import uk.co.ribot.androidboilerplate.injection.ActivityContext;

/**
 * Created by raul on 14/12/2016.
 */
@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Provides
    Fragment provideActivity() {
        return mFragment;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mFragment.getActivity().getApplicationContext();
    }
}
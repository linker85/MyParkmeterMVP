package uk.co.ribot.androidboilerplate.injection.module;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import dagger.Module;
import dagger.Provides;
import uk.co.ribot.androidboilerplate.injection.ActivityContext;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Handler provideHandler() {
        return new Handler();
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}

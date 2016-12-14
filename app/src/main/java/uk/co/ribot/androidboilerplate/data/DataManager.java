package uk.co.ribot.androidboilerplate.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import rx.Observable;
import rx.functions.Func1;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.LoginResponse;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.remote.LoginService;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    private final LoginService mLoginService;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, LoginService LoginService) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        mLoginService = LoginService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Ribot> syncRibots() {
        return mRibotsService.getRibots()
                .concatMap(new Func1<List<Ribot>, Observable<Ribot>>() {
                    @Override
                    public Observable<Ribot> call(List<Ribot> ribots) {
                        return mDatabaseHelper.setRibots(ribots);
                    }
                });
    }

    public Observable<LoginResponse> doLogin(String email, String password, String userId) {
        return mLoginService.getLogin(email, password, userId);
    }

    public Call<LoginResponse> doLogin2(String email, String password, String userId) {
        return mLoginService.getLogin2(email, password, userId);
    }

    public Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }

}

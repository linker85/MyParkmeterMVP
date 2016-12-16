package uk.co.ribot.androidboilerplate.ui.base;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.injection.component.ConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.DaggerConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.FragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.FragmentModule;

/**
 * Created by linke_000 on 14/12/2016.
 */

public class BaseFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentComponent mFragmentComponent;
    private long mFragmentId;

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_FRAGMENT_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mFragmentId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(BoilerplateApplication.get(getActivity()).getComponent())
                    .build();
            sComponentsMap.put(mFragmentId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = sComponentsMap.get(mFragmentId);
        }
        mFragmentComponent = configPersistentComponent.fragmentComponent(new FragmentModule(this));
    }

    public FragmentComponent fragmentComponent() {
        return mFragmentComponent;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
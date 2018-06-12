package com.android.bitglobal.activity.asset;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bitglobal.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WithDrawFromScanActivityFragment extends Fragment {

    public WithDrawFromScanActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_with_draw_from_scan, container, false);
    }
}

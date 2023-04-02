package com.example.weather.permission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.weather.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocationPermissionRationaleDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "LocationPermRationaleDF";

    public static void possiblyShow(AppCompatActivity parent) {
        if (parent.getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            new LocationPermissionRationaleDialogFragment().showNow(parent.getSupportFragmentManager(), TAG);
        }
    }

    private OnLocationPermissionListener onLocationPermissionListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onLocationPermissionListener = (OnLocationPermissionListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_request_rationale, container, false);

        Button cancel = view.findViewById(R.id.cancel);
        Button grantPermission = view.findViewById(R.id.grant_permission);

        cancel.setOnClickListener(v -> dismissAllowingStateLoss());
        grantPermission.setOnClickListener(v -> {
            onLocationPermissionListener.grantLocationPermission();
            dismissAllowingStateLoss();
        });

        return view;
    }
}

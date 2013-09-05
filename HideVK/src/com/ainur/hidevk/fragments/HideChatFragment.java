package com.ainur.hidevk.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.app.SherlockFragment;
import com.ainur.hidevk.R;
import com.ainur.hidevk.camera.CameraPreview;
import com.ainur.hidevk.util.Log;

public class HideChatFragment extends SherlockFragment {
	private Camera mCamera;
	private CameraPreview mPreview;
	FrameLayout cameraFrame;

	@InjectView(R.id.message_edit_hide)
	EditText messageEdit;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (!checkCameraHardware(getSherlockActivity())) {
			showNoCameraDialog();
			getSherlockActivity().finish();
		}
		resetCamera();
	}

	private void resetCamera() {
		mCamera = getCameraInstance();
		if (mCamera == null) {
			getSherlockActivity().finish();
		}
		mPreview = new CameraPreview(getSherlockActivity(),
				getSherlockActivity(), mCamera);
		mPreview.setId(100);
		cameraFrame = (FrameLayout) getSherlockActivity().findViewById(
				R.id.cameraview);
		cameraFrame.removeAllViews();
		cameraFrame.addView(mPreview);
	}

	private void showNoCameraDialog() {
		Log.d("No camera");
	}

	// ** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.hide_chat_fragment, container,
				false);
		Views.inject(this, view);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}

	@Override
	public void onPause() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		Log.d("OnPAUSE");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d("super OnResume");
		// resetCamera();
		if (mCamera == null) {
			mCamera = getCameraInstance();
			mPreview.setCamera(mCamera);
		}
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public void onDestroyView() {
		Views.reset(this);
		super.onDestroyView();
	}

}
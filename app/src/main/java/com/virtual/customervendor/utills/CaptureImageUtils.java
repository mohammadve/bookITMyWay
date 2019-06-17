package com.virtual.customervendor.utills;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import com.virtual.customervendor.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CaptureImageUtils implements View.OnClickListener {

    public static int COMPRESS_RATIO = 80;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS_CAMERA = 1;
    private static final int REQUEST_CODE_ASK_PERMISSIONS_FOR_GALLERY = 2;

    private Activity mContext;
    private Fragment fragment;

    private Uri imgUri; // file url to store image/video
    private File imageFile;
    private String selectedPath = "";

    private ImageSelectionListener imageSelectionListener;
    private BottomSheetDialog mBottomSheetDialog;


    private String mRequestPermissionsCamera = "We are requesting the Camera as it is absolutely necessary for the app to perform it\'s functionality.                                            Please select \"Grant Permission\" to try again and \"Cancel \" to exit the application.";
    private String mRequestPermissionsGallery = "We are requesting the Gallery permission as it is absolutely necessary for the app to perform it\'s functionality.\nPlease select \"Grant Permission\" to try again and \"Cancel \" to exit the application.";
    private String mRequsetSettingsGallery = "You have rejected the Gallery permission for the application. As it is absolutely necessary for the app to perform you need to enable it in the settings of your device.\nPlease select \"Go to settings\" to go to application settings in your device and \"Cancel \" to exit the application.";
    private String mRequsetSettingsCamera = "You have rejected the Camera permission for the application. As it is absolutely necessary for the app to perform you need to enable it in the settings of your device.\nPlease select \"Go to settings\" to go to application settings in your device and \"Cancel \" to exit the application.";
    private String mGrantPermissions = "Grant Permissions";
    private String mCancel = "Cancel";
    private String mGoToSettings = "Go To Settings";
    private String mPermissionRejectWarning = "Cannot Proceed Without Permissions";

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 11;
    public static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 12;

    public CaptureImageUtils(Activity mContext, ImageSelectionListener listener) {
        this.mContext = mContext;
        this.imageSelectionListener = listener;
    }


    public void openSelectImageFromDialog() {
        mBottomSheetDialog = new BottomSheetDialog(mContext);
        View sheetView = mContext.getLayoutInflater().inflate(R.layout.dialog_camera_gallery, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        TextView tv_fromGallery = sheetView.findViewById(R.id.tv_galleryText);
        TextView tv_fromCamera = sheetView.findViewById(R.id.tv_cameraText);
        TextView tv_cancelDialog = sheetView.findViewById(R.id.tv_cancelText);

        tv_fromGallery.setOnClickListener(this);
        tv_fromCamera.setOnClickListener(this);
        tv_cancelDialog.setOnClickListener(this);
    }

    public CaptureImageUtils(Fragment fragment) {
        this.fragment = fragment;
        this.mContext = fragment.getActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_galleryText:
                mBottomSheetDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissionsForGallery(REQUEST_CODE_ASK_PERMISSIONS_FOR_GALLERY, mContext);
                } else {
                    galleryIntent();
                }

                break;
            case R.id.tv_cameraText:
                mBottomSheetDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkMultiplePermissionsForCamera(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS_CAMERA, mContext);
                } else {
                    if (isDeviceSupportCamera()) {
                        cameraIntent();
                    } else {
                        UiValidator.displayMsg(mContext, "Your device does not support camera");
                    }
                }
                break;
            case R.id.tv_cancelText:
                mBottomSheetDialog.dismiss();
                break;
        }
    }

    /**
     * Handles the result of events that the Activity or Fragment receives on its own
     * onActivityResult(). This method must be called inside the onActivityResult()
     * of the container Activity or Fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case GALLERY_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    imgUri = data.getData();
                    if (imgUri != null) {
                        File file = null;
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = mContext.getContentResolver().query(imgUri,
                                filePathColumn, null, null, null);


                        if (cursor != null && cursor.moveToFirst()) {
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            selectedPath = cursor.getString(columnIndex);
                            file = new File(selectedPath);
                            cursor.close();
                        } else {
                            selectedPath = imgUri.getPath();
                            file = new File(selectedPath);
                        }
//                        String key = preferenceManager.getStringPreference(mContext, PreferenceManager.KEY_USERNAME);
                        imageFile = file;
//                        preferenceManager.putStringPreference(mContext, key, imageFile.getAbsolutePath());
                        imageSelectionListener.capturedImage(imgUri, imageFile);


                    } else {
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                    UiValidator.displayMsg(mContext,"User cancelled image selection");
                } else {
                    // failed to capture image
                    UiValidator.displayMsg(mContext, "Sorry! Failed to select image");
                }

                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
//                    String key = preferenceManager.getStringPreference(mContext, PreferenceManager.KEY_USERNAME);
//                    preferenceManager.putStringPreference(mContext, key, imageFile.getAbsolutePath());
                    imageSelectionListener.capturedImage(imgUri, imageFile);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                    UiValidator.displayMsg(mContext, "User cancelled image capture");
                } else {
                    // failed to capture image
                    UiValidator.displayMsg(mContext, "Sorry! Failed to capture image");
                }

                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (isDeviceSupportCamera()) {
                        cameraIntent();
                    } else {
                        UiValidator.displayMsg(mContext, "Your device does not support camera");
                    }
                } else {
                    boolean showRationale1 = mContext.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                    boolean showRationale2 = mContext.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (showRationale1 && showRationale2) {
                        openAlertDialog(1, mRequestPermissionsCamera, mGrantPermissions, mCancel, mContext, true);
                    } else {
                        openAlertDialog(2, mRequsetSettingsCamera, mGoToSettings, mCancel, mContext, true);
                    }
                }
                break;
            case REQUEST_CODE_ASK_PERMISSIONS_FOR_GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    boolean showRationale = mContext.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (showRationale) {
                        openAlertDialog(1, mRequestPermissionsGallery, mGrantPermissions, mCancel, mContext, false);
                    } else {
                        openAlertDialog(2, mRequsetSettingsGallery, mGoToSettings, mCancel, mContext, false);
                    }
                }
                break;
        }
    }


    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissionsForCamera(int permissionCode, Context context) {

        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            if (isDeviceSupportCamera()) {
                cameraIntent();
            } else {
                UiValidator.displayMsg(mContext, "Your device does not support camera");
            }
        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionsForGallery(int permissionCode, Context context) {

        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            galleryIntent();
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void openAlertDialog(final int dialogType, String message, String positiveBtnText, String negativeBtnText,
                                final Context mContext, final boolean isFromCamera) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (dialogType == 1) {
                    checkMultiplePermissionsForCamera(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS_CAMERA, mContext);
                } else if (dialogType == 2) {
                    if (isFromCamera) {
                        redirectToAppSettings();
                    } else {
                        redirectToAppSettings();
                    }
                }
            }
        });
        builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setCancelable(false);
        builder.create().show();
    }

    private void redirectToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

    public void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            imageFile = createImageFile(mContext);
            imgUri = Uri.fromFile(imageFile);
            selectedPath = imageFile.getPath();
        } catch (IOException ex) {
            // Error occurred while creating the File
            UiValidator.displayMsg(mContext, "Sorry, There is some problem!");
        }
        //Continue only if the File was successfully created
        if (imageFile != null) {
            setFileProvider(intent, imageFile, mContext);
            mContext.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void setFileProvider(Intent intent, File imageFile, Activity mActivity) {
        Uri apkURI = FileProvider.getUriForFile(mActivity,mActivity.getApplicationContext()
                        .getPackageName() + ".provider", imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                apkURI);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    mActivity.getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mActivity.grantUriPermission(packageName, apkURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    public static File createImageFile(Context context) throws IOException {
        String imageFileName = "img_";
        String state = Environment.getExternalStorageState();
        File storageDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            storageDir = Environment.getExternalStorageDirectory();
        } else {
            storageDir = context.getCacheDir();
        }
        storageDir.mkdirs();
        File appFile = new File(storageDir, context.getString(R.string.app_name));
        appFile.mkdir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                appFile      /* directory */
        );
        return image;

    }

    /**
     * Check Camera Availability
     *
     * @return
     */

    public boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /*private void redirectToAppGallerySetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }*/


    /**
     * Listener interface for receiving callbacks from the CaptureImageUtils.
     */
    public interface ImageSelectionListener {
        void capturedImage(Uri uri, File imageFile);
    }

    /**
     * The purpose of this method is use to convert Image Path to Byte Array
     *
     * @param source
     * @return
     */
    public String convertBase64(String source) {
        try {
            Bitmap bm = BitmapFactory.decodeFile(source);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, COMPRESS_RATIO, baos);
            return Base64.encodeToString(baos.toByteArray(), 2).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
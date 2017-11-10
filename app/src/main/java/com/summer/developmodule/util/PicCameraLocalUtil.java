package com.summer.developmodule.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;


import com.summer.developmodule.base.BaseFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.summer.developmodule.util.PhotoBitmapUtils.readPictureDegree;
import static com.summer.developmodule.util.PhotoBitmapUtils.rotaingImageView;

public class PicCameraLocalUtil {
    public static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 选择文件
     */
    public static void selectFileFromLocal(Context context) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        ((Activity) context).startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 发�?文件
     *
     * @param uri
     */
    public static File getFile(Context context, Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            ToastUtil.makeShortText(context, "文件不存");
            return null;
        }
        if (file.length() > 10 * 1024 * 1024) {
            ToastUtil.makeShortText(context, "文件太大");
            return null;
        }
        return file;

    }

    /**
     * 根据图库图片uri发�?图片
     *
     * @param selectedImage
     */
    public static String getPicByUri(Context context, Uri selectedImage) {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        String path = "";
        Cursor cursor = context.getContentResolver().query(selectedImage, null,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
            if (picturePath == null || picturePath.equals("null")) {
                ToastUtil.makeShortText(context, "没有找到照片");
                picturePath = "";
            }
            path = picturePath;
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                ToastUtil.makeShortText(context, "没有找到照片");
            }
            path = file.getAbsolutePath();
        }
        return path;

    }

    /**
     * 从图库获取图片
     */
    public static void selectPicFromLocal(Fragment f) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        f.startActivityForResult(intent, Constant.REQUEST_CODE_LOCAL);
    }

    /**
     * 照相获取图片
     */
    public static File selectPicFromCamera(Activity activity, File cameraFile) {

        if (!AppUtil.hasSDCard()) {
            ToastUtil.makeShortText(activity, "请检查存储设备");
            return null;
        }
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        if (cameraFile == null) {
            cameraFile = new File(imagePath, +System.currentTimeMillis() + ".jpg");
        }
        Uri contentUri = FileProvider7.getUriForFile(activity, cameraFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            FileProvider7.grantPermissions2(activity, intent, contentUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            activity.startActivityForResult(intent, Constant.REQUEST_CODE_CAMERA);
        }
        return cameraFile;
    }

    /**
     * 照相获取图片
     */
    public static File selectPicFromCamera(BaseFragment activity, File cameraFile) {
        if (!AppUtil.hasSDCard()) {
            ToastUtil.makeShortText(activity.getActivity(), "请检查存储设备");
            return null;
        }
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        if (cameraFile == null) {
            cameraFile = new File(imagePath, +System.currentTimeMillis() + ".jpg");
        }
        Uri contentUri = FileProvider7.getUriForFile(activity.getActivity(), cameraFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getActivity().getPackageManager()) != null) {
            FileProvider7.grantPermissions2(activity.getActivity(), intent, contentUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            activity.startActivityForResult(intent, Constant.REQUEST_CODE_CAMERA);
        }
        return cameraFile;
    }


    /**
     * 从图库获取图片
     */
    public static void selectPicFromLocal(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, Constant.REQUEST_CODE_LOCAL);
    }

    /**
     * 系统页面来裁剪传进来的照片
     */
    public static void startPhotoZoom(Uri uri, Fragment fragment) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽�?
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);
        fragment.startActivityForResult(intent, Constant.PHOTORESOULT);
    }

    /**
     * 压缩图片
     *
     * @param path
     * @param context
     * @return
     * @throws IOException
     */
    public static File revitionImageSize(String path, Context context)
            throws IOException {
        int degree = readPictureDegree(path);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1080) && (options.outHeight >> i <= 1920)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return saveBitmap(context, rotaingImageView(degree, bitmap));
    }

    /**
     * 保存图片到指定目录
     *
     * @param mBitmap
     */
    public static File saveBitmap(Context context, Bitmap mBitmap) {
        Date mDate = new Date();
        String imgName = Long.toString(mDate.getTime()) + ".jpg";
        File mFile = new File(AppUtil.getAppDir(context) + "/image/");
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(mFile.getAbsolutePath() + "/" + imgName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mFile = new File(mFile.getAbsolutePath() + "/" + imgName);
        return mFile;
    }

    /**
     * 保存图片到指定目录
     *
     * @param mBitmap
     */
    public static File saveBitmap(Context context, Bitmap mBitmap, String name) {
        // File mFile = new File(Environment.getRootDirectory()+"/oAimages");
        String imgName = name + ".jpg";
        File mFile = new File(AppUtil.getAppDir(context) + "/image/");
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(mFile.getAbsolutePath()
                    + "/" + imgName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(mFile.getAbsolutePath() + "/" + imgName);

    }


}

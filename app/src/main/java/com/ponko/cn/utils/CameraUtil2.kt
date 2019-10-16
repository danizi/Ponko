package com.ponko.cn.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.ponko.cn.app.PonkoApp
import com.soundcloud.android.crop.Crop
import com.soundcloud.android.crop.Crop.getOutput
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.log.BKLog
import java.io.File

object CameraUtil2 {
    private const val CAMERA_RESULT_CODE = 100//拍摄
    private const val ALBUM_RESULT_CODE = 200//相册
    private const val CROP_RESULT_CODE = 3//裁剪
    /**
     * 相机照片存储位置
     */
    private var cameraUri: Uri? = null

    /**
     * 是否裁剪
     */
    private var isCrop = true

    /**
     * 显示选择弹框
     */
    @SuppressLint("CheckResult")
    fun showSelectDlg(context: Context) {
        RxPermissions(context as AppCompatActivity)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean!!) {
                        AlertDialog.Builder(context).setItems(arrayOf("相册", "相机")) { dialog, which ->
                            when (which) {
                                0 -> {
                                    BKLog.d("点击相册")
                                    openSysAlbum(context)
                                }
                                1 -> {
                                    BKLog.d("点击相机")
                                    openSysCamera(context)
                                }
                            }
                        }.show()
                    } else {
                        //只要有一个权限禁止，返回false，
                        //下一次申请只申请没通过申请的权限
                        Log.e("permissions", "申请权限相机权限失败：$aBoolean")
                    }
                }
    }

    /**
     * 打开系统相册
     */
    private fun openSysCamera(act: Activity?) {
        cameraUri = ImageUri.createImageUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        act?.startActivityForResult(intent, CAMERA_RESULT_CODE)
    }

    /**
     * 打开相册
     */
    private fun openSysAlbum(act: Activity?) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        act?.startActivityForResult(intent, ALBUM_RESULT_CODE)
    }

    /**
     * 回调处理
     */
    fun onActivityResult(act: Activity?, requestCode: Int, resultCode: Int, data: Intent?, listener: OnCameraListener?) {
        when (requestCode) {
            CameraUtil2.ALBUM_RESULT_CODE -> {
                BKLog.d("相册结果")
                if (data != null && data.data != null) {
                    if (isCrop) {
                        beginCrop(act, data.data!!)
                    } else {
                        handlePic(listener, act, resultCode, data)
                    }
                    listener?.onAlbum()
                }
            }

            CameraUtil2.CAMERA_RESULT_CODE -> {
                BKLog.d("拍摄结果")
                if (isCrop) {
                    beginCrop(act, cameraUri!!)
                } else {
                    handlePic(listener, act, resultCode, data!!)
                }

                listener?.onCamera()
            }

            Crop.REQUEST_CROP -> {
                BKLog.d("裁剪结果")
                if (data != null) {
                    handleCrop(listener, act, resultCode, data)
                }
            }
            Crop.REQUEST_PICK -> {
                BKLog.d("获取结果")
//                if (data != null) {
//                    handlePic(listener, act, resultCode, data)
//                }
            }
        }
    }

    private fun handleCrop(listener: OnCameraListener?, act: Activity?, resultCode: Int, result: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val filePath = ImageUri.getPicturePath(act!!, getOutput(result))
            val bitmap = BitmapFactory.decodeFile(filePath)
            BKLog.d("handleCrop filePath:$filePath bitmap$bitmap")
            listener?.onResult(Crop.REQUEST_CROP, filePath, bitmap)
        } else if (resultCode == Crop.RESULT_ERROR) {
            ToastUtil.show(Crop.getError(result).message)
        }
    }

    private fun handlePic(listener: OnCameraListener?, act: Activity?, resultCode: Int, result: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val filePath = ImageUri.getPicturePath(act!!, getOutput(result))
            val bitmap = BitmapFactory.decodeFile(filePath)
            BKLog.d("handlePic filePath:$filePath bitmap$bitmap")
            listener?.onResult(Crop.REQUEST_PICK, filePath, bitmap)
        } else if (resultCode == Crop.RESULT_ERROR) {
            ToastUtil.show(Crop.getError(result).message)
        }
    }

    /**
     * 裁剪后处理
     */
    private fun beginCrop(act: Activity?, source: Uri) {
        val destination = Uri.fromFile(File(act?.cacheDir, "ponko2"))
        Crop.of(source, destination).withAspect(100, 100).start(act)
    }
}

/**
 * 相机相册选取回调
 */
interface OnCameraListener {
    /**
     * 选取相册成功
     */
    fun onAlbum()

    /**
     * 拍照成功
     */
    fun onCamera()

    /**
     * 裁剪成功
     */
    fun onResult(type: Int, filePath: String, bmp: Bitmap?)

}

/**
 * 相册文件操作工具类
 */
object ImageUri {

    /**
     * @param uri 获取图片路径
     */
    fun getPicturePath(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = PonkoApp.app?.contentResolver?.query(uri,
                filePathColumn, null, null, null)
        return if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            picturePath
        } else {
            uri.path
        }
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage 图片图库Url地址
     */
    fun getPicturePath(context: Context, selectedImage: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(selectedImage, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            if (picturePath == null || picturePath == "null") {
                val toast = Toast.makeText(context, "找不到指定的图片", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                return ""
            }
            return picturePath
        } else {
            val file = File(selectedImage.path)
            if (!file.exists()) {
                val toast = Toast.makeText(context, "找不到指定的图片", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                return ""

            }
            return file.absolutePath
        }
    }


    fun createImageUri(): Uri {
        val name = System.currentTimeMillis().toString()
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, name)
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "$name.jpeg")
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        return PonkoApp.app?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }
}
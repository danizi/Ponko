package com.ponko.cn.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.log.BKLog
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 相机相册图片选择工具类 https://blog.csdn.net/wufeng55/article/details/80918749
 */
@Deprecated("9.0会有问题")
object CameraUtil {
    /**
     * 打开系统相机
     */
    const val CAMERA_RESULT_CODE = 1//拍摄
    const val ALBUM_RESULT_CODE = 2//相册
    const val CROP_RESULT_CODE = 3//裁剪
    /**
     * 打开相册存储的路径
     */
    var imgName = ""
    var file: File? = null

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
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(Environment.getExternalStorageDirectory(), imgName)))
//        act?.startActivityForResult(cameraIntent, CAMERA_RESULT_CODE)

        //适配android7.0之后的
        file = createOriImageFile(act)
        var imgUriOri: Uri? = null
        if (file != null) {
            imgUriOri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(act!!, act.packageName + ".provider", file!!)
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri)
            act?.startActivityForResult(cameraIntent, CAMERA_RESULT_CODE)
        }
    }

    /**
     * 创建原图像保存的文件
     *
     * @return
     * @throws IOException
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createOriImageFile(activity: Activity?): File {
        val imgNameOri = "HomePic_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val pictureDirOri = File(activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/OriPicture")
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs()
        }
        val image = File.createTempFile(imgNameOri, /* prefix */".jpg", /* suffix */pictureDirOri /* directory */
        )
        imgName = image.absolutePath
        return image
    }

    /**
     * 打开相册
     */
    private fun openSysAlbum(act: Activity?) {
        val albumIntent = Intent(Intent.ACTION_PICK)
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        act?.startActivityForResult(albumIntent, ALBUM_RESULT_CODE)
    }

    /**
     * 相片裁剪
     */
    private fun cropPic(act: Activity?, data: Uri?) {
        if (data == null) {
            return
        }
        val cropIntent = Intent("com.android.camera.action.CROP")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        cropIntent.setDataAndType(data, "image/*")

        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true")
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 320)
        cropIntent.putExtra("outputY", 320)
        cropIntent.putExtra("scale", true)
        /**
         * return-data
         * 这个属性决定我们在 onActivityResult 中接收到的是什么数据，
         * 如果设置为true 那么data将会返回一个bitmap
         * 如果设置为false，则会将图片保存到本地并将对应的uri返回，当然这个uri得有我们自己设定。
         * 系统裁剪完成后将会将裁剪完成的图片保存在我们所这设定这个uri地址上。我们只需要在裁剪完成后直接调用该uri来设置图片，就可以了。
         */
        //cropIntent.putExtra("return-data", true)
        // 当 return-data 为 false 的时候需要设置这句
        //cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 图片输出格式
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
        //        cropIntent.putExtra("noFaceDetection", true);
        cropIntent.putExtra("return-data", false)
        act?.startActivityForResult(cropIntent, CROP_RESULT_CODE)
    }


    /**
     * 回调处理
     */
    fun onActivityResult(act: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CameraUtil.CAMERA_RESULT_CODE -> {
                BKLog.d("拍摄结果")
//                val tempFile = File(Environment.getExternalStorageDirectory(), CameraUtil.imgName)
//                cropPic(act, Uri.fromFile(tempFile))

                //裁剪有误
                cropPic(act, getImageContentUri(act, file))
            }
            CameraUtil.ALBUM_RESULT_CODE -> {
                BKLog.d("相册结果")
                cropPic(act, data?.data)
            }
            CameraUtil.CROP_RESULT_CODE -> {
                BKLog.d("裁剪结果")
                // 裁剪时,这样设置 cropIntent.putExtra("return-data", true); 处理方案如下
                if (data != null) {
                    val bundle = data.extras
                    if (bundle != null) {
                        val bitmap = bundle.getParcelable<Bitmap>("data")
                        // 把裁剪后的图片保存至本地 返回路径
                        val urlpath = FileUtilcll.saveFile(act!!, "crop.jpg", bitmap!!)
                        BKLog.d("裁剪图片地址->$urlpath")
                    }
                } else {
                    BKLog.e("裁剪结果:data is null")
                    //裁剪时,这样设置 cropIntent.putExtra("return-data", false); 处理方案如下
//                    try {
//                        ivHead.setImageBitmap(BitmapFactory.decodeStream(act?.contentResolver?.openInputStream(imageUri)))
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    }
                }
            }
        }
    }

    private fun getImageContentUri(act: Activity?, imageFile: File?): Uri? {
        val filePath = imageFile?.absolutePath
        val cursor = act?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile?.exists()!!) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return act?.contentResolver?.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                return null
            }
        }
    }

}

/**
 * 图片文件操作
 */
object FileUtilcll {

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param fileName 文件名称
     * @param bitmap   图片
     * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
     */
    fun saveFile(c: Context, fileName: String, bitmap: Bitmap): String {
        return saveFile(c, "", fileName, bitmap)
    }

    fun saveFile(c: Context, filePath: String, fileName: String, bitmap: Bitmap): String {
        val bytes = bitmapToBytes(bitmap)
        return saveFile(c, filePath, fileName, bytes)
    }

    /**
     * Bitmap 转 字节数组
     *
     * @param bm
     * @return
     */
    fun bitmapToBytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    fun saveFile(c: Context, filePath: String?, fileName: String, bytes: ByteArray): String {
        var filePath = filePath
        var fileFullName = ""
        var fos: FileOutputStream? = null
        val dateFolder = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(Date())
        try {
            val suffix = ""
            if (filePath == null || filePath.trim { it <= ' ' }.isEmpty()) {
                filePath = Environment.getExternalStorageDirectory().toString() + "/cxs/" + dateFolder + "/"
            }
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val fullFile = File(filePath, fileName + suffix)
            fileFullName = fullFile.path
            fos = FileOutputStream(File(filePath, fileName + suffix))
            fos.write(bytes)
        } catch (e: Exception) {
            fileFullName = ""
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    fileFullName = ""
                }
            }
        }
        return fileFullName
    }
}

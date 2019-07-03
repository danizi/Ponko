package com.ponko.cn.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.widget.ImageView

import com.bumptech.glide.request.target.ImageViewTarget

internal class TransformationUtil(private val target: ImageView) : ImageViewTarget<Bitmap>(target) {

    override fun setResource(resource: Bitmap) {
        s(resource)
    }

    private fun s(resource: Bitmap){
        view.setImageBitmap(resource)

        //获取原图的宽高
        val width = resource.width
        val height = resource.height

        //获取imageView的宽
        val imageViewWidth = target.width

        //计算缩放比例
        val sy = (imageViewWidth * 0.1).toFloat() / (width * 0.1).toFloat()

        //计算图片等比例放大后的高
        val imageViewHeight = (height * sy).toInt()
        val params = target.layoutParams
        params.height = imageViewHeight
        target.layoutParams = params
    }

    fun sy(resource: Bitmap):Float{
        val width = resource.width
        val height = resource.height
        val imageViewWidth = target.width
        return (imageViewWidth * 0.1).toFloat() / (width * 0.1).toFloat()
    }

    fun getNewBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高.
        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例.
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数.
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

}


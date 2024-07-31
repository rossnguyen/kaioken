package agv.kaak.vn.kaioken.helper

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import android.media.ExifInterface
import android.graphics.BitmapFactory
import android.widget.ImageButton
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.URL
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


class ImageHelper {
    companion object {
        fun loadImage(context: Context, view: ImageView?, linkImage: String?, placeHolderType: PlaceHolderType) {
            /*val placeHolders:ArrayList<Int> = arrayListOf(
                    R.drawable.place_holder_1,
                    R.drawable.place_holder_2,
                    R.drawable.place_holder_4,
                    R.drawable.place_holder_4,
                    R.drawable.place_holder_2,
                    R.drawable.place_holder_6,
                    R.drawable.place_holder_6,
                    R.drawable.place_holder_8,
                    R.drawable.place_holder_9,
                    R.drawable.place_holder_9)

            val random=Random().nextInt(10)*/
            if (linkImage == null)
                return

            if (view == null)
                return

            if (linkImage == null)
                return

            when (placeHolderType) {
                PlaceHolderType.FOOD -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.IMAGE -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.RESTAURANT -> Glide.with(context)
                        .load(URL(linkImage))
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.AVATAR -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_error)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.CATEGORY -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.NONE -> Glide.with(context)
                        .load(linkImage)
                        .into(view)
            }

        }

        fun loadImage(context: Context, view: ImageButton?, linkImage: String?, placeHolderType: PlaceHolderType) {
            /*val placeHolders:ArrayList<Int> = arrayListOf(
                    R.drawable.place_holder_1,
                    R.drawable.place_holder_2,
                    R.drawable.place_holder_4,
                    R.drawable.place_holder_4,
                    R.drawable.place_holder_2,
                    R.drawable.place_holder_6,
                    R.drawable.place_holder_6,
                    R.drawable.place_holder_8,
                    R.drawable.place_holder_9,
                    R.drawable.place_holder_9)

            val random=Random().nextInt(10)*/
            if (linkImage == null)
                return

            if (view == null)
                return

            if (linkImage == null)
                return

            when (placeHolderType) {
                PlaceHolderType.FOOD -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.IMAGE -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.RESTAURANT -> Glide.with(context)
                        .load(URL(linkImage))
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.AVATAR -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_error)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.CATEGORY -> Glide.with(context)
                        .load(linkImage)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.place_holder_4)
                                .error(R.drawable.image_error))
                        .into(view)

                PlaceHolderType.NONE -> Glide.with(context)
                        .load(linkImage)
                        .into(view)
            }

        }

        fun loadImageWithCorner(context: Context, view: ImageView?, linkImage: String?, dp: Int, placeHolderType: PlaceHolderType) {
            if (linkImage == null)
                return
            when (placeHolderType) {
                PlaceHolderType.FOOD -> Glide.with(context).asBitmap().load(linkImage).apply(RequestOptions()
                        .placeholder(R.drawable.place_holder_4).error(R.drawable.image_error))
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                view?.setImageBitmap(getRoundedCornerBitmap(resource!!, ConvertHelper.dpToPx(context, dp)))
                            }
                        })

                PlaceHolderType.IMAGE -> Glide.with(context).asBitmap().load(linkImage).apply(RequestOptions()
                        .placeholder(R.drawable.ic_picture).error(R.drawable.image_error))
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                view?.setImageBitmap(getRoundedCornerBitmap(resource!!, ConvertHelper.dpToPx(context, dp)))
                            }
                        })

                PlaceHolderType.RESTAURANT -> Glide.with(context).asBitmap().load(linkImage).apply(RequestOptions()
                        .placeholder(R.drawable.place_holder_4).error(R.drawable.image_error))
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                view?.setImageBitmap(getRoundedCornerBitmap(resource!!, ConvertHelper.dpToPx(context, dp)))
                            }
                        })

                PlaceHolderType.AVATAR -> Glide.with(context).asBitmap().load(linkImage).apply(RequestOptions()
                        .placeholder(R.drawable.image_error).error(R.drawable.image_error))
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                view?.setImageBitmap(getRoundedCornerBitmap(resource!!, ConvertHelper.dpToPx(context, dp)))
                            }
                        })
            }
        }

        fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width, bitmap
                    .height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = pixels.toFloat()

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }

        fun getBipmapFromLink(mContext: Context, link: String): Bitmap? {
            var result: Bitmap? = null
            Glide.with(mContext).asBitmap().load(link).apply(RequestOptions()
                    .placeholder(R.drawable.place_holder_4).error(R.drawable.image_error))
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                            result = resource
                        }
                    })
            return result
        }


        //this is the byte stream that I upload.
        fun getStreamByteFromImage(imageFile: File): ByteArray {

            var photoBitmap = BitmapFactory.decodeFile(imageFile.getPath())
            val stream = ByteArrayOutputStream()

            val imageRotation = getImageRotation(imageFile)

            if (imageRotation != 0)
                photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation)

            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)

            return stream.toByteArray()
        }


        private fun getImageRotation(imageFile: File): Int {

            var exif: ExifInterface? = null
            var exifRotation = 0

            try {
                exif = ExifInterface(imageFile.path)
                exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return if (exif == null)
                0
            else
                exifToDegrees(exifRotation)
        }

        private fun exifToDegrees(rotation: Int): Int {
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
                return 90
            else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
                return 180
            else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
                return 270

            return 0
        }

        private fun getBitmapRotatedByDegree(bitmap: Bitmap, rotationDegree: Int): Bitmap {
            val matrix = android.graphics.Matrix()
            matrix.preRotate(rotationDegree.toFloat())

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun resize(mContext: Context, image: Drawable, newWidth: Int, newHeight: Int): Drawable {
            val b = (image as BitmapDrawable).bitmap
            val bitmapResized = Bitmap.createScaledBitmap(b, newWidth, newHeight, false)
            return BitmapDrawable(mContext.resources, bitmapResized)
        }

        fun scaleImage(mContext: Context, image: Drawable?, scaleFactor: Float): Drawable? {
            var mImage = image

            if (mImage == null || mImage !is BitmapDrawable) {
                return mImage
            }

            val b = mImage.bitmap

            val sizeX = Math.round(mImage.intrinsicWidth * scaleFactor)
            val sizeY = Math.round(mImage.intrinsicHeight * scaleFactor)

            val bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false)

            mImage = BitmapDrawable(mContext.resources, bitmapResized)

            return mImage

        }
    }
}
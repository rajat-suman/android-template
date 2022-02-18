package com.template.imagevideopicker
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaModel
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.template.R
import com.template.databinding.ItemChooserBinding
import com.template.utils.showNegativeAlerter
import com.template.videotrimmer.TrimmerActivity
import com.template.videotrimmer.utils.FileUtils.getPath
import java.io.File


class CameraBottomSheet(
    private val galleryResult: ActivityResultLauncher<Intent>,
    private val cameraResult: ActivityResultLauncher<Intent>,
    private val activity: Fragment,
    private val isVideo:Boolean= false,
    private val videoCameraResultLauncher: ActivityResultLauncher<Intent>?=null,
    private val videoTrimmerResultLauncher: ActivityResultLauncher<Intent>?=null,
    private val onImageSelected: OnImageSelected?=null
) : BottomSheetDialogFragment() {

    private lateinit var cameraSheetBinding: ItemChooserBinding


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        cameraSheetBinding = ItemChooserBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(cameraSheetBinding.root)
        dialog.setCancelable(true)

        cameraSheetBinding.tvVideo.isVisible= isVideo
        cameraSheetBinding.tvImage.setOnClickListener {
            takeImage(activity, cameraResult, dialog)
        }
        cameraSheetBinding.tvVideo.setOnClickListener {
            videoCameraResultLauncher?.let {
                takeImage(activity, videoCameraResultLauncher, dialog, isVideo = true)
            }
        }

        cameraSheetBinding.tvGallery.setOnClickListener {
            selectImage(activity, galleryResult, dialog)
        }

        (cameraSheetBinding.root.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }


    private fun takeImage(
        activity: Fragment,
        cameraResult: ActivityResultLauncher<Intent>,
        dialog: Dialog,
        isVideo:Boolean= false
    ) {
        PermissionsUtil.storagePermission { permissionGranted ->
            if (permissionGranted) {
                try {
                  dismiss()
                    if(isVideo){
                        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
                        takeVideoIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/" + activity.requireContext()
                                .getString(R.string.app_name) + "/${System.currentTimeMillis()}.mp4"
                        )
                        cameraResult.launch(takeVideoIntent)
                    }else {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraResult.launch(takePicture)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                PermissionsUtil.permissionAlert(cameraPermission = true) { canRequest ->
                    if (canRequest)
                        takeImage(activity, cameraResult, dialog, isVideo)
                    else
                       dismiss()
                }
            }
        }
    }


    private fun selectImage(
        activity: Fragment,
        galleryResult: ActivityResultLauncher<Intent>,
        dialog: Dialog
    ) {
        PermissionsUtil.storagePermission { permissionGranted ->
            if (permissionGranted) {
                try {
                   dismiss()
                    if(isVideo){
                        UwMediaPicker
                            .with(activity)
                            .setGalleryMode(UwMediaPicker.GalleryMode.ImageAndVideoGallery)
                            .setGridColumnCount(3)
                            .setMaxSelectableMediaCount(1)
                            .setLightStatusBar(true)
                            .enableImageCompression(true)
                            .setCompressionMaxWidth(1280F)
                            .setCompressionMaxHeight(720F)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setCompressionQuality(85)
                            .setCompressedFileDestinationPath(
                                "${
                                    activity.requireActivity().application.getExternalFilesDir(
                                        null
                                    )?.path
                                }/Pictures"
                            )
                            .launch {
                                onMediaSelected(it)
                            }

                    }else {
                        val pickPhoto =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickPhoto.type = "image/*"
                        galleryResult.launch(pickPhoto)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                PermissionsUtil.permissionAlert(storagePermission = true) { canRequest ->
                    if (canRequest)
                        selectImage(activity, galleryResult, dialog)
                    else
                      dismiss()
                }
            }
        }
    }

    private fun onMediaSelected(selectedMediaList: List<UwMediaPickerMediaModel>?) {
        if (!selectedMediaList.isNullOrEmpty()) {
            if (selectedMediaList[0].mediaType == UwMediaPickerMediaType.VIDEO) {
                val intent = Intent(activity.requireContext(), TrimmerActivity::class.java)
                intent.putExtra(
                    TrimmerActivity.EXTRA_VIDEO_PATH,
                    getPath(activity.requireContext(), Uri.fromFile(File(selectedMediaList[0].mediaPath)))
                )
                intent.putExtra(
                    TrimmerActivity.VIDEO_TOTAL_DURATION,
                    getMediaDuration(Uri.fromFile(File(selectedMediaList[0].mediaPath)))
                )
                videoTrimmerResultLauncher?.launch(intent)
            } else {
                onImageSelected?.onImageSelected(selectedMediaList[0].mediaPath)
            }
        } else
            requireActivity().showNegativeAlerter(message =  activity.requireContext().getString(R.string.unexpected_error))

    }

    private fun getMediaDuration(uriOfFile: Uri): Int {
        val mp = MediaPlayer.create(activity.requireContext(), uriOfFile)
        return mp.duration
    }
    
    interface OnImageSelected{
        fun onImageSelected(string: String)
    }

}
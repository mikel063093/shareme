package co.mike.shareme


import android.app.Activity
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import co.mike.shareme.base.BaseActivity
import co.mike.shareme.databinding.ActivityCreateProfileBinding
import co.mike.shareme.util.Logger
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData
import com.miguelbcr.ui.rx_paparazzo2.entities.size.SmallSize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class Create_profile : BaseActivity() {

  lateinit var binding: ActivityCreateProfileBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Logger.log("CreateProfile", "onCrate")
    binding = DataBindingUtil.setContentView(this, R.layout.activity_create_profile)
    //binding.btnNext.setOnClickListener({ loadPhoto() })
    binding.imgPicture.setOnClickListener({ loadPhoto() })
  }

  private fun loadPhoto() {
    RxPaparazzo.single(this)
        .size(SmallSize())
        .usingGallery()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ response ->
          if (Activity.RESULT_OK != response.resultCode()) {

            Logger.log("load image cancel")
            return@subscribe
          }
          Logger.log("load image ok")
          response.targetUI().loadImage(response.data())
        })
  }

  private fun loadImage(file: FileData) {
    val uri = Uri.fromFile(file.file)
    binding.imgPicture.setImageURI(uri)
  }
}

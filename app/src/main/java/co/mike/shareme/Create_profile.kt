package co.mike.shareme


import android.Manifest
import android.app.Activity
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import co.mike.shareme.base.BaseActivity
import co.mike.shareme.databinding.ActivityCreateProfileBinding
import co.mike.shareme.util.Logger
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData
import com.miguelbcr.ui.rx_paparazzo2.entities.size.SmallSize
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.mirrajabi.rxcontacts.RxContacts


class Create_profile : BaseActivity() {

  lateinit var binding: ActivityCreateProfileBinding
  var localImage: FileData? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Logger.log("CreateProfile", "onCrate")
    binding = DataBindingUtil.setContentView(this, R.layout.activity_create_profile)
    binding.btnNext.setOnClickListener(
        {
          Logger.log("click Next with image ${localImage?.filename}, name ${binding.edtName.text}")
        })
    binding.imgPicture.setOnClickListener({ loadPhoto() })
    binding.edtName.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(txt: Editable?) {
      }

      override fun beforeTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
      }

      override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.isLoading = txt?.length!! >= 3
        Logger.log("onTextChanged $txt")
      }
    })

    val rxPermissions = RxPermissions(this)
    rxPermissions
        .request(Manifest.permission.READ_CONTACTS)
        .subscribe({ granted ->
          if (granted) {
            RxContacts.fetch(this)
                .filter { m -> m.inVisibleGroup == 1 }
                .toSortedList { obj, other -> obj.compareTo(other) }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { contacts ->
                  val json = Gson()

                  Logger.log("contacts", json.toJson(contacts))
                }
          } else {
            Logger.log("permision READ_CONTACTS Nop")
          }
        })


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
    localImage = file
    val uri = Uri.fromFile(file.file)
    binding.imgPicture.setImageURI(uri)
  }
}

package co.mike.shareme

import android.app.Application
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo


/**
 * Created by home on 5/29/17.
 */
class AppMain : Application() {
  override fun onCreate() {
    super.onCreate()
    RxPaparazzo.register(this)

  }

}
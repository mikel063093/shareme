package co.mike.shareme

import android.app.Application
import butterknife.ButterKnife

/**
 * Created by home on 5/29/17.
 */
class AppMain : Application() {
  override fun onCreate() {
    super.onCreate()
    ButterKnife.setDebug(BuildConfig.DEBUG)
  }

}
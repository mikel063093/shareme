package co.mike.shareme.base

import android.content.Intent
import android.support.v7.app.AppCompatActivity


/**
 * Created by home on 6/9/17.
 */
open class BaseActivity : AppCompatActivity() {


  fun goActv(cls: Class<*>, clear: Boolean) {
    val intent = Intent(applicationContext, cls)
    if (clear) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)



  }

  fun goActv(intent: Intent, clear: Boolean) {
    if (clear) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)

  }

}
package co.mike.shareme.util

import android.util.Log
import co.mike.shareme.BuildConfig

/**
 * Created by home on 5/29/17.
 */
class Logger {
  companion object {
    private val TAG = Logger::class.java.simpleName
    fun log(TAG: String, msg: String) {
      if (BuildConfig.DEBUG) {
        Log.e(TAG, msg)
      }
    }

    fun log(TAG: String, msg: String, e: Throwable) {
      if (BuildConfig.DEBUG) {
        Log.e(TAG, msg, e)
      }
    }

    fun log(msg: String) {
      if (BuildConfig.DEBUG) {
        Log.e(TAG, msg)
      }
    }

    fun log(msg: String, e: Throwable) {
      if (BuildConfig.DEBUG) {
        Log.e(TAG, msg, e)
      }
    }
  }


}
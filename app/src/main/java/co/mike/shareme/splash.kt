package co.mike.shareme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

import co.mike.shareme.base.BaseActivity
import co.mike.shareme.util.Logger
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType


class splash : BaseActivity() {
  var APP_REQUEST_CODE = 99


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    val btnRegister = findViewById<Button>(R.id.btn_register)
    Logger.log("Splash", btnRegister.text.toString())
    //checkAccesToken(false)
    btnRegister.setOnClickListener({ onViewClicked() })
  }

  fun onViewClicked() {
    Logger.log("btn_click")
    checkAccesToken(true)
  }

  fun checkAccesToken(requestAcces: Boolean) {
    val accessToken = AccountKit.getCurrentAccessToken()

    if (accessToken != null) {
      Logger.log("Splash ", accessToken.accountId)
      goToMyLoggedInActivity()
    } else {
      Logger.log("Splash", "no acces token")
      if (requestAcces)
        phoneLogin()
    }
  }

  fun phoneLogin() {
    val intent = Intent(this, AccountKitActivity::class.java)
    val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
        LoginType.PHONE,
        AccountKitActivity.ResponseType.TOKEN)
    intent.putExtra(
        AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
        configurationBuilder.build())
    startActivityForResult(intent, APP_REQUEST_CODE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == APP_REQUEST_CODE) {
      val loginResult = data.getParcelableExtra<AccountKitLoginResult>(
          AccountKitLoginResult.RESULT_KEY)
      val toastMessage: String
      if (loginResult.error != null) {
        toastMessage = loginResult.error!!.errorType.message
        showErrorActivity(loginResult.error!!)
      } else if (loginResult.wasCancelled()) {
        toastMessage = "Login Cancelled"
      } else {
        if (loginResult.accessToken != null) {
          toastMessage = "Success:" + loginResult.accessToken!!.accountId
        } else {
          toastMessage = String.format(
              "Success:%s...",
              loginResult.authorizationCode!!.substring(0, 10))
        }

        goToMyLoggedInActivity()
      }

      Toast.makeText(
          this,
          toastMessage,
          Toast.LENGTH_LONG)
          .show()
    }
  }

  private fun goToMyLoggedInActivity() {
    AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
      override fun onSuccess(account: Account) {
        val phoneNumber = account.phoneNumber
        Logger.log("phoneNumber: $phoneNumber")
        goActv(Create_profile::class.java, true)
      }

      override fun onError(error: AccountKitError) {
        Logger.log("onErr ${error.userFacingMessage}")
      }
    })
  }

  private fun showErrorActivity(error: AccountKitError) {
    Logger.log(error.userFacingMessage)
  }
}
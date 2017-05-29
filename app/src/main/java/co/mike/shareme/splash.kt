package co.mike.shareme

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import co.mike.shareme.util.Logger
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType


class splash : AppCompatActivity() {
  var APP_REQUEST_CODE = 99

  @BindView(R.id.btn_register) lateinit var btnRegister: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    ButterKnife.bind(this)
    Logger.log("Splash", btnRegister.text.toString())

  }

  @OnClick(R.id.btn_register) fun onViewClicked() {
    Logger.log("btn_click")
    val accessToken = AccountKit.getCurrentAccessToken()

    if (accessToken != null) {
      Logger.log("Splash ", accessToken.accountId)
      goToMyLoggedInActivity()
    } else {
      Logger.log("Splash", "no acces token")
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
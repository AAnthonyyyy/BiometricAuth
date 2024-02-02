package com.hgm.biometricauth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity

/**
 * @author：HGM
 * @created：2024/2/2 0002
 * @description：生物识别认证器
 **/
class BiometricAuthenticator(
      private val context: Context
) {

      private lateinit var promptInfo: PromptInfo

      private val biometricManager = BiometricManager.from(context)

      private lateinit var biometricPrompt: BiometricPrompt


      /** 检查是否可用  **/
      private fun isBiometricAvailable(): BiometricAuthStatus {
            return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                  BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.READY
                  BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NOT_AVAILABLE
                  BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE
                  BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
                  else -> BiometricAuthStatus.NOT_AVAILABLE
            }
      }


      /**  显示验证对话框 **/
      fun promptBiometricAuth(
            title: String,
            subTitle: String,
            negativeButtonText: String,
            fragmentActivity: FragmentActivity,
            onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
            onField: () -> Unit,
            onError: (errorCode: Int, errorMsg: String) -> Unit
      ) {
            when (isBiometricAvailable()) {
                  BiometricAuthStatus.NOT_AVAILABLE -> {
                        onError(BiometricAuthStatus.NOT_AVAILABLE.id, "设备不支持生物识别")
                        return
                  }

                  BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE -> {
                        onError(BiometricAuthStatus.NOT_AVAILABLE.id, "暂时不可用")
                        return
                  }

                  BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                        onError(
                              BiometricAuthStatus.NOT_AVAILABLE.id,
                              "请先录入你的指纹或者面容特征"
                        )
                        return
                  }

                  else -> Unit
            }

            biometricPrompt = BiometricPrompt(
                  fragmentActivity,
                  object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                              errorCode: Int,
                              errString: CharSequence
                        ) {
                              super.onAuthenticationError(errorCode, errString)
                              onError(errorCode, errString.toString())
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                              super.onAuthenticationSucceeded(result)
                              onSuccess(result)
                        }

                        override fun onAuthenticationFailed() {
                              super.onAuthenticationFailed()
                              onField()
                        }
                  }
            )

            promptInfo = PromptInfo.Builder()
                  .setTitle(title)
                  .setSubtitle(subTitle)
                  .setNegativeButtonText(negativeButtonText)
                  .build()

            biometricPrompt.authenticate(promptInfo)
      }
}
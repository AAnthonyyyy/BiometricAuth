package com.hgm.biometricauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.hgm.biometricauth.ui.theme.BiometricAuthTheme

class MainActivity : FragmentActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val biometricAuthenticator = BiometricAuthenticator(this)

            setContent {
                  BiometricAuthTheme {
                        val activity = LocalContext.current as FragmentActivity
                        var message by remember {
                              mutableStateOf("")
                        }

                        Surface(
                              modifier = Modifier.fillMaxSize(),
                              color = MaterialTheme.colorScheme.background
                        ) {
                              Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                              ) {
                                    TextButton(onClick = {
                                          biometricAuthenticator.promptBiometricAuth(
                                                title = "登录",
                                                subTitle = "请使用指纹或者面容验证",
                                                negativeButtonText = "取消",
                                                fragmentActivity = activity,
                                                onSuccess = {
                                                      message = "登录成功"
                                                },
                                                onError = { _, errorMsg ->
                                                      message = errorMsg
                                                },
                                                onField = {
                                                      message = "指纹或者面容验证错误"
                                                }
                                          )
                                    }) {
                                          Text(text = "使用指纹或者面容登录")
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = message)
                              }
                        }
                  }
            }
      }
}

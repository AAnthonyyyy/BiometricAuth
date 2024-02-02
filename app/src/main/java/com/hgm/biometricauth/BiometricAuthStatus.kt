package com.hgm.biometricauth

/**
 * @author：HGM
 * @created：2024/2/2 0002
 * @description：生物识别身份验证状态
 **/
enum class BiometricAuthStatus(val id: Int) {
      // 已准备、不可用、暂时不可用、可用但是还未注册
      READY(1),
      NOT_AVAILABLE(-1),
      TEMPORARY_NOT_AVAILABLE(-2),
      AVAILABLE_BUT_NOT_ENROLLED(-3)
}
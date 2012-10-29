package com.github.jond3k

import util.matching.Regex

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class CaseInsensitivePattern(str: String) {
  def ri: Regex = ("(?i)" + str).r
}

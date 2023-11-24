package org.example.game;

public class KoreanDoubleConstant {

  private String value;

  public KoreanDoubleConstant(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /*
  private String applyKoreanDoubleConstant() {
    // 'ㄱ, ㄷ, ㅂ, ㅅ' 자음인 경우에만 두음법칙을 적용한다.
    if (this.value.charAt(0) == 'ㄱ' || this.value.charAt(0) == 'ㄷ' || this.value.charAt(0) == 'ㅂ'
        || this.value.charAt(0) == 'ㅅ') {
      // 'ㅅ' 자음인 경우에는 'ㅅ'을 'ㅆ'으로 바꾼다.
      if (this.value.charAt(0) == 'ㅅ') {
        this.value = "ㅆ" + this.value.substring(1);
      }
      // 'ㄱ, ㄷ, ㅂ' 자음인 경우에는 'ㅎ'을 붙인다.
      else {
        this.value = this.value + "ㅎ";
      }
    }
  }
  */
}

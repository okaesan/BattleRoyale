package com._28ftb.battleroyale.clubs;

import java.util.List;

/**
 * 部活の作成に使用します。
 * 部活を追加する時ははじめにaddClubをonEnableに入れてください。
 */
public class Club {

  private String displayName;
  private String name;
  private List<String> info;

  public Club(String name) {
    this.name = name;
  }

  /**
   * 対象の表示されるべき名前を取得します。
   * 〇〇部にするのがおすすめです。
   * ただし、登録されていない場合システム名を返します。
   *
   * @return 表示用の名前(String)
   */
  public String getDisplayName() {
    if (displayName == null) {
      return name;
    }
    return displayName;
  }

  /**
   * 対象のシステム名を返します。
   * 通常はアルファベットです。
   * またシステム名は、表示されるべきではなくあくまでプログラム内のみで使用されるべきです。
   *
   * @return システム名(String)
   */
  public String getName() {
    return name;
  }

  /**
   * 対象のシステム名を変更します。
   *
   * @deprecated
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 対象に表示用の名前を設定します。
   */
  public void setDisplayName(String name) {
    displayName = name;
  }

  /**
   * 部活にどのような効果があるかなどを設定します。
   *
   * @param list
   * @deprecated
   */
  public void setInfo(List<String> list) {
    info = list;
  }

  /**
   * 部活に設定されている情報を取得します。
   *
   * @return list
   * @deprecated
   */
  public List<String> getInfo() {
    return info;
  }
}

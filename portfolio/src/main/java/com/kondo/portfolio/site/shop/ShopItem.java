package com.kondo.portfolio.site.shop;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * メルカリに出している3Dプリント品1つ分
 *
 * @param title 商品名
 * @param scale 縮尺（1/100 など）
 * @param contents 入っているもの
 * @param priceYen 出品時の価格（送料込み）
 * @param imagePath static 以下の写真のパス
 * @param note 作り方や工夫の一言
 * @param hasSold 売れた実績があるか
 */
public record ShopItem(
        String title,
        String scale,
        String contents,
        int priceYen,
        String imagePath,
        String note,
        boolean hasSold) {

    public String priceLabel() {
        return "¥" + NumberFormat.getIntegerInstance(Locale.JAPAN).format(priceYen);
    }
}

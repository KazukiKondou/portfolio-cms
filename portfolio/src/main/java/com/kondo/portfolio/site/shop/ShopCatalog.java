package com.kondo.portfolio.site.shop;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * /shop に載せるメルカリ販売の中身
 *
 * <p>商品の入れ替えは月に数回なので、CMS のテーブルは作らずコードで持つ。
 */
@Component
public class ShopCatalog {

    private static final String SHOP_NAME = "こんどぅ工房｜3Dプリント";
    private static final String MERCARI_URL = "https://jp.mercari.com/user/profile/542836628";

    private static final List<ShopItem> ITEMS =
            List.of(
                    new ShopItem(
                            "椅子100脚・机25台セット",
                            "1/100",
                            "机1台＋椅子4脚を25セット",
                            999,
                            "/img/shop/chairs-desks-1-100.jpg",
                            "最初の商品。寸法から自作し、並べたまま刷れる配置にして撮影の手間も減らした。",
                            true),
                    new ShopItem(
                            "日本の車10台＋自転車22台",
                            "1/100",
                            "車5種・自転車3種",
                            1280,
                            "/img/shop/cars-bikes-1-100.jpg",
                            "販売できる日本の車のデータが無かったので自作。車＋自転車のセットはメルカリに無かった。",
                            true),
                    new ShopItem(
                            "図書館の本棚30個",
                            "1/100",
                            "標準・低書架・3連の3種",
                            1180,
                            "/img/shop/library-shelves-1-100.jpg",
                            "CC BY のデータ（元は1/50）を1/100・板厚0.6mmで作り直し、出典を明記して販売。",
                            true),
                    new ShopItem(
                            "丸テーブル25台＋椅子100脚",
                            "1/50",
                            "丸テーブル1台＋椅子4脚を25セット",
                            1580,
                            "/img/shop/round-tables-1-50.jpg",
                            "売れ筋の出品者を分析して作った品。直径2.4mmの柱が折れないか試し刷りで確かめた。",
                            true),
                    new ShopItem(
                            "住宅家具 16種27点",
                            "1/100",
                            "3LDK一軒分（ソファ・ベッド・キッチン・浴槽など）",
                            2280,
                            "/img/shop/house-furniture-1-100.jpg",
                            "寸法の質問が来たのを受けて、写真と説明のすべてにmm寸法を入れた。",
                            false),
                    new ShopItem(
                            "ホテル客室 ツイン1室",
                            "1/50",
                            "ベッド・デスク・浴槽など9種10点",
                            1180,
                            "/img/shop/hotel-room-1-50.jpg",
                            "メルカリにホテル用の家具は0件だったので、空いている枠として作った。",
                            false));

    private static final List<ShopRole> MY_ROLES =
            List.of(
                    new ShopRole(
                            "企画・調査",
                            "メルカリの売り切れ商品から相場と売れ筋を集め、競合のいない枠を探して次に作る物を決める。"
                                    + "他人のデータは販売が認められたライセンスか確かめる。"),
                    new ShopRole("タスクの割り振り", "設計・印刷・撮影・梱包・発送を3人に振り分ける。注文が入ったら誰が刷って送るかを決める。"),
                    new ShopRole(
                            "お金の管理",
                            "売上・材料費・送料・手数料を表にまとめ、商品ごとの利益から値段を決める。"
                                    + "事業にする基準（週の利益1万円が続いたら開業届）も決めた。"),
                    new ShopRole(
                            "仕組みづくり",
                            "Discord に注文を知らせる仕組みと、刷るファイルの一覧を用意し、" + "自分がいなくても誰でも刷り直して発送できるようにした。"));

    public List<ShopItem> items() {
        return ITEMS;
    }

    public List<ShopRole> myRoles() {
        return MY_ROLES;
    }

    public String shopName() {
        return SHOP_NAME;
    }

    public String mercariUrl() {
        return MERCARI_URL;
    }
}

package com.kondo.portfolio.site.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ShopCatalogTest {

    private final ShopCatalog catalog = new ShopCatalog();

    @Test
    void 商品は1件以上あり_すべてに名前と縮尺と価格と写真がある() {
        List<ShopItem> items = catalog.items();

        assertThat(items).isNotEmpty();
        assertThat(items)
                .allSatisfy(
                        item -> {
                            assertThat(item.title()).isNotBlank();
                            assertThat(item.scale()).matches("1/\\d+");
                            assertThat(item.priceYen()).isPositive();
                            assertThat(item.imagePath()).startsWith("/img/shop/").endsWith(".jpg");
                        });
    }

    @Test
    void 写真のファイル名は商品ごとに重ならない() {
        List<String> paths = catalog.items().stream().map(ShopItem::imagePath).toList();

        assertThat(paths).doesNotHaveDuplicates();
    }

    @Test
    void 写真はクラスパスに実在する() {
        assertThat(catalog.items())
                .allSatisfy(
                        item ->
                                assertThat(getClass().getResource("/static" + item.imagePath()))
                                        .as(item.imagePath())
                                        .isNotNull());
    }

    @Test
    void 売れた実績のある商品が含まれる() {
        assertThat(catalog.items()).anyMatch(ShopItem::hasSold);
    }

    @Test
    void 価格は3桁区切りの円表記で返す() {
        ShopItem item = new ShopItem("t", "1/100", "c", 1280, "/img/shop/x.jpg", "n", false);

        assertThat(item.priceLabel()).isEqualTo("¥1,280");
    }

    @Test
    void ショップ名とメルカリのURLを持つ() {
        assertThat(catalog.shopName()).isEqualTo("こんどぅ工房｜3Dプリント");
        assertThat(catalog.mercariUrl()).startsWith("https://jp.mercari.com/user/profile/");
    }

    @Test
    void 担当した役割を4つ持つ() {
        assertThat(catalog.myRoles())
                .extracting(ShopRole::name)
                .containsExactly("企画・調査", "タスクの割り振り", "お金の管理", "仕組みづくり");
    }
}

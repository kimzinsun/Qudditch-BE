package com.goldensnitch.qudditch.utils;

import com.goldensnitch.qudditch.dto.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Crawling {
    @SuppressWarnings("unused")
    public static List<Product> Crawling(String url) {
        Connection conn = Jsoup.connect(url);
        List<Product> list = new ArrayList<>();


        try {
            Document document = conn.get();

            Elements unitElements = document.getElementsByClass("mnemitem_grid_item");
            Integer size = unitElements.size();

            for (Element unit : unitElements) {
                Elements imgElement = unit.getElementsByClass("mnemitem_thmb_img");
                Elements brandElement = unit.select("div.mnemitem_tit > span.mnemitem_goods_brand");
                Elements nameElement = unit.select("div.mnemitem_tit > span.mnemitem_goods_tit");
                Elements priceElement = unit.select("div.new_price > em.ssg_price");
                Elements unitPriceElement = unit.getElementsByClass("unit_price");

                String name = nameElement.text();
                String image = imgElement.attr("abs:src");
                String brand = brandElement.text();
                String wildPrice = priceElement.text();
                int price = Integer.parseInt(wildPrice.replaceAll(",", ""));
                String unitPrice = unitPriceElement.text();

                Product product = new Product();
                product.setName(name);
                product.setBrand(brand);
                product.setPrice(price);
                product.setUnitPrice(unitPrice);
                product.setImage(image);
                list.add(product);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;

    }

}

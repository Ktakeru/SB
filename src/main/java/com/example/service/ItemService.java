package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Item;
import com.example.repository.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    public List<Item> findAll() {
        return this.itemRepository.findAll();
    }

    public Item save(ItemForm itemForm) {
        // Entityクラスのインスタンスを生成します
        Item item = new Item();
        // フィールドのセットを行います
        item.setName(itemForm.getName());
        item.setPrice(itemForm.getPrice());
        item.setCategoryId(itemForm.getCategoryId());
        item.setStock(0);
        // 新規登録時は在庫数に0をセットする
        // repository.saveメソッドを利用してデータの保存を行います
        return this.itemRepository.save(item);
    }
    public Item findById(Integer id) {
        Optional<Item> optionalItem = this.itemRepository.findById(id);
        Item item  = optionalItem.get();
        return item;
    }
    public Item update(Integer id, ItemForm itemForm) {
        // データ１件分のEntityクラスを取得します
        Item item = this.findById(id);
        // Formクラスのフィールドをセットします
        item.setName(itemForm.getName());
        item.setPrice(itemForm.getPrice());
        item.setCategoryId(itemForm.getCategoryId());
        // repository.saveメソッドを利用してデータの保存を行います
        return this.itemRepository.save(item);
    }
    public Item delete(Integer id) {
        this.itemRepository.deleteById(id);
     // idから該当のEntityクラスを取得します
        Item item = this.findById(id);
        // EntityクラスのdeletedAtフィールドを現在日時で上書きします
        item.setDeletedAt(LocalDateTime.now());

        // 更新処理
        return this.itemRepository.save(item);
    }
    public List<Item> findByDeletedAtIsNull() {
        return this.itemRepository.findByDeletedAtIsNull();
    }
    public Item nyuka(Integer id, Integer inputValue) {
        Item item = this.findById(id);
        // 商品の在庫数に対して入力値分加算する
        item.setStock(item.getStock() + inputValue);
        // 在庫数の変動を保存
        return this.itemRepository.save(item);
    }

    // 出荷処理
    public Item shukka(Integer id, Integer inputValue) {
        Item item = this.findById(id);
        // 入力値が在庫数以内かを判定する
        if (inputValue <= item.getStock()) {
            // 在庫数から入力値を引いた値をセットする
            item.setStock(item.getStock() - inputValue);
        }

        // 在庫数の変動を保存
        return this.itemRepository.save(item);
    }


}
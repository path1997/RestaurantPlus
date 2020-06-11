package com.example.restaurantplus;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    public void addProduct(CartDb cartDb);

    @Query("select * from cart")
    public List<CartDb> getProducts();

    @Query("select count(productid) from cart")
    public int productCount();

    @Query("select sum(totalCost) from cart")
    public int totalCost();

    @Query("UPDATE cart SET quantity=:quantity and totalCost=:totalCost WHERE productid=:id")
    public void changeQuantity(int id,int quantity, double totalCost);

    @Query("DELETE FROM cart where productid=:id")
    public void deleteProductById(int id);

    @Query("DELETE FROM cart")
    public void deleteAllProduct();

    @Query("select count(productid) from cart")
    public int getCount();

    @Query("select * from cart where productid=:id")
    public List<CartDb> getProductbyId(int id);
}

package com.calculator.vaultlocker.Model;

import com.calculator.vaultlocker.Model.WalletCategoriesFieldPojo;

import java.util.List;


public class WalletCategoriesPojo {
    private List<WalletCategoriesFieldPojo> categoriesFields;
    private int categoriesIconIndex;
    private String categoriesName;

    public WalletCategoriesPojo() {
    }

    public WalletCategoriesPojo(String str, int i, List<WalletCategoriesFieldPojo> list) {
        this.categoriesName = str;
        this.categoriesIconIndex = i;
        this.categoriesFields = list;
    }

    public String getCategoryName() {
        return this.categoriesName;
    }

    public void setCategoryName(String str) {
        this.categoriesName = str;
    }

    public int getCategoryIconIndex() {
        return this.categoriesIconIndex;
    }

    public void setCategoryIconIndex(int i) {
        this.categoriesIconIndex = i;
    }

    public List<WalletCategoriesFieldPojo> getCategoryFields() {
        return this.categoriesFields;
    }

    public void setCategoryFields(List<WalletCategoriesFieldPojo> list) {
        this.categoriesFields = list;
    }
}

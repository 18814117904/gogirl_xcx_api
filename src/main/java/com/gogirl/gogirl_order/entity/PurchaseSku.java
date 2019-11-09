package com.gogirl.gogirl_order.entity;

import java.math.BigDecimal;

public class PurchaseSku {
    private Integer id;

    private String sku;

    private String picturePath;

    private String skuName;

    private Integer skuType;

    private String skuModel;

    private Double shopPrice;

    private Double purchasePrice;

    private String supplier;

    private Integer useType;

    private Integer seriesId;

    private Integer ppsParentId;

    private String ppsSku;

    private Boolean ppsMultiattribute;

    private Integer ppsCategoryId;

    private String ppsCategoryName;

    private Integer ppsShoppingMallCategoryId;

    private String ppsShoppingMallCategoryName;

    private String ppsGoodsTitle;

    private String ppsDescription;

    private BigDecimal ppsOriginalPrice;

    private BigDecimal ppsMemberPrice;

    private BigDecimal ppsDiscountPrice;

    private String ppsPicturePath;

    private String ppsVedioPath;

    private String ppsBrand;

    private String ppsColorNumber;

    private String ppsColorName;

    private String ppsSpecs;

    private String ppsSize;

    private Double ppsWeight;

    private String ppsUnit;

    private String ppsLogisticsDescription;

    private String ppsGuaranteeInstruction;

    private String ppsAfterSaleInstructions;

    private Integer source;

    private String colorValue;

    private String colorName;

    private String colorImgPath;

    private String ppsDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath == null ? null : picturePath.trim();
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel == null ? null : skuModel.trim();
    }

    public Double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getPpsParentId() {
        return ppsParentId;
    }

    public void setPpsParentId(Integer ppsParentId) {
        this.ppsParentId = ppsParentId;
    }

    public String getPpsSku() {
        return ppsSku;
    }

    public void setPpsSku(String ppsSku) {
        this.ppsSku = ppsSku == null ? null : ppsSku.trim();
    }

    public Boolean getPpsMultiattribute() {
        return ppsMultiattribute;
    }

    public void setPpsMultiattribute(Boolean ppsMultiattribute) {
        this.ppsMultiattribute = ppsMultiattribute;
    }

    public Integer getPpsCategoryId() {
        return ppsCategoryId;
    }

    public void setPpsCategoryId(Integer ppsCategoryId) {
        this.ppsCategoryId = ppsCategoryId;
    }

    public String getPpsCategoryName() {
        return ppsCategoryName;
    }

    public void setPpsCategoryName(String ppsCategoryName) {
        this.ppsCategoryName = ppsCategoryName == null ? null : ppsCategoryName.trim();
    }

    public Integer getPpsShoppingMallCategoryId() {
        return ppsShoppingMallCategoryId;
    }

    public void setPpsShoppingMallCategoryId(Integer ppsShoppingMallCategoryId) {
        this.ppsShoppingMallCategoryId = ppsShoppingMallCategoryId;
    }

    public String getPpsShoppingMallCategoryName() {
        return ppsShoppingMallCategoryName;
    }

    public void setPpsShoppingMallCategoryName(String ppsShoppingMallCategoryName) {
        this.ppsShoppingMallCategoryName = ppsShoppingMallCategoryName == null ? null : ppsShoppingMallCategoryName.trim();
    }

    public String getPpsGoodsTitle() {
        return ppsGoodsTitle;
    }

    public void setPpsGoodsTitle(String ppsGoodsTitle) {
        this.ppsGoodsTitle = ppsGoodsTitle == null ? null : ppsGoodsTitle.trim();
    }

    public String getPpsDescription() {
        return ppsDescription;
    }

    public void setPpsDescription(String ppsDescription) {
        this.ppsDescription = ppsDescription == null ? null : ppsDescription.trim();
    }

    public BigDecimal getPpsOriginalPrice() {
        return ppsOriginalPrice;
    }

    public void setPpsOriginalPrice(BigDecimal ppsOriginalPrice) {
        this.ppsOriginalPrice = ppsOriginalPrice;
    }

    public BigDecimal getPpsMemberPrice() {
        return ppsMemberPrice;
    }

    public void setPpsMemberPrice(BigDecimal ppsMemberPrice) {
        this.ppsMemberPrice = ppsMemberPrice;
    }

    public BigDecimal getPpsDiscountPrice() {
        return ppsDiscountPrice;
    }

    public void setPpsDiscountPrice(BigDecimal ppsDiscountPrice) {
        this.ppsDiscountPrice = ppsDiscountPrice;
    }

    public String getPpsPicturePath() {
        return ppsPicturePath;
    }

    public void setPpsPicturePath(String ppsPicturePath) {
        this.ppsPicturePath = ppsPicturePath == null ? null : ppsPicturePath.trim();
    }

    public String getPpsVedioPath() {
        return ppsVedioPath;
    }

    public void setPpsVedioPath(String ppsVedioPath) {
        this.ppsVedioPath = ppsVedioPath == null ? null : ppsVedioPath.trim();
    }

    public String getPpsBrand() {
        return ppsBrand;
    }

    public void setPpsBrand(String ppsBrand) {
        this.ppsBrand = ppsBrand == null ? null : ppsBrand.trim();
    }

    public String getPpsColorNumber() {
        return ppsColorNumber;
    }

    public void setPpsColorNumber(String ppsColorNumber) {
        this.ppsColorNumber = ppsColorNumber == null ? null : ppsColorNumber.trim();
    }

    public String getPpsColorName() {
        return ppsColorName;
    }

    public void setPpsColorName(String ppsColorName) {
        this.ppsColorName = ppsColorName == null ? null : ppsColorName.trim();
    }

    public String getPpsSpecs() {
        return ppsSpecs;
    }

    public void setPpsSpecs(String ppsSpecs) {
        this.ppsSpecs = ppsSpecs == null ? null : ppsSpecs.trim();
    }

    public String getPpsSize() {
        return ppsSize;
    }

    public void setPpsSize(String ppsSize) {
        this.ppsSize = ppsSize == null ? null : ppsSize.trim();
    }

    public Double getPpsWeight() {
        return ppsWeight;
    }

    public void setPpsWeight(Double ppsWeight) {
        this.ppsWeight = ppsWeight;
    }

    public String getPpsUnit() {
        return ppsUnit;
    }

    public void setPpsUnit(String ppsUnit) {
        this.ppsUnit = ppsUnit == null ? null : ppsUnit.trim();
    }

    public String getPpsLogisticsDescription() {
        return ppsLogisticsDescription;
    }

    public void setPpsLogisticsDescription(String ppsLogisticsDescription) {
        this.ppsLogisticsDescription = ppsLogisticsDescription == null ? null : ppsLogisticsDescription.trim();
    }

    public String getPpsGuaranteeInstruction() {
        return ppsGuaranteeInstruction;
    }

    public void setPpsGuaranteeInstruction(String ppsGuaranteeInstruction) {
        this.ppsGuaranteeInstruction = ppsGuaranteeInstruction == null ? null : ppsGuaranteeInstruction.trim();
    }

    public String getPpsAfterSaleInstructions() {
        return ppsAfterSaleInstructions;
    }

    public void setPpsAfterSaleInstructions(String ppsAfterSaleInstructions) {
        this.ppsAfterSaleInstructions = ppsAfterSaleInstructions == null ? null : ppsAfterSaleInstructions.trim();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue == null ? null : colorValue.trim();
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName == null ? null : colorName.trim();
    }

    public String getColorImgPath() {
        return colorImgPath;
    }

    public void setColorImgPath(String colorImgPath) {
        this.colorImgPath = colorImgPath == null ? null : colorImgPath.trim();
    }

    public String getPpsDetails() {
        return ppsDetails;
    }

    public void setPpsDetails(String ppsDetails) {
        this.ppsDetails = ppsDetails == null ? null : ppsDetails.trim();
    }
}
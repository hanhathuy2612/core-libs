package com.hnh.enterprise.core.enumeration;

/**
 * Supplier category enumeration for tourism service providers
 */
public enum SupplierCategory {
    ACCOMMODATION("Lưu trú"),
    RESTAURANT("Nhà hàng"), 
    TRANSPORTATION("Vận chuyển"),
    ENTERTAINMENT("Vui chơi giải trí"),
    ATTRACTION("Điểm tham quan"),
    GUIDE_SERVICE("Dịch vụ hướng dẫn"),
    OTHER("Khác");

    private final String displayName;

    SupplierCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

package com.hnh.enterprise.core.enumeration;

/**
 * Tour status enumeration based on TURA specification
 * - PENDING_CONFIRMATION: Đang xác nhận - Tours sold by Sales, awaiting operations confirmation
 * - CONFIRMED: Đã xác nhận - Operations team actively managing and operating
 * - PAYMENT_COMPLETED: Đã thanh toán - Post-tour completion, settlement phase
 * - CANCELLED: Đã hủy - Cancelled tours
 * - DRAFT: Bản nháp - Tours in design phase
 */
public enum TourStatus {
    DRAFT("Bản nháp"),
    PENDING_CONFIRMATION("Đang xác nhận"),
    CONFIRMED("Đã xác nhận"), 
    PAYMENT_COMPLETED("Đã thanh toán"),
    CANCELLED("Đã hủy");

    private final String displayName;

    TourStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

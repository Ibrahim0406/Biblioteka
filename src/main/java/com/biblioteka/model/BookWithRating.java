package com.biblioteka.model;

import javafx.beans.property.*;

public class BookWithRating extends Book {
    private final DoubleProperty averageRating;
    private final IntegerProperty reviewCount;

    public BookWithRating(int id, String title, String author, String isbn, int year,
                          double price, String status, int availableQuantity,
                          double averageRating, int reviewCount) {
        super(id, title, author, isbn, year, price, status, availableQuantity);
        this.averageRating = new SimpleDoubleProperty(averageRating);
        this.reviewCount = new SimpleIntegerProperty(reviewCount);
    }

    // Average Rating
    public double getAverageRating() {
        return averageRating.get();
    }

    public void setAverageRating(double value) {
        averageRating.set(value);
    }

    public DoubleProperty averageRatingProperty() {
        return averageRating;
    }

    // Review Count
    public int getReviewCount() {
        return reviewCount.get();
    }

    public void setReviewCount(int value) {
        reviewCount.set(value);
    }

    public IntegerProperty reviewCountProperty() {
        return reviewCount;
    }

    public String getRatingStars() {
        double rating = getAverageRating();
        if (rating == 0) return "Bez ocjene";

        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        boolean hasHalfStar = (rating - fullStars) >= 0.5;

        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        if (hasHalfStar) {
            stars.append("⯨");
        }
        for (int i = fullStars + (hasHalfStar ? 1 : 0); i < 5; i++) {
            stars.append("☆");
        }

        return stars.toString() + String.format(" (%.1f)", rating);
    }
}

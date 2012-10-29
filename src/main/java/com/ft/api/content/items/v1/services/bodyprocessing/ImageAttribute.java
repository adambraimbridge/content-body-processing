package com.ft.api.content.items.v1.services.bodyprocessing;

public enum ImageAttribute {
    HEIGHT("height"), WIDTH("width"), ALT("alt"), SRC("src"), CAPTION("caption"), SOURCE("source"), MEDIA_TYPE("media_type");

    private String attributeName;

    ImageAttribute(String attributeName) {
        this.attributeName = attributeName;
    }
    
    public static ImageAttribute getByName(String attributeName) {
        for(ImageAttribute imageAttribute : ImageAttribute.values()) {
            if(imageAttribute.attributeName.equals(attributeName)) {
                return imageAttribute;
            }
        }
        return null;
    }
}
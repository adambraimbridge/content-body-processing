package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.PromoBox;
import com.ft.unifiedContentModel.model.PromoBoxFields;
import com.ft.unifiedContentModel.model.PromoBoxImage;

public class PromoBoxData extends BaseData implements AssetAware {

    private String title;
    private String headline;
    private String intro;
    private String link;
    private String imageUrl;
    private String imageType;
    private String imageHeight;
    private String imageWidth;
    private String imageAlt;
    private String imageFileRef;
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }
    
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public String getHeadline() {
        return headline;
    }

    public String getIntro() {
        return intro;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public String getImageFileRef() {
        return imageFileRef;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageFileRef(String imageFileRef) {
        this.imageFileRef = imageFileRef;
    }
    
    @Override
    public boolean isOkToRender() {
        if(!StringUtils.isBlank(this.title) && !StringUtils.isBlank(this.imageFileRef)) {
            return true;
        }
        return false;
    }

    @Override
    public Asset getAsset() throws IllegalStateException {
        PromoBox promoBox = null;
        if(this.isOkToRender()) {
            promoBox = new PromoBox();
            PromoBoxImage promoImage = new PromoBoxImage(nullIfEmpty(this.imageUrl), nullIfEmpty(this.imageType), nullIfEmpty(this.imageHeight), 
                                                        nullIfEmpty(this.imageWidth), nullIfEmpty(this.imageAlt));
            
            PromoBoxFields fields = new PromoBoxFields(nullIfEmpty(this.title), nullIfEmpty(this.headline), 
                                                       nullIfEmpty(intro), nullIfEmpty(this.link), promoImage);
            promoBox.setFields(fields);
            return promoBox;
        }
        throw new IllegalStateException("The object does not have sufficient data to render a valid asset. Only if the method isOkToRender is true will this method return a valid asset.");
     }
}
package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.InteractiveGraphic;
import com.ft.unifiedContentModel.model.InteractiveGraphicFields;

public class InteractiveGraphicData extends BaseData implements AssetAware {

    private String id;
    private String src;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
       this.src = src;
    }

    public boolean isOkToRender() {
        return !StringUtils.isEmpty(this.id) && !StringUtils.isEmpty(this.src);
    }

    @Override
    public Asset getAsset() throws IllegalStateException {
        InteractiveGraphic interactiveGraphic = null;
        if(this.isOkToRender()) {
            interactiveGraphic = new InteractiveGraphic();
            InteractiveGraphicFields fields = new InteractiveGraphicFields(nullIfEmpty(this.src), nullIfEmpty(this.id));
            interactiveGraphic.setFields(fields);
            return interactiveGraphic;
        }
        throw new IllegalStateException("The object does not have sufficient data to render a valid asset. Only if the method isOkToRender is true will this method return a valid asset.");
    }

}

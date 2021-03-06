package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.InteractiveGraphic;
import com.ft.api.ucm.model.v1.InteractiveGraphicFields;

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

    public boolean isAllRequiredDataPresent() {
        return containsValidData(this.id) && containsValidData(this.src);
    }

    @Override
    public Asset getAsset() throws BodyProcessingException {
        InteractiveGraphic interactiveGraphic = null;
        if(this.isAllRequiredDataPresent()) {
            interactiveGraphic = new InteractiveGraphic();
            InteractiveGraphicFields fields = new InteractiveGraphicFields(nullIfEmpty(this.src), nullIfEmpty(this.id));
            interactiveGraphic.setFields(fields);
            return interactiveGraphic;
        }
        throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);
    }

}

package com.ft.content.bodyprocessing;

import com.ft.api.ucm.model.v1.Asset;
import com.ft.deliveryplatform.content.ImageView;


import java.util.List;
import java.util.Map;


public interface BodyProcessingContext {

    @Deprecated
	public String getAttributeForImage(String attributeName, String uuid);
	
	public String getAttributeForImage(ImageAttribute imageAttribute, String uuid);
	
	public Asset assignAssetNameToExistingAsset(String uuid);

	public Asset addAsset(Asset asset);
	
	public boolean imageExists(String uuid);

    public void addImageWithAttributes(Map<String, String> attributes);
    
    List<ImageView> retrieveProcessedImages();
}

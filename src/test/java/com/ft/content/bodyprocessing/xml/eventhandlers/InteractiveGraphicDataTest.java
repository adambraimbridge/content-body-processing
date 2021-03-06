package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.InteractiveGraphic;
import com.ft.api.ucm.model.v1.InteractiveGraphicFields;


public class InteractiveGraphicDataTest {

    private InteractiveGraphicData interactiveGraphicData;
    private String id = "someId";
    private String src = "someUrl";
    
    @Before
    public void setUp() {
        interactiveGraphicData = new InteractiveGraphicData();
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenIdAndSrcPresent() {
        interactiveGraphicData.setId(id);
        interactiveGraphicData.setSrc(src);
        assertTrue(interactiveGraphicData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenOnlyIdPresent() {
        interactiveGraphicData.setId(id);        
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenOnlyIdPresentSrcNewLineData() {
        interactiveGraphicData.setSrc("\n");
        interactiveGraphicData.setId(id);        
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenOnlySrcPresentIdNewLineData() {
        interactiveGraphicData.setSrc(src);
        interactiveGraphicData.setId("\n");   
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenNoDataPresent() {
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testGetAssetIsValid() {
        interactiveGraphicData.setId(id);
        interactiveGraphicData.setSrc(src);
        Asset actualAsset = interactiveGraphicData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        assertTrue("The asset is not of the expected type.", actualAsset instanceof InteractiveGraphic);
        InteractiveGraphicFields actualFields = ((InteractiveGraphic)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        Assert.assertEquals("The id was not as expected.", actualFields.getId(), id);
        Assert.assertEquals("The url was not as expected.", actualFields.getUrl(), src);
    }
    
    @Test(expected = BodyProcessingException.class)
    public void testGetAssetWhenNotOkToRender() {
        interactiveGraphicData.getAsset();
    }
}

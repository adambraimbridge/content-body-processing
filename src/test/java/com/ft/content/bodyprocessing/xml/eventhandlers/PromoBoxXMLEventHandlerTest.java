package com.ft.content.bodyprocessing.xml.eventhandlers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import junit.framework.Assert;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.ucm.model.v1.Asset;
import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;

@RunWith(MockitoJUnitRunner.class)
public class PromoBoxXMLEventHandlerTest extends BaseXMLEventHandlerTest {

    @Mock private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private PromoBoxXMLParser mockPromoBoxXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private PromoBoxData mockPromoBox;
        
    private StartElement startElement;
    private PromoBoxXMLEventHandler promoBoxXMLEventHandler;
    private String elementName = "someElementName";
    
    @Before
    public void setup() throws XMLStreamException {
        promoBoxXMLEventHandler = new PromoBoxXMLEventHandler(mockPromoBoxXMLParser, mockAsideElementWriter);
        when(mockPromoBoxXMLParser.parseElementData(Matchers.isA(StartElement.class), Matchers.eq(mockXmlEventReader))).thenReturn(mockPromoBox);
        when(mockAsset.getName()).thenReturn(elementName);
    }
    
    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isAllRequiredDataPresent()).thenReturn(true);
        when(mockPromoBox.getAsset()).thenReturn(mockAsset);
        when(mockBodyProcessingContext.addAsset(Matchers.isA(Asset.class))).thenReturn(mockAsset);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTag() throws XMLStreamException {
        
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isAllRequiredDataPresent()).thenReturn(false);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser, never()).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTagMissingIdElement() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isAllRequiredDataPresent()).thenReturn(false);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser, never()).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTagMissingIdElementAfterTransformation() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isAllRequiredDataPresent()).thenReturn(true).thenReturn(false);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullPullQuoteXMLParser() {
        new InteractiveGraphicXMLEventHandler(null, mockAsideElementWriter);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullAsideElementWriter() {
        new PromoBoxXMLEventHandler(mockPromoBoxXMLParser, null);
    }
    
    @Test
    public void testGetTypePromoteNumber() {
        when(mockPromoBox.isNumbersComponent()).thenReturn(true);
        
        String type = promoBoxXMLEventHandler.getType(mockPromoBox);
        assertEquals("promoteNumber", type);
    }

    @Test
    public void testGetTypePromoBox() {
        when(mockPromoBox.isNumbersComponent()).thenReturn(false);
        
        String type = promoBoxXMLEventHandler.getType(mockPromoBox);
        assertEquals("promoBox", type);
    }
}

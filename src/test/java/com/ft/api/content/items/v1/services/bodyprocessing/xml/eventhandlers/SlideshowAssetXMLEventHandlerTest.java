package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.SlideshowAssetXMLEventHandler;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.XMLEventHandler;
import com.ft.unifiedContentModel.model.Asset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SlideshowAssetXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private static final String EMPTY_UUID_MEDIA_ASSET_HREF = "?uuid=&a=1";
	private static final String MEDIA_ASSET_HREF = "http://www.ft.com/blah/abc.xml?a=1&uuid=1234567890&b=3";
	private static final String MEDIA_ASSET_HREF_NO_UUID = "http://www.ft.com/blah/abc.xml?a=1&b=3";
	private static final String NAME_1 = "name1";
	private static final String HREF_ATTRIBUTE_NAME = "href";
	private static final String TYPE_ATTRIBUTE_NAME = "type";
	private static final String SLIDESHOW_ATTRIBUTE_VALUE = "slideshow";
	private static final String DUMMY_ATTRIBUTE_VALUE = "dummy";
	
	@Mock private BodyWriter mockEventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	@Mock private XMLEventHandler mockFallbackXmlEventHandler;
	@Mock private Asset mockAsset;

	private EndElement endElement;
	private StartElement startElement;
	
	private SlideshowAssetXMLEventHandler mediaAssetXMLEventHandler;
	
	
	@Before
	public void setup() {
		mediaAssetXMLEventHandler = new SlideshowAssetXMLEventHandler(mockFallbackXmlEventHandler);
	}
	
	@Test(expected=XMLStreamException.class)
	public void failStartElementNotA() throws XMLStreamException{
		startElement = getStartElement("p");
		mediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
	}

	@SuppressWarnings({"unchecked", "serial"})
	@Test
	public void shouldFallbackStartElementIfNoMediaAsset() throws XMLStreamException{
		Map<String,String> attributes =  new HashMap<String,String>(){{	
			put(HREF_ATTRIBUTE_NAME, MEDIA_ASSET_HREF); 
			put(TYPE_ATTRIBUTE_NAME, DUMMY_ATTRIBUTE_VALUE);
		}};
		startElement = getStartElementWithAttributes("a", attributes);
		mediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	@SuppressWarnings({"unchecked", "serial"})
	@Test
	public void shouldFallbackStartElementIfEmptyUuid() throws XMLStreamException{
		Map<String,String> attributes =  new HashMap<String,String>(){{	
			put(HREF_ATTRIBUTE_NAME, EMPTY_UUID_MEDIA_ASSET_HREF); 
			put(TYPE_ATTRIBUTE_NAME, SLIDESHOW_ATTRIBUTE_VALUE);
		}};
		startElement = getStartElementWithAttributes("a", attributes);
		mediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	@SuppressWarnings({"unchecked", "serial"})
	@Test
	public void shouldFallbackStartElementIfNoUuid() throws XMLStreamException{
		Map<String,String> attributes =  new HashMap<String,String>(){{	
			put(HREF_ATTRIBUTE_NAME, MEDIA_ASSET_HREF_NO_UUID); 
			put(TYPE_ATTRIBUTE_NAME, SLIDESHOW_ATTRIBUTE_VALUE);
		}};
		startElement = getStartElementWithAttributes("a", attributes);
		mediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	@SuppressWarnings({"unchecked", "serial"})
	@Test
	public void shouldFallbackStartElementIfNoUuidMatch() throws XMLStreamException{
		when(mockBodyProcessingContext.assignAssetNameToExistingAsset("1234567890")).thenReturn(null);
		Map<String,String> attributes =  new HashMap<String,String>(){{	
			put(HREF_ATTRIBUTE_NAME, MEDIA_ASSET_HREF); 
			put(TYPE_ATTRIBUTE_NAME, SLIDESHOW_ATTRIBUTE_VALUE);
		}};
		startElement = getStartElementWithAttributes("a", attributes);
		mediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	@SuppressWarnings({"unchecked","rawtypes", "serial"})
	@Test
	public void shouldPassStartElementIfValidTag() throws XMLStreamException{
		when(mockBodyProcessingContext.assignAssetNameToExistingAsset("1234567890")).thenReturn(mockAsset);
		when(mockAsset.getName()).thenReturn(NAME_1);
		Map<String,String> attributes =  new HashMap<String,String>(){{
			put(HREF_ATTRIBUTE_NAME, MEDIA_ASSET_HREF); 
			put(TYPE_ATTRIBUTE_NAME, SLIDESHOW_ATTRIBUTE_VALUE);
		}};
		startElement = getStartElementWithAttributes("a", attributes);
		ArgumentCaptor<Map> attributesCaptor = ArgumentCaptor.forClass(Map.class);
		ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
		
		mediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

		verify(mockFallbackXmlEventHandler, Mockito.never()).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockBodyProcessingContext).assignAssetNameToExistingAsset("1234567890");
		verify(mockEventWriter, times(2)).writeStartTag(tagCaptor.capture(), attributesCaptor.capture());
		// for slideshows, we have to handle the enclosing p tag which isn't valid round an aside tag
		List<String> tagsWritten = tagCaptor.getAllValues();
		assertEquals("aside", tagsWritten.get(0));
		assertEquals("p", tagsWritten.get(1));
		List<Map> attributesWritten = attributesCaptor.getAllValues();
		assertEquals(NAME_1, attributesWritten.get(0).get("data-asset-name"));
		assertEquals("slideshow", attributesWritten.get(0).get("data-asset-type"));
		assertEquals(null, attributesWritten.get(1));
		verify(mockEventWriter).writeEndTag("p");
		verify(mockEventWriter).writeEndTag("aside");
	}
	
	
	@Test(expected=XMLStreamException.class)
	public void failEndElementNotA() throws XMLStreamException{
		endElement = getEndElement("p");
		mediaAssetXMLEventHandler.handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
	}
	
	@Test
	public void shouldFallbackEndElementA() throws XMLStreamException{
		endElement = getEndElement("a");
		mediaAssetXMLEventHandler.handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
		verify(mockFallbackXmlEventHandler).handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
	}
	
	
}
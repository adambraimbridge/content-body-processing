package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;

public class PromoBoxXMLParser extends BaseXMLParser<PromoBoxData> implements XmlParser<PromoBoxData> {

    private static final String PROMO_TYPE = "promo";
    private static final String PROMO_BOX = "promo-box";
    private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;
    
    private static final String PROMO_TITLE = "promo-title";
    private static final String PROMO_HEADLINE = "promo-headline";
    private static final String PROMO_INTRO = "promo-intro";
    private static final String PROMO_LINK= "promo-link";
    private static final String PROMO_IMAGE= "promo-image";
    private static final String UUIDPREFIX = "uuid=";
    
    
    private ElementRawDataParser rawDataParser;
    
    protected PromoBoxXMLParser(StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
        super(PROMO_BOX);
        notNull(stAXTransformingBodyProcessor, "The StAXTransformingBodyProcessor cannot be null.");
        this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
    }

    @Override
    public void transformFieldContentToStructuredFormat(PromoBoxData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Transform raw strings
        dataBean.setTitle(transformRawContentToStructuredFormat(dataBean.getTitle(), bodyProcessingContext));
        dataBean.setHeadline(transformRawContentToStructuredFormat(dataBean.getHeadline(), bodyProcessingContext));
        dataBean.setIntro(transformRawContentToStructuredFormat(dataBean.getIntro(), bodyProcessingContext));
        dataBean.setLink(transformRawContentToStructuredFormat(dataBean.getLink(), bodyProcessingContext));
        
        // populate image attributes
        String imageUuid = parseImageUuid(dataBean.getImageFileRef());
        dataBean.setImageType(PROMO_TYPE);
        dataBean.setImageHeight(bodyProcessingContext.getAttributeForImage("height", imageUuid));
        dataBean.setImageWidth(bodyProcessingContext.getAttributeForImage("width", imageUuid));
        dataBean.setImageAlt(bodyProcessingContext.getAttributeForImage("alt", imageUuid));
        dataBean.setImageUrl(bodyProcessingContext.getAttributeForImage("src", imageUuid));
    }
    
    //TODO: Refactor to use a regex
    private String parseImageUuid(String imageFileRef) {
        int indexOfUuid= imageFileRef.indexOf("uuid=");
        if (indexOfUuid == -1) {
            return "";
        }
        return imageFileRef.substring(imageFileRef.indexOf(UUIDPREFIX) + UUIDPREFIX.length());
    }

    private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
        if (!StringUtils.isBlank(unprocessedContent)) {
            return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
        }
        return null;
    }

    @Override
    PromoBoxData createDataBeanInstance() {
        return new PromoBoxData();
    }

    @Override
    public void populateBean(PromoBoxData promoBoxData, StartElement nextStartElement, XMLEventReader xmlEventReader)
            throws UnexpectedElementStructureException {
        
        if (isElementNamed(nextStartElement.getName(), PROMO_TITLE)) {
            promoBoxData.setTitle(parseRawContent(PROMO_TITLE, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_HEADLINE)) {
            promoBoxData.setHeadline(parseRawContent(PROMO_HEADLINE, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_INTRO)) {
            promoBoxData.setIntro(parseRawContent(PROMO_INTRO, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_LINK)) {
            promoBoxData.setLink(parseRawContent(PROMO_LINK, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_IMAGE)) {
            promoBoxData.setImageFileRef(nextStartElement.getAttributeByName(QName.valueOf("fileref")).getValue());
        }
    }
    
    private String parseRawContent(String elementName, XMLEventReader xmlEventReader) {
        rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(elementName, xmlEventReader);
        } catch (XMLStreamException e) {
            return null;
        }
    }

}
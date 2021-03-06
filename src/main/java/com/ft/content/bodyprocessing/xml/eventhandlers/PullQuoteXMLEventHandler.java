package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;

public class PullQuoteXMLEventHandler extends AsideBaseXMLEventHandler<PullQuoteData> {

    private static final String WEB_PULL_QUOTE_ELEMENT_NAME = "web-pull-quote";
    private static final String WEB_PULL_QUOTE_TYPE = "pullQuote";

    private XmlParser<PullQuoteData> pullQuoteXMLParser;

    public PullQuoteXMLEventHandler(XmlParser<PullQuoteData> pullQuoteXMLParser, AsideElementWriter asideElementWriter) {
        super(asideElementWriter);
        notNull(pullQuoteXMLParser, "pullQuoteXMLParser cannot be null");
        
        this.pullQuoteXMLParser = pullQuoteXMLParser;
    }

    @Override
    String getElementName() {
       return WEB_PULL_QUOTE_ELEMENT_NAME;
    }

    @Override
    String getType(PullQuoteData dataType) {
        return WEB_PULL_QUOTE_TYPE;
    }

    @Override
    void transformFieldContentToStructuredFormat(PullQuoteData dataBean, BodyProcessingContext bodyProcessingContext) {
        pullQuoteXMLParser.transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);        
    }

    @Override
    PullQuoteData parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        return pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
    }

}

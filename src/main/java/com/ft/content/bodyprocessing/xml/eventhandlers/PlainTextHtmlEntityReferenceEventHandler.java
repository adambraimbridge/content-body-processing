package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.writer.BodyWriter;

import javax.xml.stream.events.EntityReference;

public class PlainTextHtmlEntityReferenceEventHandler extends HtmlEntityReferenceEventHandler {

    @Override
    protected void handleUnknownEntity(EntityReference event, BodyWriter eventWriter) {
        eventWriter.write(getEntityAsEscapedUnicode(event));
    }
}

package com.ft.content.bodyprocessing.xml.eventhandlers;


import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.PullQuote;
import com.ft.api.ucm.model.v1.PullQuoteFields;

public class PullQuoteData extends BaseData implements AssetAware {

    private String quoteText;
    private String quoteSource;

    public String getQuoteText() {
        return quoteText;
    }

    public String getQuoteSource() {
        return quoteSource;
    }

    public void setQuoteText(String quoteText) {
       this.quoteText = quoteText; 
    }

    public void setQuoteSource(String quoteSource) {
        this.quoteSource = quoteSource;
    }

    @Override
    public boolean isAllRequiredDataPresent() {
       return containsValidData(this.quoteText) || containsValidData(this.quoteSource);
    }

    @Override
    public Asset getAsset() throws BodyProcessingException {
       PullQuote pullQuote = null;
       if(this.isAllRequiredDataPresent()) {
           pullQuote = new PullQuote();
           PullQuoteFields fields = new PullQuoteFields(nullIfEmpty(this.quoteText), nullIfEmpty(this.quoteSource));
           pullQuote.setFields(fields);
           return pullQuote;
       }
       throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);
    }

}

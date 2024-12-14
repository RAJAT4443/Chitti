package com.Chitti.AiVoiceMail.dtos;

public class AzureResponsePhrase {
    private String text;

    public AzureResponsePhrase() {
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

}


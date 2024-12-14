package com.Chitti.AiVoiceMail.dtos;

import java.util.List;

public class OpenAiResponseDto {

    private String id;
    private String object;
    private List<Choice> choices;

    public static class Choice {
        private String text;
        private OpenAiMessageDto message;

        public String getText() {
            return text != null ? text : message.getContent();
        }
    }

    public String getFirstChoiceText() {
        return choices != null && !choices.isEmpty() ? choices.get(0).getText() : "";
    }

}

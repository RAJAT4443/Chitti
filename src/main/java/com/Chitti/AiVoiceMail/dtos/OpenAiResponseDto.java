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

        public OpenAiMessageDto getMessage() {
            return message;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setMessage(OpenAiMessageDto message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Choice{" +
                    "text='" + text + '\'' +
                    ", message=" + message +
                    '}';
        }
    }

    public String getFirstChoiceText() {
        return choices != null && !choices.isEmpty() ? choices.get(0).getText() : "";
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "OpenAiResponseDto{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", choices=" + choices +
                '}';
    }
}

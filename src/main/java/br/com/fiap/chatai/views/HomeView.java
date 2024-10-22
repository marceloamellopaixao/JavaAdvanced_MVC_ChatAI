package br.com.fiap.chatai.views;

import br.com.fiap.chatai.ChatService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Route("/")
public class HomeView extends VerticalLayout {
    private final ChatService chatService;
    private MessageList messageList = new MessageList();
    private TextField textField = new TextField();

    public HomeView(ChatService chatService) {

        add(new H1("Chat com professor de Ciências"));

        textField.setPlaceholder("digite uma mensagem");
        textField.setClearButtonVisible(true);
        textField.setPrefixComponent(VaadinIcon.CHAT.create());
        textField.setWidthFull();
        textField.addKeyDownListener(Key.ENTER, event -> {
            sendMessage();
        });

        Button button = new Button("Enviar");
        button.setIcon(VaadinIcon.PAPERPLANE.create());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(clickEvent -> {
            sendMessage();
        });

        var messagePanel = new HorizontalLayout(textField, button);
        messagePanel.setWidthFull();

        messageList.getStyle().setBackground("#3a4b61");
        messageList.getStyle().setBorderRadius("20");
        messageList.setHeightFull();

        var scroller = new Scroller(messageList);
        scroller.setWidthFull();
        scroller.setHeightFull();

        setHeightFull();

        add(messagePanel);
        add(scroller);
        this.chatService = chatService;
    }

    private void sendMessage(){
        // Notification.show("enviando mensagem...");
        String message = textField.getValue();
        addMessage(message, "Marcelo Augusto", 1);

        String response = chatService.sendToAi(message);
        addMessage(response, "Professor Gepeto", 2);
        textField.clear();
    }

    private void addMessage(String message, String user, int color){
        MessageListItem messageListItem = new MessageListItem(
                message,
                Instant.now(),
                user
        );
        messageListItem.setUserColorIndex(color);
        var currentMessageList = new ArrayList<>(messageList.getItems());
        currentMessageList.add(messageListItem);
        messageList.setItems(currentMessageList);
    }
}
package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.response_dto.LinkResponse;
import edu.shared_dto.response_dto.ListLinksResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.commands.Answers.ANSWER_TO_UNREGISTERED_USER;
import static edu.java.bot.commands.Answers.EMPTY_LINKS_LIST;
import static edu.java.bot.commands.Answers.YOUR_LIST;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    public static final String NAME = "/list";
    public static final String DESCRIPTION = "показать список отслеживаемых ссылок";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(@NotNull Update update, Object scrapperResponse) {
        long chatId = update.message().chat().id();
        List<LinkResponse> linkResponseList;
        int size;
        try {
            linkResponseList = ((ListLinksResponse) scrapperResponse).links();
            size = ((ListLinksResponse) scrapperResponse).size();
            if (size == 0) {
                return new SendMessage(chatId, EMPTY_LINKS_LIST);
            } else {
                StringBuilder links = new StringBuilder();
                links.append(YOUR_LIST);
                for (int i = 0; i < size; i++) {
                    String url = linkResponseList.get(i).url().toString();
                    links.append(url).append("\n\n");
                }
                return new SendMessage(chatId, String.valueOf(links));
            }
        } catch (Exception e) {
            return new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER);
        }
    }
}

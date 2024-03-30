package edu.java.bot.commands;

public abstract class Answers {

    public static final String ALREADY_REGISTERED = "Вы уже зарегистрированы в боте!";
    public static final String ALREADY_TRACKING =
        "Ошибка! Ссылка уже отслеживается! Используйте команду заново!";
    public static final String ANSWER_TO_UNREGISTERED_USER =
        "Ошибка! Сначала зарегистрируйтесь с помощью команды " + StartCommand.NAME;
    public static final String EMPTY_LINKS_LIST = "Список ваших отслеживаемых ссылок пустой!";
    public static final String INAPPROPRIATE_LINK_TRACK =
        "Отслеживание такой ссылки не поддерживается!";
    public static final String INVALID_COMMAND = "Не удалось распознать команду!";
    public static final String INVALID_URL = "Ошибка при вводе URL! Используйте команду заново!";
    public static final String INPUT_URL = "Введите ссылку:";
    public static final String NO_TRACKING =
        "Ошибка! Такой ссылки нет в списке отслеживаемых! Используйте команду заново!";
    public static final String SOMETHING_WENT_WRONG = "Что-то пошло не так!";
    public static final String SUCCESSFUL_REGISTRATION = "Вы зарегистрировались в боте!";
    public static final String SUCCESSFUL_TRACKING = "Ссылка успешно отслеживается!";
    public static final String SUCCESSFUL_UNTRACKING = "Ссылка больше не отслеживается!";
    public static final String USE_TRACK_OR_UNTRACK = "Используйте команды " + TrackCommand.NAME
        + " или " + UntrackCommand.NAME + " для отслеживания/удаления ссылки!";
    public static final String YOUR_LIST = "Список ваших отслеживаемых ссылок:\n";
}

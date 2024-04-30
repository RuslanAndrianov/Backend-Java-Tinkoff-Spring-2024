/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.jooq.tables.Chats;
import edu.java.scrapper.domain.jooq.tables.ChatsToLinks;
import edu.java.scrapper.domain.jooq.tables.Links;
import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in the default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Tables {

    /**
     * The table <code>CHATS</code>.
     */
    public static final Chats CHATS = Chats.CHATS;

    /**
     * The table <code>CHATS_TO_LINKS</code>.
     */
    public static final ChatsToLinks CHATS_TO_LINKS = ChatsToLinks.CHATS_TO_LINKS;

    /**
     * The table <code>LINKS</code>.
     */
    public static final Links LINKS = Links.LINKS;
}
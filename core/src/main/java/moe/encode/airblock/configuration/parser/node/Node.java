package moe.encode.airblock.configuration.parser.node;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Represents a simple node.
 */
public abstract class Node implements Serializable {

    /**
     * Default constructor for node implementations only.
     */
    Node() {}

    /**
     * Contains the comment.
     */
    @Getter
    @Setter
    @Nullable
    private String[] comment = new String[0];
}

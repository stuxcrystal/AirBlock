package moe.encode.airblock.configuration.parser.node;

import lombok.*;

import java.util.List;

/**
 * Represents a list.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public final class ListNode extends Node {

    @Getter
    @Setter
    public List<Node> nodes;

}

package net.stuxcrystal.airblock.configuration.parser.node;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains a map node.
 */
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = true)
@ToString
public final class MapNode extends Node {

    @Getter
    @Setter
    private Map<Node, Node> nodes = new LinkedHashMap<Node, Node>();

}

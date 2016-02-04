package moe.encode.airblock.configuration.parser.node;

import lombok.*;

/**
 * A node that contains simple data.
 */
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode(callSuper = true)
public final class DataNode extends Node {

    /**
     * The data node.
     */
    @Getter
    @Setter
    @NonNull
    private String data;



}

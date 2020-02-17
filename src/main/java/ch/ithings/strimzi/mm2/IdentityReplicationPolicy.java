package ch.ithings.strimzi.mm2;

import java.util.Map;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.mirror.ReplicationPolicy;

import java.util.regex.Pattern;
import org.apache.kafka.connect.mirror.MirrorClientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tpham
 */
public class IdentityReplicationPolicy implements ReplicationPolicy, Configurable {

    private static final Logger log = LoggerFactory.getLogger(IdentityReplicationPolicy.class);

    // In order to work with various metrics stores, we allow custom separators.
    public static final String SEPARATOR_CONFIG = MirrorClientConfig.REPLICATION_POLICY_SEPARATOR;
    public static final String SEPARATOR_DEFAULT = "";

    private String separator = SEPARATOR_DEFAULT;
    private Pattern separatorPattern = Pattern.compile(Pattern.quote(SEPARATOR_DEFAULT));

    /**
     * Configure this class with the given key-value pairs
     */
    @Override
    public void configure(Map<String, ?> props) {
        if (props.containsKey(SEPARATOR_CONFIG)) {
            separator = (String) props.get(SEPARATOR_CONFIG);
            log.info("Using custom remote topic separator: '{}'", separator);
            separatorPattern = Pattern.compile(Pattern.quote(separator));
        }
    }

    /** How to rename remote topics; generally should be like us-west.topic1. */
    @Override
    public String formatRemoteTopic(String sourceClusterAlias, String topic) {
        // return sourceClusterAlias + separator + topic;
        return topic;
    }

    /** Source cluster alias of given remote topic, e.g. "us-west" for "us-west.topic1".
        Returns null if not a remote topic.
    */
    @Override
    public String topicSource(String topic) {
        return null;
    }

    /** Name of topic on the source cluster, e.g. "topic1" for "us-west.topic1".
        Topics may be replicated multiple hops, so the immediately upstream topic
        may itself be a remote topic.
        Returns null if not a remote topic.
    */
    @Override
    public String upstreamTopic(String topic) {
        return topicSource(topic);
    }
    
    /** The name of the original source-topic, which may have been replicated multiple hops.
        Returns the topic if it is not a remote topic.
    */
    //@Override
    //public String originalTopic(String topic);

    /** Internal topics are never replicated. */
    //@Override
    //public boolean isInternalTopic(String topic);

}

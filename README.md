# kafka-solr-sink-connector
This is simple Java based solr sink kafka connector, that takes plain json data from kafka topic and push to solr, uses solr cloud.

Note: for now only plain JSON data is supported and keep schemas.enable=false for value converter.

Refer/Use configuration: <a href="https://github.com/bkatwal/kafka-solr-sink-connector/tree/master/config">config files</a>

Mandatory Fields:

<pre>
<code>
topics=your toipic name
  
solr.collection=your solr collection name
  
solr.zkhosts=comma separated zookeeper hosts

  example: localhost:2181,localhost:2183,localhost:2182
 </code>
</pre>

This connector can consume only JSON type object from kafka topic. Further, can be used for both delete and insert of document. To delete pass additional field `_delete_` in values(no need to maintain this field in solr/solr schema file), this field will be removed before indexing data to solr. Based on boolean values(true/false) in `_delete_`, delete/insert operation is triggered

# kafka-solr-sink-connector
This is simple JSON based solr sink kafka connector, uses solr cloud

Refer/Use configuration: https://github.com/bkatwal/kafka-solr-sink-connector/tree/master/config

Mandatory Fields:

<pre>
<code>
topics=your toipic name
  
solr.collection=your solr collection name
  
solr.zkhosts=comma separated zookeeper hosts

  example: localhost:2181,localhost:2183,localhost:2182
 </code>
</pre>

This connector can consume only JSON type object from kafka topic. Further, can be used for both delete and insert of document. To delete pass additional field "`_delete_`"(no need to maintain this in solr/solr schema file) in values, this field will be removed before indexing data to solr. Based on boolean values(true/false) in `_delete_`, delete/insert operation is triggered

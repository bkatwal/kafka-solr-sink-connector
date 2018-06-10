# kafka-solr-sink-connector
This is simple JSON based solr sink kafka connector, uses solr cloud

Refer/Use configuration: https://github.com/bkatwal/kafka-solr-sink-connector/tree/master/config

Mandatory Fields:

topics=\<your toipic name\>
  
solr.collection=
<your solr collection name>
  
solr.zkhosts=
<comma separated zookeeper hosts>
  
  example: localhost:2181,localhost:2183,localhost:2182
  
This connector can consume only JSON type object from kafka topic. Further, can be used for both delete and insert of document. To delete pass additional field "\_delete\_" in values. Based on boolean values(true/false) in \_delete\_, delete/insert operation is triggered

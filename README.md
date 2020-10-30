[![Build Status](https://travis-ci.com/bkatwal/kafka-solr-sink-connector.png)](https://travis-ci.com/github/kafka-solr-sink-connector)

# kafka-solr-sink-connector
This is simple Java based solr sink kafka connector, that takes plain json data from kafka topic and push to solr, both solr cloud and standalone mode supported.

Note: Only JSON data is supported and keep `schemas.enable=false` for value converter.

### Configs Description: 

Config Name|Description|Config Value|Is Mandatory?|
-----------|-----------|------------|----|
topics|topic to listen to|topic name|Yes|
solr.collection|Solr Collection name where topic data needs to be pushed|Collection Name|Yes|
solr.mode|Mode on which solr is running, pass solr node url|CLOUD or STANDALONE|Yes|
solr.url|If `solr.mode` is `CLOUD`, pass comma seperated zookeeper url else pass standalone solr server url|url|Yes|
connector.class|Connector class name|com.bkatwal.kafkaproject.SolrSinkConnector|Yes|
commit.within.ms|commit within ms value for solr update, if none passes defaults to 10 ms|int value|No|
solr.writes_per_sec|Throttle writes per second. Default value = 100|double value|No|
error.ignore_bad_offset|Ignore and commit any bad message, use this option with dead letter queue|boolean value. Default value = 100|No|

##### Refer/Use configuration: <a href="https://github.com/bkatwal/kafka-solr-sink-connector/tree/master/config">config files</a>

### Features Supported:
1. Json Data 
2. One level of child document update supported. Just pass, additional field, `_childDocuments_` with the parent doc.
3. Doc deletion: To delete pass additional field `_delete_` with value `true` in your record(no need to maintain this field in solr/solr schema file), this field will be removed before indexing data to solr. Based on boolean values(true/false) in `_delete_`, delete/insert operation is triggered
   OR
   Pass `kafka header` with the record/message. Use header key name as `_delete_` and value as `true`.
4. Dynamic fields update. Pass a Map field in json doc. The field name will be used as prefix, so set dynamic field in managed_schema accordingly. Example:
```
{
    "field1": "doc1",
    "id":"1",
    "field2":"dome val,
    "dynamicField":
    {
      "df1":"val1",
      "df2":"val2",
      "df3":"val3"
    }
  }
  
  This will be transformed in solr as:
  dynamicField_df1 : val1, dynamicField_df2 : val2, dynamicField_df3 : val3 
```
You can have any field name for the dynamic field, just have this defined in managed_schema. So, for above to work, dynamic field need to be defined in managed_schema file as below: 
`<dynamicField name="dynamicField_*" type="string" indexed="true" stored="true"/>`

### Deploy Steps:
1. build with: mvn clean package
2. In target look for directory bkatwal-kafka-connect-solr-sink-< version >. Copy this directory to plugins path in `connector.properties` file.
  
##### or download deployable artifact from : https://www.confluent.io/hub/bkatwal/bkatwal-kafka-connect-solr-sink

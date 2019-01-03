### GitLab [![build status](https://gitlab.com/bikas.katwal10/kafka-solr-sink-connector/badges/master/build.svg)](https://gitlab.com/bikas.katwal10/kafka-solr-sink-connector/master)

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

##### Refer/Use configuration: <a href="https://github.com/bkatwal/kafka-solr-sink-connector/tree/master/config">config files</a>

### Features Supported:
1. Json Data 
2. One level of child document update supported. Just pass, additional field, `_childDocuments_` with the parent doc.
3. Deleting a document is supported: To delete pass additional field `_delete_` with value `true` in your record(no need to maintain this field in solr/solr schema file), this field will be removed before indexing data to solr. Based on boolean values(true/false) in `_delete_`, delete/insert operation is triggered
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
You can have any field name for dynamic field, just have this defined in managed_schema. So, for above to work, dynamic field meeds to be defined in managed_schema file as below: 
`<dynamicField name="dynamicField_*" type="string" indexed="true" stored="true"/>`

### Deploy Steps:
1. build with: mvn clean package
2. In target look for directory bkatwal-kafka-connect-solr-sink-< version >. Copy this directory to plugins path.
  
##### or download deployable artifact from : https://www.confluent.io/connector/solr-sink-connector/

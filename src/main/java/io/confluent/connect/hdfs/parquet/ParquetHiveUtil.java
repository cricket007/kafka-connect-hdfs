/**
 * Copyright 2015 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.confluent.connect.hdfs.parquet;

import io.confluent.connect.storage.hive.HiveMetaStore;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.kafka.connect.data.Schema;

import java.util.List;

import io.confluent.connect.hdfs.HdfsSinkConnectorConfig;
import io.confluent.connect.hdfs.partitioner.Partitioner;
import io.confluent.connect.storage.errors.HiveMetaStoreException;
import io.confluent.connect.storage.hive.HiveSchemaConverter;

public class ParquetHiveUtil extends io.confluent.connect.storage.hive.HiveUtil {

  public ParquetHiveUtil(HdfsSinkConnectorConfig conf, HiveMetaStore hiveMetaStore) {
    super(conf, hiveMetaStore);
  }

  @Deprecated
  public void createTable(
      String database,
      String tableName,
      Schema schema,
      Partitioner partitioner
  ) throws HiveMetaStoreException {
    Table table = constructTable(database, tableName, schema, partitioner);
    hiveMetaStore.createTable(table);
  }

  @Override
  public void createTable(
          String database,
          String tableName,
          Schema schema,
          io.confluent.connect.storage.partitioner.Partitioner<FieldSchema> partitioner
  ) throws HiveMetaStoreException {
    Table table = constructTable(database, tableName, schema, partitioner);
    hiveMetaStore.createTable(table);
  }

  @Override
  public void alterSchema(String database, String tableName, Schema schema) {
    Table table = hiveMetaStore.getTable(database, tableName);
    List<FieldSchema> columns = HiveSchemaConverter.convertSchema(schema);
    table.setFields(columns);
    hiveMetaStore.alterTable(table);
  }

  @Override
  public String getInputFormat() {
    String newClass = "org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat";
    String oldClass = "parquet.hive.DeprecatedParquetInputFormat";

    try {
      Class.forName(newClass);
      return newClass;
    } catch (ClassNotFoundException ex) {
      return oldClass;
    }
  }

  @Override
  public String getOutputFormat() {
    String newClass = "org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat";
    String oldClass = "parquet.hive.DeprecatedParquetOutputFormat";

    try {
      Class.forName(newClass);
      return newClass;
    } catch (ClassNotFoundException ex) {
      return oldClass;
    }
  }

  @Override
  public String getSerde() {
    String newClass = "org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe";
    String oldClass = "parquet.hive.serde.ParquetHiveSerDe";

    try {
      Class.forName(newClass);
      return newClass;
    } catch (ClassNotFoundException ex) {
      return oldClass;
    }
  }
}

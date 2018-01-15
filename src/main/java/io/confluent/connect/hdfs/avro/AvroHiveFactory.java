/*
 * Copyright 2016 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file exceptin compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confluent.connect.hdfs.avro;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.confluent.connect.storage.common.StorageCommonConfig;
import org.apache.kafka.common.config.AbstractConfig;

import io.confluent.connect.avro.AvroData;
import io.confluent.connect.hdfs.hive.HiveMetaStore;

@Deprecated
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class AvroHiveFactory extends io.confluent.connect.storage.hive.avro.AvroHiveFactory {

  public AvroHiveFactory(AvroData avroData) {
    super(avroData);
  }

  @Deprecated
  public io.confluent.connect.storage.hive.avro.AvroHiveUtil createHiveUtil(
      AbstractConfig conf,
      HiveMetaStore hiveMetaStore
  ) {
    return new io.confluent.connect.storage.hive.avro
            .AvroHiveUtil((StorageCommonConfig) conf, avroData, hiveMetaStore);
  }
}

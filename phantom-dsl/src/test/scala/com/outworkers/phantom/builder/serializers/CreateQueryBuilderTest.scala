/*
 * Copyright 2013 - 2017 Outworkers Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.outworkers.phantom.builder.serializers

import java.util.concurrent.TimeUnit

import com.twitter.conversions.storage._
import com.twitter.util.{Duration => TwitterDuration}
import com.outworkers.phantom.builder.QueryBuilder
import com.outworkers.phantom.builder.query.SerializationTest
import com.outworkers.phantom.builder.syntax.CQLSyntax
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.tables.TestDatabase
import org.joda.time.Seconds
import org.scalatest.{FreeSpec, Matchers}

import scala.concurrent.duration._

class CreateQueryBuilderTest extends FreeSpec with Matchers with SerializationTest {

  val BasicTable = TestDatabase.basicTable
  final val DefaultTtl = 500
  final val OneDay = 86400

  "The CREATE query builder" - {
    "should allow specifying table creation options" - {

      "serialise a simple create query with a SizeTieredCompactionStrategy and no compaction strategy options set" in {

        val qb = BasicTable.create.`with`(compaction eqs SizeTieredCompactionStrategy).qb.queryString

        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH compaction = {'class'" +
          ": 'SizeTieredCompactionStrategy'}"
      }

      "serialise a simple create query with a SizeTieredCompactionStrategy and 1 compaction strategy options set" in {

        val qb = BasicTable.create.`with`(compaction eqs LeveledCompactionStrategy.sstable_size_in_mb(50)).qb.queryString

        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH compaction = {'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb': 50}"
      }

      "serialise a simple create query with a SizeTieredCompactionStrategy and 1 compaction strategy options set and a compression strategy set" in {
        val qb = BasicTable.create
          .`with`(compaction eqs LeveledCompactionStrategy.sstable_size_in_mb(50))
          .and(compression eqs LZ4Compressor.crc_check_chance(0.5))
        .qb.queryString

        qb shouldEqual """CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH compaction = {'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb': 50} AND compression = {'sstable_compression': 'LZ4Compressor', 'crc_check_chance': 0.5}"""
      }

      "add a comment option to a create query" in {
        val qb = BasicTable.create
          .`with`(comment eqs "testing")
          .qb.queryString

        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH comment = 'testing'"
      }

      "allow specifying a read_repair_chance clause" in {
        val qb = BasicTable.create.`with`(read_repair_chance eqs 5D).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH read_repair_chance = 5.0"
      }

      "allow specifying a dclocal_read_repair_chance clause" in {
        val qb = BasicTable.create.`with`(dclocal_read_repair_chance eqs 5D).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH dclocal_read_repair_chance = 5.0"
      }

      "allow specifying a replicate_on_write clause" in {
        val qb = BasicTable.create.`with`(replicate_on_write eqs true).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH replicate_on_write = true"
      }

      "allow specifying a custom gc_grace_seconds clause" in {
        val qb = BasicTable.create.`with`(gc_grace_seconds eqs 5.seconds).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH gc_grace_seconds = 5"
      }

      "allow specifying larger custom units as gc_grace_seconds" in {
        val qb = BasicTable.create.`with`(gc_grace_seconds eqs 1.day).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH gc_grace_seconds = 86400"
      }

      "allow specifying custom gc_grade_seconds using the Joda Time ReadableInstant and Second API" in {
        val qb = BasicTable.create.`with`(gc_grace_seconds eqs Seconds.seconds(OneDay)).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH gc_grace_seconds = 86400"
      }

      "allow specifying a bloom_filter_fp_chance using a Double param value" in {
        val qb = BasicTable.create.`with`(bloom_filter_fp_chance eqs 5D).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH " +
          "bloom_filter_fp_chance = 5.0"
      }
    }

    "should allow specifying cache strategies " - {
      "specify Cache.None as a cache strategy" in {
        val qb = BasicTable.create.`with`(caching eqs Cache.None()).qb.queryString

        val baseQuery = "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3))"

        if (session.v4orNewer) {
          qb shouldEqual s"$baseQuery WITH caching = {'keys': 'none', 'rows_per_partition': 'none'}"
        } else {
          qb shouldEqual s"$baseQuery WITH caching = 'none'"
        }
      }

      "specify Cache.KeysOnly as a caching strategy" in {
        val qb = BasicTable.create.`with`(caching eqs Cache.KeysOnly()).qb.queryString

        if (session.v4orNewer) {
          qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text," +
            " PRIMARY KEY (id, id2, id3)) WITH caching = {'keys': 'all', 'rows_per_partition': 'none'}"
        } else {
          qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text," +
            " PRIMARY KEY (id, id2, id3)) WITH caching = 'keys_only'"
        }
      }

      "specify Cache.RowsOnly as a caching strategy" in {
        val qb = BasicTable.create.`with`(caching eqs Cache.RowsOnly()).qb.queryString

        if (session.v4orNewer) {
          qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text," +
            " PRIMARY KEY (id, id2, id3)) WITH caching = {'rows_per_partition': 'all'}"
        } else {
          qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text," +
            " PRIMARY KEY (id, id2, id3)) WITH caching = 'rows_only'"
        }
      }

      "specify Cache.All as a caching strategy" in {
        val qb = BasicTable.create.`with`(caching eqs Cache.All()).qb.queryString
        val baseQuery = "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3))"

        if (session.v4orNewer) {
          qb shouldEqual s"$baseQuery WITH caching = {'keys': 'all', 'rows_per_partition': 'all'}"
        } else {
          qb shouldEqual s"$baseQuery WITH caching = 'all'"
        }

      }
    }

    "should allow specifying a default_time_to_live" - {
      "specify a default time to live using a Long value" in {
        val qb = BasicTable.create.`with`(default_time_to_live eqs DefaultTtl.toLong).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH default_time_to_live = 500"
      }

      "specify a default time to live using a org.joda.time.Seconds value" in {
        val qb = BasicTable.create.`with`(default_time_to_live eqs Seconds.seconds(DefaultTtl)).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH default_time_to_live = 500"
      }

      "specify a default time to live using a scala.concurrent.duration.FiniteDuration value" in {
        val qb = BasicTable.create.`with`(default_time_to_live eqs FiniteDuration(DefaultTtl, TimeUnit.SECONDS)).qb.queryString
        qb shouldEqual "CREATE TABLE phantom.basicTable (id uuid, id2 uuid, id3 uuid, placeholder text, PRIMARY KEY (id, id2, id3)) WITH default_time_to_live = 500"
      }
    }

    "should allow specifying a clustering order" - {
      "specify a single column clustering order with ascending ordering" in {
        val column = ("test", CQLSyntax.Ordering.asc) :: Nil

        val qb = QueryBuilder.Create.clusteringOrder(column).queryString

        qb shouldEqual "CLUSTERING ORDER BY (test ASC)"
      }

      "specify a single column clustering order with descending ordering" in {
        val column = ("test", CQLSyntax.Ordering.desc) :: Nil

        val qb = QueryBuilder.Create.clusteringOrder(column).queryString

        qb shouldEqual "CLUSTERING ORDER BY (test DESC)"
      }

      "specify multiple columns and preserve ordering" in {
        val column1 = ("test", CQLSyntax.Ordering.asc)
        val column2 = ("test2", CQLSyntax.Ordering.desc)

        val columns = List(column1, column2)

        val qb = QueryBuilder.Create.clusteringOrder(columns).queryString

        qb shouldEqual "CLUSTERING ORDER BY (test ASC, test2 DESC)"
      }
    }

    "should allow generating secondary indexes based on trait mixins" - {
      "specify a secondary index on a non-map column" in {
        val qb = QueryBuilder.Create.index("t", "k", "col").queryString

        qb shouldEqual "CREATE INDEX IF NOT EXISTS t_col_idx ON k.t(col)"
      }

      "specify a secondary index on a map column for the keys of a map column" in {
        val qb = QueryBuilder.Create.mapIndex("t", "k", "col").queryString

        qb shouldEqual "CREATE INDEX IF NOT EXISTS t_col_idx ON k.t(keys(col))"
      }
    }

  }
}
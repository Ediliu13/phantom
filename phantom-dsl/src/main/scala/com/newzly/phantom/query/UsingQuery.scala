/*
 * Copyright 2013 newzly ltd.
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
package com.newzly.phantom.query

import com.datastax.driver.core.querybuilder.{QueryBuilder, Insert, Using}
import com.newzly.phantom.AbstractColumn
import com.newzly.phantom.CassandraTable
import com.newzly.phantom.{ AbstractColumn, CassandraTable }

class UsingQuery[T <: CassandraTable[T, R], R](table: T, val qb: QueryBuilder) extends ExecutableStatement {

  /*
  def ttl[RR](c: T => AbstractColumn[RR], value: RR)(expiry: Int): UsingQuery = {
    val col = c(table)
    new UsingQuery[T, R](table, qb.ttl(expiry))
  }*/
}
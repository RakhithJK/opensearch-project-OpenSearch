/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.script.expression;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.DoubleValues;
import org.opensearch.index.fielddata.IndexFieldData;
import org.opensearch.index.fielddata.LeafGeoPointFieldData;
import org.opensearch.index.fielddata.MultiGeoPointValues;

import java.io.IOException;

/**
 * ValueSource to return longitudes as a double "stream" for geopoint fields
 */
final class GeoLongitudeValueSource extends FieldDataBasedDoubleValuesSource {

    GeoLongitudeValueSource(IndexFieldData<?> fieldData) {
        super(fieldData);
    }

    @Override
    public DoubleValues getValues(LeafReaderContext leaf, DoubleValues scores) {
        LeafGeoPointFieldData leafData = (LeafGeoPointFieldData) fieldData.load(leaf);
        final MultiGeoPointValues values = leafData.getGeoPointValues();
        return new DoubleValues() {
            @Override
            public double doubleValue() throws IOException {
                return values.nextValue().getLon();
            }

            @Override
            public boolean advanceExact(int doc) throws IOException {
                return values.advanceExact(doc);
            }
        };
    }

    @Override
    public int hashCode() {
        return 31 * getClass().hashCode() + fieldData.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GeoLongitudeValueSource other = (GeoLongitudeValueSource) obj;
        if (!fieldData.equals(other.fieldData)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "lon: field(" + fieldData.getFieldName() + ")";
    }
}

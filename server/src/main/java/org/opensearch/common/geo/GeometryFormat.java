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
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package org.opensearch.common.geo;

import org.opensearch.core.xcontent.ToXContent;
import org.opensearch.core.xcontent.XContentBuilder;
import org.opensearch.core.xcontent.XContentParser;

import java.io.IOException;
import java.text.ParseException;

/**
 * Geometry serializer/deserializer
 *
 * @opensearch.internal
 */
public interface GeometryFormat<ParsedFormat> {

    /**
     * The name of the format, for example 'wkt'.
     */
    String name();

    /**
     * Parser JSON representation of a geometry
     */
    ParsedFormat fromXContent(XContentParser parser) throws IOException, ParseException;

    /**
     * Serializes the geometry into its JSON representation
     */
    XContentBuilder toXContent(ParsedFormat geometry, XContentBuilder builder, ToXContent.Params params) throws IOException;

    /**
     * Serializes the geometry into a standard Java object.
     * <p>
     * For example, the GeoJson format returns the geometry as a map, while WKT returns a string.
     */
    Object toXContentAsObject(ParsedFormat geometry);
}

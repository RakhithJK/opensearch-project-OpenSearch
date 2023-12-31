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

package org.opensearch.example.customsettings;

import com.carrotsearch.randomizedtesting.annotations.Name;
import com.carrotsearch.randomizedtesting.annotations.ParametersFactory;

import org.opensearch.test.rest.yaml.ClientYamlTestCandidate;
import org.opensearch.test.rest.yaml.OpenSearchClientYamlSuiteTestCase;

/**
 * {@link ExampleCustomSettingsClientYamlTestSuiteIT} executes the plugin's REST API integration tests.
 * <p>
 * The tests can be executed using the command: ./gradlew :example-plugins:custom-settings:yamlRestTest
 * <p>
 * This class extends {@link OpenSearchClientYamlSuiteTestCase}, which takes care of parsing the YAML files
 * located in the src/yamlRestTest/resources/rest-api-spec/test/ directory and validates them against the
 * custom REST API definition files located in src/yamlRestTest/resources/rest-api-spec/api/.
 * <p>
 * Once validated, {@link OpenSearchClientYamlSuiteTestCase} executes the REST tests against a single node
 * integration cluster which has the plugin already installed by the Gradle build script.
 * </p>
 */
public class ExampleCustomSettingsClientYamlTestSuiteIT extends OpenSearchClientYamlSuiteTestCase {

    public ExampleCustomSettingsClientYamlTestSuiteIT(@Name("yaml") ClientYamlTestCandidate testCandidate) {
        super(testCandidate);
    }

    @ParametersFactory
    public static Iterable<Object[]> parameters() throws Exception {
        // The test executes all the test candidates by default
        // see OpenSearchClientYamlSuiteTestCase.REST_TESTS_SUITE
        return OpenSearchClientYamlSuiteTestCase.createParameters();
    }
}

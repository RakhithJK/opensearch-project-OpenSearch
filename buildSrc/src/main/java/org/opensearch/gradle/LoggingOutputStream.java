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

package org.opensearch.gradle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Writes data passed to this stream as log messages.
 * <p>
 * The stream will be flushed whenever a newline is detected.
 * Allows setting an optional prefix before each line of output.
 */
public abstract class LoggingOutputStream extends OutputStream {
    /** The starting length of the buffer */
    private static final int DEFAULT_BUFFER_LENGTH = 4096;

    /** The buffer of bytes sent to the stream */
    private byte[] buffer = new byte[DEFAULT_BUFFER_LENGTH];

    /** Offset of the start of unwritten data in the buffer */
    private int start = 0;

    /** Offset of the end (semi-open) of unwritten data in the buffer */
    private int end = 0;

    @Override
    public void write(final int b) throws IOException {
        if (b == 0) {
            return;
        }
        if (b == '\n') {
            // always flush with newlines instead of adding to the buffer
            flush();
            return;
        }

        if (end == buffer.length) {
            if (start != 0) {
                // first try shifting the used buffer back to the beginning to make space
                int len = end - start;
                System.arraycopy(buffer, start, buffer, 0, len);
                start = 0;
                end = len;
            } else {
                // otherwise extend the buffer
                buffer = Arrays.copyOf(buffer, buffer.length + DEFAULT_BUFFER_LENGTH);
            }
        }

        buffer[end++] = (byte) b;
    }

    @Override
    public void flush() {
        if (end == start) {
            return;
        }
        logLine(new String(buffer, start, end - start));
        start = end;
    }

    protected abstract void logLine(String line);
}

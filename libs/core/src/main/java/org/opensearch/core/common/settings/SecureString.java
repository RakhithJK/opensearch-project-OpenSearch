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

package org.opensearch.core.common.settings;

import org.opensearch.common.annotation.PublicApi;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A String implementations which allows clearing the underlying char array.
 *
 * @opensearch.api
 */
@PublicApi(since = "1.0.0")
public final class SecureString implements CharSequence, Closeable {

    private char[] chars;

    /**
     * Constructs a new SecureString which controls the passed in char array.
     * <p>
     * Note: When this instance is closed, the array will be zeroed out.
     */
    public SecureString(char[] chars) {
        this.chars = Objects.requireNonNull(chars);
    }

    /**
     * Constructs a new SecureString from an existing String.
     * <p>
     * NOTE: This is not actually secure, since the provided String cannot be deallocated, but
     * this constructor allows for easy compatibility between new and old apis.
     *
     * @deprecated Only use for compatibility between deprecated string settings and new secure strings
     */
    @Deprecated
    public SecureString(String s) {
        this(s.toCharArray());
    }

    /** Constant time equality to avoid potential timing attacks. */
    @Override
    public synchronized boolean equals(Object o) {
        ensureNotClosed();
        if (this == o) return true;
        if (o == null || o instanceof CharSequence == false) return false;
        CharSequence that = (CharSequence) o;
        if (chars.length != that.length()) {
            return false;
        }

        int equals = 0;
        for (int i = 0; i < chars.length; i++) {
            equals |= chars[i] ^ that.charAt(i);
        }

        return equals == 0;
    }

    @Override
    public synchronized int hashCode() {
        return Arrays.hashCode(chars);
    }

    @Override
    public synchronized int length() {
        ensureNotClosed();
        return chars.length;
    }

    @Override
    public synchronized char charAt(int index) {
        ensureNotClosed();
        return chars[index];
    }

    @Override
    public SecureString subSequence(int start, int end) {
        throw new UnsupportedOperationException("Cannot get subsequence of SecureString");
    }

    /**
     * Convert to a {@link String}. This should only be used with APIs that do not take {@link CharSequence}.
     */
    @Override
    public synchronized String toString() {
        return new String(chars);
    }

    /**
     * Closes the string by clearing the underlying char array.
     */
    @Override
    public synchronized void close() {
        if (chars != null) {
            Arrays.fill(chars, '\0');
            chars = null;
        }
    }

    /**
     * Returns a new copy of this object that is backed by its own char array. Closing the new instance has no effect on the instance it
     * was created from. This is useful for APIs which accept a char array and you want to be safe about the API potentially modifying the
     * char array. For example:
     *
     * <pre>
     *     try (SecureString copy = secureString.clone()) {
     *         // pass thee char[] to a external API
     *         PasswordAuthentication auth = new PasswordAuthentication(username, copy.getChars());
     *         ...
     *     }
     * </pre>
     */
    @Override
    public synchronized SecureString clone() {
        ensureNotClosed();
        return new SecureString(Arrays.copyOf(chars, chars.length));
    }

    /**
     * Returns the underlying char[]. This is a dangerous operation as the array may be modified while it is being used by other threads
     * or a consumer may modify the values in the array. For safety, it is preferable to use {@link #clone()} and pass its chars to the
     * consumer when the chars are needed multiple times.
     */
    public synchronized char[] getChars() {
        ensureNotClosed();
        return chars;
    }

    /** Throw an exception if this string has been closed, indicating something is trying to access the data after being closed. */
    private void ensureNotClosed() {
        if (chars == null) {
            throw new IllegalStateException("SecureString has already been closed");
        }
    }
}

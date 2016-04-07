package org.cache2k;

/*
 * #%L
 * cache2k API only package
 * %%
 * Copyright (C) 2000 - 2016 headissue GmbH, Munich
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Object representing a cache entry. With the cache entry, it can be
 * checked whether a mapping in the cache is present, even if the cache
 * holds null or an exception. Entries can be retrieved by
 * {@link Cache#peekEntry(Object)} or {@link Cache#getEntry(Object)} or
 * via {@link Cache#iterator()}.
 *
 * <p>After retrieved, the entry instance does not change its values, even
 * if the value for its key is updated in the cache.
 *
 * <p>Design note: The cache is generally also aware of the time the
 * object will be refreshed next or when it will expire. This is not exposed
 * to applications by intention.
 *
 * @author Jens Wilke; created: 2014-03-18
 * @see Cache#peekEntry(Object)
 * @see Cache#getEntry(Object)
 * @see Cache#iterator()
 */
public interface CacheEntry<K, V> {

  /**
   * Key associated with this entry.
   */
  K getKey();

  /**
   * Value of the entry. The value may be null if nulls are allowed, or,
   * if an exception was thrown by the loader.
   */
  V getValue();

  /**
   * If not null a exception happened when the value was loaded and
   * the exception cannot be suppressed.
   */
  Throwable getException();

  /**
   * Time in millis the entry was last updated either by loading
   * or by a put.
   */
  long getLastModification();

}

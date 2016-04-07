package org.cache2k.integration;

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

import org.cache2k.PropagatedCacheException;

/**
 * Wraps loader exceptions. Exceptions occurring in the loader are usually cached, which
 * means, there can be multiple instances thrown for one loader exception. The cause
 * contains the original exceptions from the loader.
 * Wraps an application exception.
 *
 * If a cache receives an exception when loading a value it may propagate
 * the exception wrapped into this one to the caller. Whether propagation
 * occurs depends on the configuration and on the presence of valid data.
 *
 * @author Jens Wilke
 */
public class CacheLoaderException extends PropagatedCacheException {

  public CacheLoaderException(String _message, Throwable ex) {
    super(_message, ex);
  }

  public CacheLoaderException(Throwable ex) {
    super(ex);
  }

}
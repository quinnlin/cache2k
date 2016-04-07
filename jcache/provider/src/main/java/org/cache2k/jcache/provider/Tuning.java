package org.cache2k.jcache.provider;

/*
 * #%L
 * cache2k JCache JSR107 implementation
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

import org.cache2k.impl.util.TunableConstants;
import org.cache2k.impl.util.TunableFactory;

/**
 * Some global parameters for the cache2k JCache implementation.
 *
 * @author Jens Wilke
 */
public class Tuning extends TunableConstants {

  public static final Tuning GLOBAL = TunableFactory.get(Tuning.class);

  /**
   * Every access to an JMX value will flush the cache statistics. This is only needed for the TCK,
   * that it can check the correct counting.
   */
  public boolean flushStatisticsOnAccess = true;

  /**
   * Apply a "correction" to the statistics for the entity processor invokes. Needed for the TCK.
   */
  public boolean tweakStatisticsForEntityProcessor = true;

}

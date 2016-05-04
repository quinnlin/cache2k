package org.cache2k.core;

/*
 * #%L
 * cache2k core
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

import org.cache2k.CacheEntry;
import org.cache2k.integration.LoadExceptionInformation;
import org.cache2k.integration.ResiliencePolicy;

import java.util.Random;

/**
 * @author Jens Wilke
 */
public class DefaultResiliencePolicy<K,V> extends ResiliencePolicy<K,V> {

  /**
   * We use a common random instance. Since this is only called for an exception
   * we do not bother for contention.
   */
  static final Random SHARED_RANDOM = new Random();

  static final int RETRY_PERCENT_OF_RESILIENCE_DURATION = 10;
  static final int MIN_RETRY_INTERVAL = 1000;

  private double multiplier = 1.5;
  private double randomization = 0.5;
  private long resilienceDuration;
  private long maxRetryInterval;
  private long retryInterval;

  public double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(final double _multiplier) {
    multiplier = _multiplier;
  }

  public double getRandomization() {
    return randomization;
  }

  public void setRandomization(final double _randomization) {
    randomization = _randomization;
  }

  public long getResilienceDuration() {
    return resilienceDuration;
  }

  public long getMaxRetryInterval() {
    return maxRetryInterval;
  }

  public long getRetryInterval() { return retryInterval; }

  @Override
  public void init(final Context ctx) {
    resilienceDuration = ctx.getResilienceDurationMillis();
    maxRetryInterval = ctx.getMaxRetryIntervalMillis();
    retryInterval = ctx.getRetryIntervalMillis();
    if (resilienceDuration == -1) {
      if (ctx.getExpireAfterWriteMillis() == ETERNAL) {
        resilienceDuration = 0;
      } else {
        resilienceDuration = ctx.getExpireAfterWriteMillis();
      }
    } else {
      if (maxRetryInterval == -1) {
        maxRetryInterval = resilienceDuration;
      }
    }
    if (maxRetryInterval == -1 && retryInterval == -1) {
      maxRetryInterval = resilienceDuration;
    }
    if (retryInterval == -1) {
      retryInterval = resilienceDuration * RETRY_PERCENT_OF_RESILIENCE_DURATION / 100;
      retryInterval = Math.min(retryInterval, maxRetryInterval);
      retryInterval = Math.max(MIN_RETRY_INTERVAL, retryInterval);
    }
    if (retryInterval > maxRetryInterval) {
      maxRetryInterval = retryInterval;
    }
    if (maxRetryInterval > resilienceDuration && resilienceDuration != 0) {
      resilienceDuration = maxRetryInterval;
    }
  }

  /**
   * No timer needed if the settings enable immediate expiry or never expire.
   */
  public boolean isNoTimerNeeded() {
    return
        (resilienceDuration == 0 || resilienceDuration == Long.MAX_VALUE) &&
        (retryInterval == 0 || retryInterval == Long.MAX_VALUE);
  }

  @Override
  public long suppressExceptionUntil(final Object key,
                                     final LoadExceptionInformation exceptionInformation,
                                     final CacheEntry cachedContent) {
    if (resilienceDuration == 0) {
      return 0;
    }
    if (resilienceDuration == Long.MAX_VALUE) {
      return Long.MAX_VALUE;
    }
    long _maxSuppressUntil = exceptionInformation.getSinceTime() + resilienceDuration;
    long _deltaMs = calculateRetryDelta(exceptionInformation);
    return Math.min(exceptionInformation.getLoadTime() + _deltaMs, _maxSuppressUntil);
  }

  private long calculateRetryDelta(final LoadExceptionInformation exceptionInformation) {
    long _delta = (long)
      (retryInterval * Math.pow(multiplier, exceptionInformation.getRetryCount()));
    _delta -= SHARED_RANDOM.nextDouble() * randomization * _delta;
    return Math.min(_delta, maxRetryInterval);
  }

  @Override
  public long retryLoadAfter(final Object key,
                             final LoadExceptionInformation exceptionInformation) {
    if (retryInterval == 0) {
      return 0;
    }
    if (retryInterval == Long.MAX_VALUE) {
      return Long.MAX_VALUE;
    }
    return exceptionInformation.getLoadTime() + calculateRetryDelta(exceptionInformation);
  }

}

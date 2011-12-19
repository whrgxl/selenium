package org.openqa.selenium.testing;

import com.google.common.collect.Sets;

import org.openqa.selenium.Platform;
import org.openqa.selenium.testing.Ignore.Driver;
import org.openqa.selenium.testing.Ignore.NativeEventsEnabledState;

import java.util.Set;

public class IgnoreComparator {
  private NativeEventsEnabledState nativeEventsIgnoreState = NativeEventsEnabledState.ALL;
  private Set<Ignore.Driver> ignored = Sets.newHashSet();
  private Platform currentPlatform = Platform.getCurrent();

  // TODO(simon): reduce visibility
  public void addDriver(Driver driverToIgnore) {
    ignored.add(driverToIgnore);
  }

  public void setNativeEventsIgnoreState(NativeEventsEnabledState value) {
    this.nativeEventsIgnoreState = value;
  }

  public void setCurrentPlatform(Platform platform) {
    currentPlatform = platform;
  }

  private boolean shouldIgnoreBecauseOfNativeEvents(NativeEventsEnabledState nativeEvents) {
    return nativeEvents == NativeEventsEnabledState.ALL ||
           this.nativeEventsIgnoreState == NativeEventsEnabledState.ALL ||
           this.nativeEventsIgnoreState == nativeEvents;
  }

  public boolean shouldIgnore(Ignore ignoreAnnotation) {
    if (ignoreAnnotation == null) {
      return false;
    }

    if (ignoreAnnotation.value().length == 0) {
      return true;
    }

    for (Ignore.Driver value : ignoreAnnotation.value()) {
      if (ignored.contains(value) || value == Ignore.Driver.ALL) {
        for (Platform platform : ignoreAnnotation.platforms()) {
          if (platform.is(currentPlatform)) {
            return shouldIgnoreBecauseOfNativeEvents(ignoreAnnotation.nativeEvents());
          }
        }
      }
    }

    return false;
  }
}

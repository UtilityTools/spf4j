/*
 * Copyright 2018 SPF4J.
 *
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
 */
package org.spf4j.test.log;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Zoltan Farkas
 */
@ParametersAreNonnullByDefault
public interface LogConfig {

  List<LogHandler> getLogHandlers(String category, Level level);

  LogConfig add(String category, LogHandler handler);

  LogConfig remove(String category, LogHandler handler);

  default Level minRootLevel() {
    for (Level l : Level.values()) {
      List<LogHandler> rlh = getLogHandlers("", l);
      if (!rlh.isEmpty()) {
        return l;
      }
    }
    return Level.ERROR;
  }


}
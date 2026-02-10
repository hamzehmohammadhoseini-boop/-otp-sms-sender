#!/usr/bin/env sh

APP_HOME="$(cd "$(dirname "$0")" && pwd)"

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$CLASSPATH" ]; then
  echo "❌ gradle-wrapper.jar not found at $CLASSPATH"
  exit 1
fi

exec java -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"

#!/bin/bash

SCRIPT_DIR=$(cd $(dirname $0); pwd)

exec java -Xdock:name=ToshinforMacOS -Xdock:icon=$SCRIPT_DIR/logo/icon.icns -jar $SCRIPT_DIR/Toshin4Mac.jar
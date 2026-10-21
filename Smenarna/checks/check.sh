#!/usr/bin/env bash
set -eu
cd "$(dirname "$0")/.."
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
javac -encoding UTF-8 -d "$work" top/src/main/java/cz/vspj/smenarna/Money.java top/src/main/java/cz/vspj/smenarna/CnbClient.java checks/LogicCheck.java
java -cp "$work" LogicCheck

#!/usr/bin/env bash
# Runs every plain demo (before/, handrolled/, jdk/, enums/) with the JDK's source launcher, no build:
# proves each package stands alone, which is what lets a reader run `java Demo.java`.
set -euo pipefail
cd "$(dirname "$0")/.."
status=0
while IFS= read -r demo; do
  module=${demo%%/src/*}
  echo "== $demo"
  if ! (cd "$module" && java "${demo#"$module"/}"); then
    echo "FAILED: $demo"
    status=1
  fi
done < <(find . -path '*/src/main/java/*' -name '*Demo.java' -not -path '*/spring/*' | sed 's|^\./||' | sort)
exit $status

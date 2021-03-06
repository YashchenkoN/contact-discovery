#!/bin/bash

mongo="${MONGO:-mongo}"
mongoport="${MONGOPORT:-27017}"
mongouser="${MONGODB_USERNAME:-}"
mongopass="${MONGODB_PASSWORD:-}"
esnamespace="${ESNAMESPACE:-}"
elasticsearch="${ELASTICSEARCH:-elasticsearch}"
elasticport="${ELASTICPORT:-9200}"

function _mongo() {
    mongo --quiet --host ${mongo} --port ${mongoport} << EOF
    $@
EOF
}

function _prepare_mongo() {
    mongod --host ${mongo} --port ${mongoport} --replSet rs0
    sleep 10
    mongo --host ${mongo} --port ${mongoport} --eval 'rs.initiate()'
    sleep 10
}

_prepare_mongo

is_master_result="false"
expected_result="true"

while true;
do
  if [ "${is_master_result}" == "${expected_result}" ] ; then
    is_master_result=$(_mongo "rs.isMaster().ismaster")
    echo "Waiting for mongod node to assume primary status..."
    sleep 3
  else
    echo "mongod node is now primary"
    break;
  fi
done

sleep 15

mongo-connector --auto-commit-interval=0 \
  --continue-on-error \
  --namespace-set=${esnamespace} \
  --oplog-ts=/data/oplog.ts \
  --main=${mongo}:${mongoport} \
  --target-url=${elasticsearch}:${elasticport} \
  --doc-manager=elastic2_doc_manager \
  --admin-username=${mongouser} \
  --password=${mongopass} \
  --stdout
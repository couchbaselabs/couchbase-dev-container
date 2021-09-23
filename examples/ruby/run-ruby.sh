#!/bin/bash
source /etc/profile.d/rvm.sh

cd $(dirname ${1})

ruby ${1}

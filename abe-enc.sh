#!/bin/bash
#
# CPABE wrapper to simply encrypt strings. The policy is set
# such that the decrypter must have a certificate with 
# PRI > <message_priority> AND HOST = <message_host>
# OR the 'admin' tag.
#
# Usage : ./abe-enc.sh <data> <pri> <host>

pub_key="pub_key"
temp_file="temp.cpabe"

policy="((pri < $2 and $3) or admin)"

cpabe-enc -o $temp_file $pub_key <(echo "$1") "$policy"
cat $temp_file
rm $temp_file

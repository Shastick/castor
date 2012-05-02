#!/bin/bash
#
# CPABE wrapper to simply decrypt strings.
#
# Usage : ./abe-dec.sh <data>

pub_key="pub_key"
priv_key="priv_key"
temp_enc="temp_dec.cpabe"
temp_dec="temp_dec"

echo "$1" > $temp_enc

cpabe-dec $pub_key $priv_key $temp_enc

if [ $? -eq 0 ]
then
	cat $temp_dec
	rm $temp_dec $temp_enc
	exit 0
else
	rm $temp_enc
	exit 1
fi

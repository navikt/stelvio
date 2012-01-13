#!/bin/sh

# Abort on error
set -e

add_line()
{
	grep -q "$2" $1 || sudo sh -c "set -f; echo $2 >> $1"
}

# WAS needs a large stack, a high number of file handles, and many processes
add_line /etc/security/limits.conf "root soft stack 32768"
add_line /etc/security/limits.conf "root hard stack 32768"

add_line /etc/security/limits.conf "root soft nofile 65536"
add_line /etc/security/limits.conf "root hard nofile 65536"

add_line /etc/security/limits.conf "root soft nproc 16384"
add_line /etc/security/limits.conf "root hard nproc 16384"

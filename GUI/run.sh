#!/bin/sh

check_args() {
	# Check 2 arguments are given #
	if [ $# -lt 2 ]
	then
		echo "Usage : $0 option section"
		exit
	fi
}

test2 () {
	case "$1" in
		'/home/nicolas/cs356-HotSpot/gui/TestPrograms')
			_test $1 $2
			;;
		*) echo "$1 is not a valid option"
			exit
			;;
	esac
}

cleanall() {
	rm -f $1/*.class
	rm -f $1/*.txt
}

compile () {
	javac -d "$3" /$1/$2.java
	sleep 4
	_test $1 $2 $3
}

_test () {
	rm -f $3/dump.txt
	java -cp "$3" $2 2 >> $3/dump.txt &
	sleep 1
	kill -3 $!
	sleep 1
	kill -9 $!
	wait $! 2>/dev/null
	sleep 1
	grep -q 'Found' $3/dump.txt
	if [ $? -eq 0 ]
	then
		echo "Found deadlocks! Displaying deadlocked thread info..."
		sleep 2
		sed -n -e '/Found one/,/deadlock./w dump2.txt' $3/dump.txt
	else
		echo "Did not find deadlocks..."
		echo "No deadlocks detected by the HotSpot JVM." > dump2.txt
	fi
}

case "$1" in
	'compile')  echo "Compiling all"
		compile $2 $3 $4
		;;
	'clean')  echo  "Cleaning all"
		cleanall $2
		;;
	*) echo "$1 is not a valid option"
		exit
		;;
esac

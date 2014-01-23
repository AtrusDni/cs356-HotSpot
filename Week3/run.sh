#!/bin/sh

check_args() {
	# Check 2 arguments are given #
	if [ $# -lt 2 ]
	then
		echo "Usage : $0 option section"
		exit
	fi
}

test () {
	case "$1" in
		'found')
			_test $1 Deadl
			_test $1 ObjectDeadlock
			_test $1 ReentrantLockTest
			_test $1 DeadlockTest
			;;
		'notfound')
			_test $1 ReadWriteTest
			_test $1 Stack
			;;
		*) echo "$1 is not a valid option"
			exit
			;;
	esac
}

test2 () {
	case "$1" in
		'found')
			_test $1 $2
			;;
		'notfound')
			_test $1 $2
			;;
		*) echo "$1 is not a valid option"
			exit
			;;
	esac
}



clean () {
	rm -f $1/*.class
	rm -f $1/*/*.class
}

cleanall() {
	rm -f */*.class
}

compile () {
	javac -cp ./:"$1"/ $1/*.java
}

_test () {
	rm -f "$1"_"$2"_dump.txt
	java -cp ./:"$1"/ $2 2 >> "$1"_"$2"_dump.txt &
	sleep 1
	kill -3 $!
	sleep 1
	kill -9 $!
	wait $! 2>/dev/null
	sleep 1
	grep -q 'Found' ./"$1"_"$2"_dump.txt
	if [[ $? -eq 0 ]]
	then
		echo "Found deadlocks! Displaying deadlocked thread info..."
		sleep 2
		sed -n -e '/Found one/,/deadlock./ p' ./"$1"_"$2"_dump.txt
	else
		echo "Did not find deadlocks..."
	fi
}

case "$1" in
	'compile')  echo "Compiling all"
		compile found
		compile notfound
		;;
	'test')  echo  "Testing $2 $3"
		test2 $2 $3
		;;
	'testall') echo "Testing all in $2"
		test $2
		;;
	'clean')  echo  "Cleaning all"
		cleanall
		;;
	*) echo "$1 is not a valid option"
		exit
		;;
esac

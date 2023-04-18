TEST = EmptyException.java Queue.java LockFreeQueue.java TestQueues.java QueueTester.java
JFLAGS = java -ea

all: tester

tester:
	javac $(TEST)

run1: all
	$(JFLAGS) TestQueues 1

run10: all
	$(JFLAGS) TestQueues

run100: all
	$(JFLAGS) TestQueues 100

run1000: all
	$(JFLAGS) TestQueues 1000

clean:
	rm -rf *.class

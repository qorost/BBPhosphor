#The Java file that has been instrumented before
JAVA=../phosphor/Phosphor/target/jre-inst-int/bin/java
#The phosphor runnable 
PHOSPHOR=../phosphor/Phosphor/target/Phosphor-0.0.3-SNAPSHOT.jar
#The BBPhosphor jar file
BBPHOS=../BBPhosphor/target/BBPhosphor-1.0-SNAPSHOT.jar
#The test jar file
EXAMPLEJAR=../phosphor-examples/target/phosphor-examples-1.0-SNAPSHOT.jar
#Simple test class file
TESTCLASSFILE=./tests/Test.class

all:
	mvn package
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${BBPHOS} -cp ${EXAMPLEJAR} -ea com.josecambronero.IntegerTagExamples


implicit:
	mvn package
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${PHOSPHOR}=lightImplicit -cp ${EXAMPLEJAR} -ea com.josecambronero.IntegerTagExamples

normal:
	mvn package
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${PHOSPHOR} -cp ${EXAMPLEJAR} -ea com.josecambronero.IntegerTagExamples

test:
	mvn package
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${PHOSPHOR} ${TESTCLASSFILE}


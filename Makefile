#The Java file that has been instrumented before
JAVA=../phosphor/Phosphor/target/jre-inst-obj/bin/java
#The phosphor runnable 
PHOSPHOR=../phosphor/Phosphor/target/Phosphor-0.0.3-SNAPSHOT.jar
#The BBPhosphor jar file
BBPHOS=../BBPhosphor/target/BBPhosphor-1.0-SNAPSHOT.jar
#The test jar file
EXAMPLEJAR=../phosphor-examples/target/phosphor-examples-1.0-SNAPSHOT.jar

.PHONY: test ${TESTJAR} 


all: implicit normal testbb

PHOSPHOR: ${PHOSPHOR}
	echo "building phosphor"
	cd ../phosphor/Phosphor && mvn package



${BBPHOS}:
	mvn package

${PHOSPHOR}:
	cd ../phosphor/Phosphor && mvn package

${EXAMPLEJAR}:
	cd ../phosphor-examples && mvn package

clean:
	mvn clean

implicit: ${BBPHOS} ${PHOSPHOR} ${EXAMPLEJAR}
	echo "enable lightImplicit for phosphor-examples"
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${PHOSPHOR}=lightImplicit -cp ${EXAMPLEJAR} -ea com.josecambronero.ObjectTagExamples

normal: ${BBPHOS} ${PHOSPHOR} ${EXAMPLEJAR}
	echo "using phosphor in the normal way"
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${PHOSPHOR} -cp ${EXAMPLEJAR} -ea com.josecambronero.ObjectTagExamples



#Simple test class file
TESTJAR= tests/BBPhosphorTests/target/BBPhosphorTests-1.0-SNAPSHOT.jar
TESTCLASS = edu.ucdavis.cs.cyberlab.BBPhosphorTests.App


${TESTJAR}:
	echo "building ${TESTJAR}"
	cd tests/BBPhosphorTests && mvn package


testbb: ${TESTJAR} ${PHOSPHOR}
	echo "running ${TESTJAR} with bbphosphor, WITH_TAGS_FOR_JUMPS turned on"
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${BBPHOS} -cp ${TESTJAR} -ea ${TESTCLASS}


#testeg: ${TESTJAR} ${PHOSPHOR}
testeg: 
	echo "running ${TESTJAR} with bbphosphor, WITH_TAGS_FOR_JUMPS turned on"
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${BBPHOS} -cp ${EXAMPLEJAR} -ea com.josecambronero.ObjectTagExamples


testphosphor: ${TESTJAR} ${PHOSPHOR}
	echo "running ${TESTJAR} with bbphosphor, WITH_TAGS_FOR_JUMPS turned on"
	${JAVA} -Xbootclasspath/a:${PHOSPHOR} -javaagent:${PHOSPHOR} -cp ${TESTJAR} -ea ${TESTCLASS}

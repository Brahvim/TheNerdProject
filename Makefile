# Forgive me for this choice of using `make`.
# This is the best I could do, the best I was allowed to do, and also what I wanted to do.

JAVA_COMP=javac
NERD_SRC_DIR=src/com/brahvim/nerd/
APP_DIR=src/com/brahvim/nerd_tests

SRC_FILES=${NERD_SRC_DIR}/$(wildcard *.java)

#TheNerdProject.jar:
NewTar data lib bin:
	@echo Building...
	@echo Using sources from ${APP_DIR}.
	@${JAVA_COMP} ${SRC_FILES}
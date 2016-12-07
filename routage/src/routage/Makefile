# =================================================================
# File: routage/Makefile
 
sourcefiles = \
Client.java \
Camion.java \
Solution.java \
Recherche.java
 
classfiles = $(patsubst %.java,$(classpath)/%.class,$(sourcefiles))
 
ifndef classpath
export classpath = $(PWD)/class
endif
 
.PHONY: all clean subblocks
 
all: subblocks $(classfiles)
 
subblocks:
 
$(classpath)/%.class: %.java
	@mkdir -p $(classpath)
	javac -d $(classpath) -classpath $(classpath)  $<
 
clean:
	rm -f $(classpath)/*.class
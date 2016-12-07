# =================================================================
# File: routage/Makefile
 
sourcefiles = \
Camion.java \
Client.java \
Recherche.java \
Solution.java
 
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
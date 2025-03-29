# Variables
KOTLINC=kotlinc
JAVA=java
SRC_DIR=mypackage
BUILD_DIR=build
JAR=libGrafoKt.jar
MAIN=Main.kt
OUTPUT=Main.jar

all: clean lib compile

lib:
	@echo Compilando la libreria...
	if not exist $(BUILD_DIR) mkdir $(BUILD_DIR)
	$(KOTLINC) $(SRC_DIR)/*.kt -d $(BUILD_DIR)
	jar -cvf $(JAR) -C $(BUILD_DIR) .

compile: lib
	@echo Compilando el programa principal...
	$(KOTLINC) -cp $(JAR) $(MAIN) -include-runtime -d $(OUTPUT)

run:
	@echo Ejecutando el programa...
	$(JAVA) -cp "$(JAR);$(OUTPUT)" MainKt

clean:
	@echo Limpiando archivos generados...
	if exist $(OUTPUT) del /F /Q $(OUTPUT)
	if exist $(JAR) del /F /Q $(JAR)
	if exist $(BUILD_DIR) rmdir /S /Q $(BUILD_DIR)

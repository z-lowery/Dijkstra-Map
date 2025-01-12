runServer:
	@javac -cp lib\junit.jar src\*.java
	@sudo java WebApp 80 

runTests: lib\junit.jar
	@javac -cp lib\junit.jar *Tests.java
	@java -cp lib\junit.jar org.junit.platform.console.ConsoleLauncher --select-class BackendTests
	@java -cp lib\junit.jar org.junit.platform.console.ConsoleLauncher --select-class FrontendTests

clean:
	@rm *.class
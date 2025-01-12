runServer:
	@javac -cp .:../junit5.jar *.java
	@sudo java WebApp 80

runTests: ../junit5.jar
	@javac -cp .:../junit5.jar *Tests.java
	@java -cp .:../junit5.jar org.junit.platform.console.ConsoleLauncher --select-class BackendTests
	@java -cp .:../junit5.jar org.junit.platform.console.ConsoleLauncher --select-class FrontendTests

clean:
	@rm *.class
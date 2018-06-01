package org.gobiiproject.gobiiclient.gobii.instructions;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ADLEncapsulator {
    private static final String INPUT_HOST = "-h";
    private static final String INPUT_USER = "-u";
    private static final String INPUT_PASSWORD = "-p";
    private static final String INPUT_TIMEOUT = "-t";
    private static final String INPUT_SCENARIO = "-s";
    private static final String INPUT_DIRECTORY = "-d";
    private static final String SPACE = " ";
    private static final String JAVA_COMMAND = "java -jar";
    private static final String OUTPUT = "output.txt";
    private static final String ERROR = "error.txt";

    private String inputHost;
    private String inputUser;
    private String inputPassword;
    private Integer inputTimeout;
    private String inputScenario;
    private String inputDirectory;
    private String adlJarPath;

    private String getInputHost() {
        return inputHost;
    }

    public void setInputHost(String inputHost) {
        this.inputHost = inputHost;
    }

    private String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    private String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    private Integer getInputTimeout() {
        return inputTimeout;
    }

    public void setInputTimeout(Integer inputTimeout) {
        this.inputTimeout = inputTimeout;
    }

    private String getInputScenario() {
        return inputScenario;
    }

    public void setInputScenario(String inputScenario) {
        this.inputScenario = inputScenario;
    }

    private String getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    private String getAdlJarPath() {
        return adlJarPath;
    }

    public void setAdlJarPath(String adlJarPath) {
        this.adlJarPath = adlJarPath;
    }

    private String getBasicCommand() {
        String servercommand = JAVA_COMMAND + SPACE;
        servercommand += getAdlJarPath() + SPACE;
        servercommand += INPUT_HOST + SPACE + getInputHost() + SPACE;
        servercommand += INPUT_USER + SPACE + getInputUser() + SPACE;
        servercommand += INPUT_PASSWORD + SPACE + getInputPassword() + SPACE;
        servercommand += INPUT_TIMEOUT + SPACE + getInputTimeout() + SPACE;
        return servercommand;
    }

    private String getBatchAdlCommand() {
        String servercommand = getBasicCommand();
        servercommand += INPUT_DIRECTORY + SPACE + getInputDirectory() + SPACE;
        return servercommand;
    }

    private String getScenarioAdlCommand() {
        String servercommand = getBasicCommand();
        servercommand += INPUT_SCENARIO + SPACE + getInputScenario() + SPACE;
        return servercommand;
    }

    public String executeBatchGobiiADL() {
        if (getAdlJarPath() == null || getInputHost() == null || getInputUser() == null || getInputPassword() == null || getInputTimeout() == null || getInputDirectory() == null) {
            return "Please set all the input parameters and try again.";
        }
        boolean status = HelperFunctions.tryExec(getBatchAdlCommand(), getInputDirectory() + OUTPUT, getInputDirectory() + ERROR);
        if (status) {
            return "SUCCESS";
        } else {
            final String[] msg = {getBatchAdlCommand()};
            try (Stream<String> stream = Files.lines(Paths.get(getInputDirectory() + ERROR))) {
                stream.forEach(s -> msg[0] += s + SPACE);
            } catch (IOException e) {
                msg[0] += e.getMessage();
            }
            return msg[0];
        }
    }
}

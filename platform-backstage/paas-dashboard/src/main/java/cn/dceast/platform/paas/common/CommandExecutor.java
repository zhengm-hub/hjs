/**
 * Project:platform-paas
 * <p/>
 * File:CommandExecutor.java
 * <p/>
 * Package:cn.dceast.platform.paas.common
 * <p/>
 * Date:15/8/5上午2:03
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/5上午2:03		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.common;

import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 执行命令行的帮助类
 *
 * @ClassName: CommandExecutor
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/5 上午2:03
 * @Email shumin_1027@126.com
 */
public class CommandExecutor {
    private static Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    public static final int DEFAULT_INTERVAL = 1000;
    // 超时时间
    //default time out, in millseconds
    public static int DEFAULT_TIMEOUT = 15 * 60 * DEFAULT_INTERVAL; //15 min


    public static CommandResult exec(String command) throws IOException, InterruptedException {

        log.info("exec……");

        CommandLine cmdLine = CommandLine.parse(command);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(DEFAULT_TIMEOUT);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine, resultHandler);
        resultHandler.waitFor();


        CommandResult commandResult = new CommandResult();
        commandResult.setExitValue(resultHandler.getExitValue());
        commandResult.setOutput(outputStream.toString());
        commandResult.setError(errorStream.toString());

        streamHandler.stop();
        outputStream.close();
        errorStream.close();
        return commandResult;
    }

    public static CommandResult exec(String... commands) throws IOException, InterruptedException {
        log.info("exec……");

        StringBuilder cmdsb = new StringBuilder();
        for (int i = 0; i < commands.length; i++) {
            cmdsb.append(commands[i]).append(" ");
        }

        CommandLine cmdLine = CommandLine.parse(cmdsb.toString().trim());

        ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine, resultHandler);
        resultHandler.waitFor();


        CommandResult commandResult = new CommandResult();
        commandResult.setExitValue(resultHandler.getExitValue());
        commandResult.setOutput(outputStream.toString());
        commandResult.setError(errorStream.toString());

        outputStream.close();
        errorStream.close();
        return commandResult;
    }


    public static class CommandResult {
        public static final int EXIT_VALUE_TIMEOUT = -1;

        private String output;

        public Queue<String> outputQueue = new ArrayDeque<String>();

        void setOutput(String error) {
            output = error;
        }

        public String getOutput() {
            return output;
        }

        private int exitValue;

        void setExitValue(int value) {
            exitValue = value;
        }

        int getExitValue() {
            return exitValue;
        }

        private String error;

        /**
         * @return the error
         */
        public String getError() {
            return error;
        }

        /**
         * @param error the error to set
         */
        void setError(String error) {
            this.error = error;
        }
    }
}
package info.gianlucacosta.sagittarius.git;

import java.io.IOException;
import java.util.Arrays;


class Git {
    public static void addAll() {
        execGit(
                "add",
                "--all"
        );
    }


    public static void commit(String message) {
        execGit(
                "commit",
                "-m",
                message
        );
    }


    public static void push(String remote, String branch) {
        execGit(
                "push",
                remote,
                branch
        );
    }


    public static void createTag(String tag) {
        execGit(
                "tag",
                tag
        );
    }


    private static void execGit(String... commands) {
        String[] commandLine =
                new String[commands.length + 1];

        System.arraycopy(
                commands,
                0,
                commandLine,
                1,
                commands.length
        );

        commandLine[0] =
                "git";


        try {
            Process gitProcess =
                    Runtime.getRuntime().exec(
                            commandLine
                    );

            gitProcess.waitFor();

            if (gitProcess.exitValue() != 0) {
                throw new RuntimeException(
                        "Git failed with the following commands: " + Arrays.toString(commands)
                );
            }
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }


    private Git() {
    }
}

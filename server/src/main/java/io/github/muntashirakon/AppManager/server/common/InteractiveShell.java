// SPDX-License-Identifier: Apache-2.0

package io.github.muntashirakon.AppManager.server.common;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A wrapper for Runtime.exec that maintains an open stdin for continuous commands.
 * Optimizes background logging by consuming stdout/stderr in separate threads.
 */
public class InteractiveShell implements Closeable {
    private static final String TAG = "InteractiveShell";

    private final Process mProcess;
    private final OutputStream mStdin;
    private final ExecutorService mExecutor;
    private boolean mIsClosed = false;

    /**
     * Starts an interactive shell (e.g., "sh" or "su").
     *
     * @param command The command to start the shell (e.g., "sh").
     */
    public InteractiveShell(String command) throws IOException {
        mProcess = Runtime.getRuntime().exec(command);
        mStdin = mProcess.getOutputStream();
        mExecutor = Executors.newFixedThreadPool(2);

        // Start background threads to consume stdout and stderr
        mExecutor.submit(new StreamGobbler(mProcess.getInputStream(), "STDOUT"));
        mExecutor.submit(new StreamGobbler(mProcess.getErrorStream(), "STDERR"));
    }

    /**
     * Writes a command to the running shell's stdin.
     *
     * @param command The command to execute.
     */
    public synchronized void writeCommand(String command) throws IOException {
        if (mIsClosed) {
            throw new IOException("Shell is closed");
        }
        String cmd = command.endsWith("\n") ? command : command + "\n";
        mStdin.write(cmd.getBytes(StandardCharsets.UTF_8));
        mStdin.flush();
    }

    @Override
    public synchronized void close() throws IOException {
        if (mIsClosed) return;
        mIsClosed = true;

        try {
            // Try to exit gracefully first
            mStdin.write("exit\n".getBytes(StandardCharsets.UTF_8));
            mStdin.flush();
        } catch (IOException ignored) {}

        mStdin.close();
        mProcess.destroy();
        mExecutor.shutdownNow();
    }

    private static class StreamGobbler implements Runnable {
        private final InputStream mInputStream;
        private final String mType;

        StreamGobbler(InputStream inputStream, String type) {
            mInputStream = inputStream;
            mType = type;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // In a real implementation, this might callback to a listener.
                    // For now, we log it as per the "logging background" requirement.
                    Log.d(TAG, "[" + mType + "] " + line);
                }
            } catch (IOException e) {
                // Stream likely closed
            }
        }
    }
}

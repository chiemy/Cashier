package com.chiemy.app.cashier.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by chiemy on 16/2/21.
 */
public class DatabaseUtil {
    private static final String COMMAND_BACKUP = "backupDatabase";
    private static final String COMMAND_RESTORE = "restroeDatabase";

    // 数据恢复
    private void dataRecover(Context context, String dbname) {
        new BackupTask(context).execute(dbname, COMMAND_RESTORE);
    }

    // 数据备份
    private void dataBackup(Context context, String dbname) {
        new BackupTask(context).execute(dbname, COMMAND_BACKUP);
    }

    private static class BackupTask extends AsyncTask<String, Void, Integer> {
        private Context mContext;

        public BackupTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            File dbFile = mContext.getDatabasePath(params[0]);
            File exportDir = new File(Environment.getExternalStorageDirectory(),
                    "cashier");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File backup = new File(exportDir, dbFile.getName());
            String command = params[1];
            if (command.equals(COMMAND_BACKUP)) {
                try {
                    backup.createNewFile();
                    fileCopy(dbFile, backup);
                    return Log.d("backup", "ok");
                } catch (Exception e) {
                    e.printStackTrace();
                    return Log.d("backup", "fail");
                }
            } else if (command.equals(COMMAND_RESTORE)) {
                try {
                    fileCopy(backup, dbFile);
                    return Log.d("restore", "success");
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    return Log.d("restore", "fail");
                }
            } else {
                return null;
            }
        }

        private void fileCopy(File dbFile, File backup) throws IOException {
            // TODO Auto-generated method stub
            FileChannel inChannel = new FileInputStream(dbFile).getChannel();
            FileChannel outChannel = new FileOutputStream(backup).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        }
    }
}

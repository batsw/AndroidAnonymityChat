package com.batsw.anonimitychat.tor.bundle;

import android.content.res.AssetManager;
import android.util.Log;

import com.batsw.anonimitychat.appManagement.AppController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tudor on 11/6/2016.
 */

public class CopyTorResource {

    //TODO: insert logger
//    protected static final String TOR_PROCESS_MANAGER_TAG = TorProcessManager.class.getSimpleName();

    //TODO: insert logger to ENTER and LEAVE in the method
    public String provideTorResource(String internalRootFilesDir, AssetManager assetManager, String activityTagName) {

        String torResourceAbsolutePath = "";

        boolean ioExceptionFileRelated = false;

        try {
            //creating the android internal folder
            //TODO: singura solutie e sa creez un folder intern in care sa copiez toate resursele. Apoi sa ma joc cu ele ...
            String dirPath = internalRootFilesDir + TorConstants.INTERNAL_RESOURCE_FOLDER_NAME;
            File internalFile = new File(dirPath);

            Log.i(activityTagName, "CREATED_FILE= " + internalFile.getAbsolutePath());
            internalFile.mkdir();
            //Redundant  check to make sure the absolute path inside the Android OS env is properly created
            //RETURNS TRUE
            // abs path is ::::: /data/user/0/com.example.tudor.testbutton/files/torBundle
            if (internalFile.exists()) {
                Log.i(activityTagName, "CREATED_FILE= " + true + "\n" + internalFile.getAbsolutePath());
            } else {
                Log.i(activityTagName, "CREATED_FILE= " + false);
            }

            //after the folder is created and we know it exists ...
            //we copy the content of all assets tor files
//            final String[] torBundlesFiles = assetManager.list("tor_bundle");
            final String[] torBundlesFiles = assetManager.list(TorConstants.FOLDER_NAME_FOR_RESOURCES_IN_ASSETS);

            for (String fileName : torBundlesFiles) {
                Log.i(activityTagName, "tor_bundle contains: " + fileName);

                InputStream inputStreamFromAssetsFile = assetManager.open(TorConstants.FOLDER_NAME_FOR_RESOURCES_IN_ASSETS + "/" + fileName);
                copyInputStreamToFile(inputStreamFromAssetsFile, internalFile, fileName);
            }

            for (String copiedFileNames : internalFile.list()) {

                Log.i(activityTagName, "---> copiedFileNames = " + copiedFileNames);
            }

            torResourceAbsolutePath = internalFile.getAbsolutePath();

        } catch (IOException e) {
            Log.e(activityTagName, "Killing the previous TorProcess");
            android.os.Process.killProcess((int) AppController.getInstanceParameterized(null).getBundlePid());
            Log.e(activityTagName, "Unable to locate files in the tor_bundle folder...." + e.getMessage(), e);
        }

        return torResourceAbsolutePath;
    }

    private void copyInputStreamToFile(InputStream in, File fileDirectory, String fileName) throws IOException {

        File newFile = new File(fileDirectory.getAbsolutePath() + "/" + fileName);
        OutputStream out = new FileOutputStream(newFile);
        try {

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            out.close();
            in.close();
        }
    }
}

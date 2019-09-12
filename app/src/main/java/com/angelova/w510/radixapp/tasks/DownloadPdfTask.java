package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.InvoiceDetailsActivity;
import com.angelova.w510.radixapp.clients.InvoicesRestClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class DownloadPdfTask extends AsyncTask<Void, Void, Object> {

    private Activity context;
    private String fileName;
    private String invoiceId;
    private String url;
    private String accessToken;

    public DownloadPdfTask(Activity context, String fileName, String invoiceId, String endpoint, String accessToken) {
        this.context = context;
        this.fileName = fileName;
        this.invoiceId = invoiceId;
        this.url = endpoint;
        this.accessToken = accessToken;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                url = String.format("%s?fileName=%s&consecutiveID=%s", url, fileName, invoiceId);
                try {
                    File outputDir = context.getCacheDir(); // context being the Activity pointer
                    File outputFile = new File(outputDir.getAbsolutePath() + File.separator + invoiceId + ".pdf");
                    if(!outputFile.exists()) {
                        outputFile.createNewFile();
                    }
                    //File file = File.createTempFile(reportId, null, outputDir);
                    //String bearerAuthorization = String.format("Bearer " + accessToken);

                    InvoicesRestClient.getPdfFile(context, url, "x-access-token", accessToken, new FileAsyncHttpResponseHandler(outputFile) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            System.out.println("Success");
                            ((InvoiceDetailsActivity) context).handleFileDownloaded(file);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable error, File file) {
                            System.out.printf("Error!");
//                            ((MenuActivity)context).showErrorMessage("Something went wrong");
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
//                            if(progressDialog != null) {
//                                progressDialog.setProgress((int) ((bytesWritten * 100) / totalSize));
//                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        mainHandler.post(myRunnable);

        return null;
    }
}

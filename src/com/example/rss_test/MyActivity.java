package com.example.rss_test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

public class MyActivity extends Activity {

    private ScrollView scrollView;
    private EditText editText;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        scrollView = (ScrollView) findViewById(R.id.scrollRSS);
        editText = (EditText) findViewById(R.id.rss_edit_text);
        editText.setText("http://www.mokjozefow.pl/index.php?format=feed&type=rss");
    }

    public void callRSS(View view) {
        String uri = editText.getText().toString();
        new CheckRSSTask().execute(uri);
    }

    private class CheckRSSTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... urls) {
            RSSReader reader = new RSSReader();
            String uri = (String) urls[0];
            RSSFeed feed = null;
            try {
                feed = reader.load(uri);
            } catch (RSSReaderException e) {
                e.printStackTrace();
            }
            return feed;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            RSSFeed feed = (RSSFeed) o;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout allRSSView = new LinearLayout(MyActivity.this);
            allRSSView.setLayoutParams(params);
            allRSSView.setOrientation(LinearLayout.VERTICAL);
            for(RSSItem item : feed.getItems()) {
                TextView title = new TextView(MyActivity.this);
                title.setText(item.getTitle());
                WebView descriptionView = new WebView(MyActivity.this);
                descriptionView.setLayoutParams(params);
                descriptionView.getSettings().setAllowFileAccess(true);
//                descriptionView.getSettings().setAllowFileAccessFromFileURLs(true);
                descriptionView.getSettings().setLoadsImagesAutomatically(true);
//                descriptionView.getSettings().setAllowContentAccess(true);
                descriptionView.getSettings().setJavaScriptEnabled(true);
                descriptionView.loadData("<html>\n<body\n><head>\n" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "</head>\n " + item.getDescription() + "\n</body>\n</html>", "text/html", null);
                allRSSView.addView(title);
                allRSSView.addView(descriptionView);
            }
            scrollView.removeAllViews();
            scrollView.addView(allRSSView);
        }
    }
}

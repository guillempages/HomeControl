package cat.guillempages.homecontrol;

import android.Manifest.permission;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ai.api.AIConfiguration.SupportedLanguages;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends Activity implements AIListener, OnInitListener {

    private static final String TAG = "HomeControl";

    private static final Bundle SPEECH_PARAMS = new Bundle();
    static {SPEECH_PARAMS.putInt(Engine.KEY_PARAM_STREAM, AudioManager.STREAM_NOTIFICATION);
    }

    private AIService mAiService;
    private TextView mResultTextView;
    private ToggleButton mListenButton;

    private TextToSpeech mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AIConfiguration config = new AIConfiguration("f54f2605a9d14bf5bbe40aa44f618c63",
                SupportedLanguages.German,
                AIConfiguration.RecognitionEngine.System);
        mAiService = AIService.getService(this, config);
        mAiService.setListener(this);

        mListenButton = (ToggleButton) findViewById(R.id.listen_button);
        mListenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                 startListening();
            }
        });
        mResultTextView = ((TextView) findViewById(R.id.main_text_view));
        mTts = new TextToSpeech(this, this);

        for (final EngineInfo engine: mTts.getEngines()) {
            Log.d(TAG, "Available Engine: " + engine);
        }
    }

    private void startListening() {
        if (checkSelfPermission(permission.RECORD_AUDIO) == PERMISSION_GRANTED) {
            mAiService.startListening();
        } else {
            requestPermissions(new String[] {permission.RECORD_AUDIO}, 42);
        }
    }

    @Override
    protected void onPause() {
        mAiService.stopListening();
        super.onPause();
    }

    @Override
    public void onResult(final AIResponse response) {
        Log.i(TAG, "Got response: " + response);
        final Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry
                    : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }
        final String responseSpeech = result.getFulfillment().getSpeech();
        speak(responseSpeech);

        switch (result.getAction()) {
            case "radio_on":
                Log.d(TAG, "Switch radio on: " + parameterString);
                radioOn(result.getParameters());
                break;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Show results in TextView.
                mResultTextView.setText(responseSpeech);
            }
        });
    }

    /**
     * Speak the given text through the notification stream.
     *
     * @param responseSpeech The text to speak.
     */
    private void speak(final String responseSpeech) {
        mTts.speak(responseSpeech, TextToSpeech.QUEUE_FLUSH, SPEECH_PARAMS, responseSpeech);
    }

    /**
     * Switch the radio on.
     *
     * @param parameters The user supplied parameters to help decide which radio to turn on.
     */
    private void radioOn(final HashMap<String, JsonElement> parameters) {

    }

    @Override
    public void onError(final AIError error) {
        Log.e(TAG, "Got error: " + error);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResultTextView.setText(error.getMessage());
            }
        });
    }

    @Override
    public void onAudioLevel(final float level) {

    }

    @Override
    public void onListeningStarted() {
        Log.d(TAG, "Listening Started");
        mListenButton.setChecked(true);
    }

    @Override
    public void onListeningCanceled() {
        Log.d(TAG, "Listening Cancelled");
        mListenButton.setChecked(false);
    }

    @Override
    public void onListeningFinished() {
        Log.d(TAG, "Listening Finished");
        mListenButton.setChecked(false);
    }

    // TextToSpeech
    @Override
    public void onInit(final int status) {
        if(status != TextToSpeech.ERROR) {
            mTts.setLanguage(Locale.GERMANY);
        }
    }
}

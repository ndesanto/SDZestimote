package com.estimote.SDZRetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.estimote.coresdk.cloud.model.Color;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.SDZRetail.estimote.BeaconID;
import com.estimote.SDZRetail.estimote.EstimoteCloudBeaconDetails;
import com.estimote.SDZRetail.estimote.EstimoteCloudBeaconDetailsFactory;
import com.estimote.SDZRetail.estimote.ProximityContentManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final Map<Color, Integer> BACKGROUND_COLORS = new HashMap<>();

    static {
        BACKGROUND_COLORS.put(Color.ICY_MARSHMALLOW, android.graphics.Color.rgb(109, 170, 199));
        BACKGROUND_COLORS.put(Color.BLUEBERRY_PIE, android.graphics.Color.rgb(98, 84, 158));
        BACKGROUND_COLORS.put(Color.MINT_COCKTAIL, android.graphics.Color.rgb(155, 186, 160));
    }

    private static final int BACKGROUND_COLOR_NEUTRAL = android.graphics.Color.rgb(160, 169, 172);

    private ProximityContentManager proximityContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        // TODO: replace with UUIDs, majors and minors of your own beacons
                        new BeaconID("4D51F886-B6D3-95D1-F9FD-08E5CEC98D19", 50560, 5440),
                        new BeaconID("C4AEF61C-6650-8254-C0D3-519683C8C518", 12156, 19757),
                        new BeaconID("1B9CAD88-C99D-F151-4583-90EC99F9847D", 48607, 20285),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 11872, 20281),
                        new BeaconID("C107E355-9FE8-D41D-EB03-38D7EC369DCA", 22637, 36183),
                        new BeaconID("9DD50A54-4A51-F7E8-3D55-296F2B44E4A1", 44817, 47528)),
                new EstimoteCloudBeaconDetailsFactory());
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;
                Integer backgroundColor;
                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
                    text = "You're in " + beaconDetails.getBeaconName() + "'s range!";
                    backgroundColor = BACKGROUND_COLORS.get(beaconDetails.getBeaconColor());
                } else {
                    text = "No beacons in range.";
                    backgroundColor = null;
                }
                ((TextView) findViewById(R.id.textView)).setText(text);
                findViewById(R.id.relativeLayout).setBackgroundColor(
                        backgroundColor != null ? backgroundColor : BACKGROUND_COLOR_NEUTRAL);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
            proximityContentManager.startContentUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ProximityContentManager content updates");
        proximityContentManager.stopContentUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
    }
}

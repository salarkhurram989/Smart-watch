package com.salarx.haylou;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * API-15-compatible dashboard prototype.
 *
 * Important: Android 4.0.4 has no public Android BLE GATT API.
 * The LS02 BLE connection is not implemented here; the app must not
 * pretend that it has connected or display invented sensor readings.
 */
public class MainActivity extends Activity {
    private static final int BG = Color.rgb(245, 247, 250);
    private static final int INK = Color.rgb(25, 32, 43);
    private static final int MUTED = Color.rgb(105, 116, 130);
    private static final int CYAN = Color.rgb(0, 164, 190);
    private LinearLayout content;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        showHome();
    }

    private void showHome() {
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(22));
        content.setBackgroundColor(BG);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(content);
        setContentView(scroll);

        addText(content, "SALARX", 13, CYAN, true);
        addText(content, "Haylou Connect", 27, INK, true);
        addText(content, "Smartwatch dashboard", 14, MUTED, false);
        addGap(content, 18);

        LinearLayout device = card();
        addText(device, "MY DEVICE", 12, MUTED, true);
        addText(device, "Haylou Smart Watch 2 (LS02)", 18, INK, true);
        addText(device, "Status: Not connected", 15, Color.rgb(190, 92, 30), true);
        addText(device, "Compatibility warning: this tablet model is specified with Bluetooth 3.0, while the LS02 requires BLE. Android 4.0.4 also lacks the standard BLE GATT API. Direct connection is not supported by this prototype.", 13, MUTED, false);
        Button connect = new Button(this);
        connect.setText("CHECK CONNECTION SUPPORT");
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showBluetoothDiagnostic(); }
        });
        device.addView(connect, matchWrap());
        content.addView(device, matchWrap());
        addGap(content, 12);

        addSectionTitle(content, "HEALTH OVERVIEW");
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(metricCard("STEPS", "Unavailable", "Waiting for watch data"), weighted());
        row1.addView(metricCard("HEART RATE", "Unavailable", "Waiting for watch data"), weighted());
        content.addView(row1, matchWrap());
        addGap(content, 8);
        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.addView(metricCard("SLEEP", "Unavailable", "Waiting for watch data"), weighted());
        row2.addView(metricCard("BATTERY", "Unavailable", "Waiting for watch data"), weighted());
        content.addView(row2, matchWrap());
        addGap(content, 16);

        Button reports = new Button(this);
        reports.setText("VIEW SMARTWATCH REPORTS");
        reports.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showReports(); }
        });
        content.addView(reports, matchWrap());

        Button diagnostics = new Button(this);
        diagnostics.setText("BLUETOOTH DIAGNOSTICS");
        diagnostics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showBluetoothDiagnostic(); }
        });
        content.addView(diagnostics, matchWrap());

        addGap(content, 14);
        addText(content, "Honest data only: measurements remain unavailable until a real LS02 connection and protocol are implemented and tested.", 12, MUTED, false);
    }

    private void showReports() {
        content.removeAllViews();
        addText(content, "SALARX", 13, CYAN, true);
        addText(content, "Smartwatch Reports", 26, INK, true);
        addText(content, "Daily • Weekly • Monthly", 14, MUTED, false);
        addGap(content, 16);

        addReport("DAILY REPORT", "No watch measurements received.");
        addReport("WEEKLY REPORT", "A trend will appear after real data is synchronized.");
        addReport("MONTHLY REPORT", "Historical reports require actual readings and timestamps.");
        addReport("SYNC REPORT", "Last successful sync: Never");
        addReport("DEVICE REPORT", "Model: Haylou LS02\nConnection: Not connected\nBattery: Unavailable\nFirmware: Unavailable");
        addGap(content, 12);

        Button back = new Button(this);
        back.setText("BACK TO DASHBOARD");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showHome(); }
        });
        content.addView(back, matchWrap());
    }

    private void addReport(String title, String body) {
        LinearLayout box = card();
        addText(box, title, 12, CYAN, true);
        addText(box, body, 15, INK, false);
        content.addView(box, matchWrap());
        addGap(content, 8);
    }

    private void showBluetoothDiagnostic() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        String message;
        if (adapter == null) {
            message = "This tablet does not expose a Bluetooth adapter to Android. The LS02 connection cannot be tested.";
        } else if (!adapter.isEnabled()) {
            message = "Bluetooth adapter detected but Bluetooth is turned off. You may enable Bluetooth in Android Settings. Note: classic Bluetooth discovery does not equal BLE support.";
        } else {
            message = "Bluetooth adapter is present and enabled. Android 4.0.4 does not provide the standard BLE GATT APIs needed by the LS02. This prototype cannot establish an LS02 BLE connection yet.";
        }
        new AlertDialog.Builder(this)
            .setTitle("Connection diagnostic")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private LinearLayout metricCard(String label, String value, String note) {
        LinearLayout box = card();
        addText(box, label, 11, MUTED, true);
        addText(box, value, 17, INK, true);
        addText(box, note, 11, MUTED, false);
        return box;
    }

    private LinearLayout card() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(14), dp(14), dp(14), dp(14));
        box.setBackgroundColor(Color.WHITE);
        return box;
    }

    private void addSectionTitle(LinearLayout parent, String text) {
        addText(parent, text, 12, MUTED, true);
        addGap(parent, 8);
    }

    private void addText(LinearLayout parent, String text, int size, int color, boolean bold) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(color);
        tv.setPadding(0, dp(3), 0, dp(5));
        if (bold) tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        parent.addView(tv, matchWrap());
    }

    private void addGap(LinearLayout parent, int height) {
        View gap = new View(this);
        parent.addView(gap, new LinearLayout.LayoutParams(1, dp(height)));
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams weighted() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        p.setMargins(dp(3), 0, dp(3), 0);
        return p;
    }

    private int dp(int value) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
